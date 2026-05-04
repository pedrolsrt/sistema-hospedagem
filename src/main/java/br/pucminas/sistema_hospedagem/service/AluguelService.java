package br.pucminas.sistema_hospedagem.service;

import br.pucminas.sistema_hospedagem.dto.AluguelRequestDTO;
import br.pucminas.sistema_hospedagem.dto.AluguelResponseDTO;
import br.pucminas.sistema_hospedagem.dto.ReciboResponseDTO;
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
    private static final double ADICIONAL_CAMA_SOLTEIRO_EXTRA = 25.0;
    private static final double ADICIONAL_CAMA_QUEEN = 35.0;
    private static final double ADICIONAL_CAMA_KING = 55.0;
    private static final double ADICIONAL_BERCO = 30.0;
    private static final double ADICIONAL_HOSPEDE_FAMILIA = 20.0;
    private static final double ADICIONAL_AMBIENTE_FAMILIA = 40.0;
    private static final double DESCONTO_GRUPO_FAMILIA = 0.10;

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

        Integer numeroHospedes = aluguelRequestDTO.getNumeroHospedes();
        Boolean solicitaBerco = Boolean.TRUE.equals(aluguelRequestDTO.getSolicitaBerco());

        validarCapacidadeDoQuarto(quarto, numeroHospedes);
        validarSolicitacaoDeBerco(quarto, solicitaBerco);

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

        Double valorDiariaCalculado = calcularValorDiaria(quarto, numeroHospedes, solicitaBerco);
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
        aluguel.setNumeroHospedes(numeroHospedes);
        aluguel.setSolicitouBerco(solicitaBerco);
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

    public ReciboResponseDTO gerarRecibo(Long id) {
        Aluguel aluguel = aluguelRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Aluguel não encontrado com o id: " + id));

        return new ReciboResponseDTO(
                aluguel.getId(),
                aluguel.getCliente().getNome(),
                aluguel.getResidencia().getEndereco(),
                aluguel.getQuarto().getId(),
                aluguel.getDataEntrada(),
                aluguel.getDataSaida(),
                aluguel.getQuantidadeDiarias(),
                aluguel.getValorFinal(),
                aluguel.getPagamento().getStatus().name(),
                montarTextoRecibo(aluguel)
        );
    }

    private void validarDatas(LocalDateTime dataEntrada, LocalDateTime dataSaida) {
        if (!dataSaida.isAfter(dataEntrada)) {
            throw new RegraDeNegocioException("A data de saída deve ser posterior à data de entrada.");
        }
    }

    private void validarCapacidadeDoQuarto(Quarto quarto, Integer numeroHospedes) {
        if (numeroHospedes == null || numeroHospedes <= 0) {
            throw new RegraDeNegocioException("O número de hóspedes deve ser maior que zero.");
        }

        if (numeroHospedes > quarto.getCapacidadeMaxima()) {
            throw new RegraDeNegocioException("O número de hóspedes excede a capacidade máxima do quarto.");
        }
    }

    private void validarSolicitacaoDeBerco(Quarto quarto, Boolean solicitaBerco) {
        if (Boolean.TRUE.equals(solicitaBerco) && !Boolean.TRUE.equals(quarto.getPermiteBerco())) {
            throw new RegraDeNegocioException("O quarto selecionado não permite berço.");
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

    private Double calcularValorDiaria(Quarto quarto, Integer numeroHospedes, Boolean solicitaBerco) {
        double valor = quarto.getValorBaseDiaria();

        if (Boolean.TRUE.equals(quarto.getPossuiArCondicionado())) {
            valor += ADICIONAL_AR_CONDICIONADO;
        }

        if (Boolean.TRUE.equals(quarto.getPossuiHidromassagem())) {
            valor += ADICIONAL_HIDROMASSAGEM;
        }

        if (quarto.getTipo() == TipoQuarto.INDIVIDUAL) {
            valor += calcularAdicionalQuartoIndividual(quarto);
        }

        if (quarto.getTipo() == TipoQuarto.CASAL) {
            valor += calcularAdicionalQuartoCasal(quarto, solicitaBerco);
        }

        if (quarto.getTipo() == TipoQuarto.FAMILIA) {
            valor += calcularAdicionalQuartoFamilia(quarto, numeroHospedes);
            valor = aplicarDescontoProgressivoFamilia(valor, numeroHospedes);
        }

        return valor;
    }

    private Double calcularAdicionalQuartoIndividual(Quarto quarto) {
        int camasExtras = quarto.getQuantidadeCamasSolteiro() - 1;

        if (camasExtras <= 0) {
            return 0.0;
        }

        return camasExtras * ADICIONAL_CAMA_SOLTEIRO_EXTRA;
    }

    private Double calcularAdicionalQuartoCasal(Quarto quarto, Boolean solicitaBerco) {
        double adicional = 0.0;

        adicional += quarto.getQuantidadeCamasQueen() * ADICIONAL_CAMA_QUEEN;
        adicional += quarto.getQuantidadeCamasKing() * ADICIONAL_CAMA_KING;

        if (Boolean.TRUE.equals(solicitaBerco)) {
            adicional += ADICIONAL_BERCO;
        }

        return adicional;
    }

    private Double calcularAdicionalQuartoFamilia(Quarto quarto, Integer numeroHospedes) {
        double adicional = 0.0;

        adicional += numeroHospedes * ADICIONAL_HOSPEDE_FAMILIA;

        if (quarto.getQuantidadeAmbientes() > 1) {
            adicional += (quarto.getQuantidadeAmbientes() - 1) * ADICIONAL_AMBIENTE_FAMILIA;
        }

        adicional += quarto.getQuantidadeCamasQueen() * ADICIONAL_CAMA_QUEEN;
        adicional += quarto.getQuantidadeCamasKing() * ADICIONAL_CAMA_KING;

        return adicional;
    }

    private Double aplicarDescontoProgressivoFamilia(Double valor, Integer numeroHospedes) {
        if (numeroHospedes >= 5) {
            return valor - (valor * DESCONTO_GRUPO_FAMILIA);
        }

        return valor;
    }

    private String montarTextoRecibo(Aluguel aluguel) {
        return "=== RECIBO DE HOSPEDAGEM ===" + System.lineSeparator()
                + "Cliente: " + aluguel.getCliente().getNome() + System.lineSeparator()
                + "Residência: " + aluguel.getResidencia().getEndereco() + System.lineSeparator()
                + "Quarto: " + aluguel.getQuarto().getId() + System.lineSeparator()
                + "Tipo do quarto: " + aluguel.getQuarto().getTipo().name() + System.lineSeparator()
                + "Número de hóspedes: " + aluguel.getNumeroHospedes() + System.lineSeparator()
                + "Berço solicitado: " + (Boolean.TRUE.equals(aluguel.getSolicitouBerco()) ? "Sim" : "Não") + System.lineSeparator()
                + "Data e horário de entrada: " + aluguel.getDataEntrada() + System.lineSeparator()
                + "Data e horário de saída: " + aluguel.getDataSaida() + System.lineSeparator()
                + "Número de diárias: " + aluguel.getQuantidadeDiarias() + System.lineSeparator()
                + "Total à pagar: R$ " + String.format("%.2f", aluguel.getValorFinal()) + System.lineSeparator()
                + "Status do pagamento: " + aluguel.getPagamento().getStatus().name();
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
                montarTextoRecibo(aluguel)
        );
    }
}