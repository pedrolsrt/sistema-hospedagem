package br.pucminas.sistema_hospedagem.service;

import br.pucminas.sistema_hospedagem.dto.AluguelRequestDTO;
import br.pucminas.sistema_hospedagem.dto.AluguelResponseDTO;
import br.pucminas.sistema_hospedagem.enums.StatusPagamento;
import br.pucminas.sistema_hospedagem.enums.StatusReserva;
import br.pucminas.sistema_hospedagem.enums.TipoQuarto;
import br.pucminas.sistema_hospedagem.exception.RegraDeNegocioException;
import br.pucminas.sistema_hospedagem.exception.RecursoNaoEncontradoException;
import br.pucminas.sistema_hospedagem.model.Aluguel;
import br.pucminas.sistema_hospedagem.model.Cliente;
import br.pucminas.sistema_hospedagem.model.Pagamento;
import br.pucminas.sistema_hospedagem.model.Quarto;
import br.pucminas.sistema_hospedagem.model.Reserva;
import br.pucminas.sistema_hospedagem.model.Residencia;
import br.pucminas.sistema_hospedagem.repository.AluguelRepository;
import br.pucminas.sistema_hospedagem.repository.ClienteRepository;
import br.pucminas.sistema_hospedagem.repository.PagamentoRepository;
import br.pucminas.sistema_hospedagem.repository.QuartoRepository;
import br.pucminas.sistema_hospedagem.repository.ReservaRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class AluguelService {

    private static final double ADICIONAL_AR_CONDICIONADO = 30.0;
    private static final double ADICIONAL_HIDROMASSAGEM = 50.0;
    private static final double ADICIONAL_QUARTO_CASAL = 40.0;

    private final AluguelRepository aluguelRepository;
    private final PagamentoRepository pagamentoRepository;
    private final ClienteRepository clienteRepository;
    private final QuartoRepository quartoRepository;
    private final ReservaRepository reservaRepository;

    public AluguelService(AluguelRepository aluguelRepository,
                          PagamentoRepository pagamentoRepository,
                          ClienteRepository clienteRepository,
                          QuartoRepository quartoRepository,
                          ReservaRepository reservaRepository) {
        this.aluguelRepository = aluguelRepository;
        this.pagamentoRepository = pagamentoRepository;
        this.clienteRepository = clienteRepository;
        this.quartoRepository = quartoRepository;
        this.reservaRepository = reservaRepository;
    }

    public AluguelResponseDTO cadastrarAluguel(AluguelRequestDTO aluguelRequestDTO) {
        validarDatas(aluguelRequestDTO.getDataEntrada(), aluguelRequestDTO.getDataSaida());

        Cliente cliente = clienteRepository.findById(aluguelRequestDTO.getClienteId())
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Cliente não encontrado com o id: " + aluguelRequestDTO.getClienteId()
                ));

        Quarto quarto = quartoRepository.findById(aluguelRequestDTO.getQuartoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Quarto não encontrado com o id: " + aluguelRequestDTO.getQuartoId()
                ));

        Residencia residencia = quarto.getResidencia();

        validarDisponibilidadeDoQuarto(
                quarto.getId(),
                aluguelRequestDTO.getDataEntrada(),
                aluguelRequestDTO.getDataSaida()
        );

        Integer quantidadeDiarias = calcularQuantidadeDiarias(
                aluguelRequestDTO.getDataEntrada(),
                aluguelRequestDTO.getDataSaida()
        );

        Double valorDiariaCalculado = calcularValorDiaria(quarto);
        Double valorFinal = valorDiariaCalculado * quantidadeDiarias;

        Pagamento pagamento = new Pagamento();
        pagamento.setValor(valorFinal);
        pagamento.setStatus(StatusPagamento.PENDENTE);
        Pagamento pagamentoSalvo = pagamentoRepository.save(pagamento);

        Aluguel aluguel = new Aluguel();
        aluguel.setCliente(cliente);
        aluguel.setQuarto(quarto);
        aluguel.setResidencia(residencia);
        aluguel.setDataEntrada(aluguelRequestDTO.getDataEntrada());
        aluguel.setDataSaida(aluguelRequestDTO.getDataSaida());
        aluguel.setQuantidadeDiarias(quantidadeDiarias);
        aluguel.setValorFinal(valorFinal);
        aluguel.setPagamento(pagamentoSalvo);

        Aluguel aluguelSalvo = aluguelRepository.save(aluguel);
        return converterParaResponseDTO(aluguelSalvo);
    }

    public List<AluguelResponseDTO> listarTodos() {
        return aluguelRepository.findAll()
                .stream()
                .map(this::converterParaResponseDTO)
                .toList();
    }

    public AluguelResponseDTO buscarPorId(Long id) {
        Aluguel aluguel = aluguelRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Aluguel não encontrado com o id: " + id));

        return converterParaResponseDTO(aluguel);
    }

    public AluguelResponseDTO marcarPagamentoComoPago(Long id) {
        Aluguel aluguel = aluguelRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Aluguel não encontrado com o id: " + id));

        Pagamento pagamento = aluguel.getPagamento();
        pagamento.setStatus(StatusPagamento.PAGO);
        pagamentoRepository.save(pagamento);

        return converterParaResponseDTO(aluguel);
    }

    private void validarDatas(LocalDateTime dataEntrada, LocalDateTime dataSaida) {
        if (!dataSaida.isAfter(dataEntrada)) {
            throw new RegraDeNegocioException("A data de saída deve ser posterior à data de entrada.");
        }
    }

    private void validarDisponibilidadeDoQuarto(Long quartoId, LocalDateTime dataEntrada, LocalDateTime dataSaida) {
        List<Aluguel> alugueisConflitantes =
                aluguelRepository.findByQuartoIdAndDataEntradaLessThanEqualAndDataSaidaGreaterThanEqual(
                        quartoId,
                        dataSaida,
                        dataEntrada
                );

        if (!alugueisConflitantes.isEmpty()) {
            throw new RegraDeNegocioException("O quarto já está ocupado no período informado.");
        }

        List<Reserva> reservasConflitantes =
                reservaRepository.findByQuartoIdAndStatusAndDataEntradaLessThanEqualAndDataSaidaGreaterThanEqual(
                        quartoId,
                        StatusReserva.ATIVA,
                        dataSaida,
                        dataEntrada
                );

        if (!reservasConflitantes.isEmpty()) {
            throw new RegraDeNegocioException("O quarto possui uma reserva ativa para o período informado.");
        }
    }

    private Integer calcularQuantidadeDiarias(LocalDateTime dataEntrada, LocalDateTime dataSaida) {
        LocalDateTime entradaBase = ajustarEntradaParaBase(dataEntrada);
        LocalDateTime saidaBase = ajustarSaidaParaBase(dataSaida);

        long diarias = Duration.between(entradaBase, saidaBase).toDays();

        if (diarias <= 0) {
            diarias = 1;
        }

        return (int) diarias;
    }

    private LocalDateTime ajustarEntradaParaBase(LocalDateTime dataEntrada) {
        LocalDateTime baseNoDia = LocalDateTime.of(dataEntrada.toLocalDate(), LocalTime.NOON);

        if (dataEntrada.toLocalTime().isAfter(LocalTime.NOON)) {
            return baseNoDia;
        }

        return baseNoDia.minusDays(1);
    }

    private LocalDateTime ajustarSaidaParaBase(LocalDateTime dataSaida) {
        LocalDateTime baseNoDia = LocalDateTime.of(dataSaida.toLocalDate(), LocalTime.NOON);

        if (dataSaida.toLocalTime().isAfter(LocalTime.NOON)) {
            return baseNoDia.plusDays(1);
        }

        return baseNoDia;
    }

    private Double calcularValorDiaria(Quarto quarto) {
        double valor = quarto.getValorBaseDiaria();

        if (quarto.getTipo() == TipoQuarto.CASAL) {
            valor += ADICIONAL_QUARTO_CASAL;
        }

        if (Boolean.TRUE.equals(quarto.getPossuiArCondicionado())) {
            valor += ADICIONAL_AR_CONDICIONADO;
        }

        if (Boolean.TRUE.equals(quarto.getPossuiHidromassagem())) {
            valor += ADICIONAL_HIDROMASSAGEM;
        }

        return valor;
    }

    private String gerarRecibo(Aluguel aluguel) {
        return "Data e horário de entrada: " + aluguel.getDataEntrada() + System.lineSeparator()
                + "Data e horário de saída: " + aluguel.getDataSaida() + System.lineSeparator()
                + "Número de diárias: " + aluguel.getQuantidadeDiarias() + System.lineSeparator()
                + "Total à pagar: R$ " + String.format("%.2f", aluguel.getValorFinal());
    }

    private AluguelResponseDTO converterParaResponseDTO(Aluguel aluguel) {
        return new AluguelResponseDTO(
                aluguel.getId(),
                aluguel.getCliente().getId(),
                aluguel.getQuarto().getId(),
                aluguel.getResidencia().getId(),
                aluguel.getDataEntrada(),
                aluguel.getDataSaida(),
                aluguel.getQuantidadeDiarias(),
                aluguel.getValorFinal(),
                aluguel.getPagamento().getId(),
                aluguel.getPagamento().getStatus().name(),
                gerarRecibo(aluguel)
        );
    }
}