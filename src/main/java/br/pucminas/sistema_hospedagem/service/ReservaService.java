package br.pucminas.sistema_hospedagem.service;

import br.pucminas.sistema_hospedagem.dto.ReservaRequestDTO;
import br.pucminas.sistema_hospedagem.dto.ReservaResponseDTO;
import br.pucminas.sistema_hospedagem.enums.StatusReserva;
import br.pucminas.sistema_hospedagem.exception.RegraDeNegocioException;
import br.pucminas.sistema_hospedagem.exception.RecursoNaoEncontradoException;
import br.pucminas.sistema_hospedagem.model.Aluguel;
import br.pucminas.sistema_hospedagem.model.Cliente;
import br.pucminas.sistema_hospedagem.model.Quarto;
import br.pucminas.sistema_hospedagem.model.Reserva;
import br.pucminas.sistema_hospedagem.repository.AluguelRepository;
import br.pucminas.sistema_hospedagem.repository.ClienteRepository;
import br.pucminas.sistema_hospedagem.repository.QuartoRepository;
import br.pucminas.sistema_hospedagem.repository.ReservaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final ClienteRepository clienteRepository;
    private final QuartoRepository quartoRepository;
    private final AluguelRepository aluguelRepository;

    public ReservaService(ReservaRepository reservaRepository,
                          ClienteRepository clienteRepository,
                          QuartoRepository quartoRepository,
                          AluguelRepository aluguelRepository) {
        this.reservaRepository = reservaRepository;
        this.clienteRepository = clienteRepository;
        this.quartoRepository = quartoRepository;
        this.aluguelRepository = aluguelRepository;
    }

    public ReservaResponseDTO cadastrarReserva(ReservaRequestDTO reservaRequestDTO) {
        validarDatas(reservaRequestDTO);

        Cliente cliente = clienteRepository.findById(reservaRequestDTO.getClienteId())
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Cliente não encontrado com o id: " + reservaRequestDTO.getClienteId()
                ));

        Quarto quarto = quartoRepository.findById(reservaRequestDTO.getQuartoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Quarto não encontrado com o id: " + reservaRequestDTO.getQuartoId()
                ));

        validarDisponibilidadeDoQuarto(
                reservaRequestDTO.getQuartoId(),
                reservaRequestDTO.getDataEntrada(),
                reservaRequestDTO.getDataSaida()
        );

        Reserva reserva = new Reserva();
        reserva.setCliente(cliente);
        reserva.setQuarto(quarto);
        reserva.setDataEntrada(reservaRequestDTO.getDataEntrada());
        reserva.setDataSaida(reservaRequestDTO.getDataSaida());
        reserva.setStatus(StatusReserva.ATIVA);

        Reserva reservaSalva = reservaRepository.save(reserva);
        return converterParaResponseDTO(reservaSalva);
    }

    public List<ReservaResponseDTO> listarTodas() {
        return reservaRepository.findAll()
                .stream()
                .map(this::converterParaResponseDTO)
                .toList();
    }

    public ReservaResponseDTO buscarPorId(Long id) {
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Reserva não encontrada com o id: " + id));

        return converterParaResponseDTO(reserva);
    }

    public ReservaResponseDTO cancelarReserva(Long id) {
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Reserva não encontrada com o id: " + id));

        if (reserva.getStatus() == StatusReserva.CANCELADA) {
            throw new RegraDeNegocioException("A reserva já está cancelada.");
        }

        reserva.setStatus(StatusReserva.CANCELADA);
        Reserva reservaAtualizada = reservaRepository.save(reserva);

        return converterParaResponseDTO(reservaAtualizada);
    }

    private void validarDatas(ReservaRequestDTO reservaRequestDTO) {
        if (!reservaRequestDTO.getDataSaida().isAfter(reservaRequestDTO.getDataEntrada())) {
            throw new RegraDeNegocioException("A data de saída deve ser posterior à data de entrada.");
        }
    }

    private void validarDisponibilidadeDoQuarto(Long quartoId,
                                                LocalDateTime dataEntrada,
                                                LocalDateTime dataSaida) {
        List<Reserva> reservasConflitantes =
                reservaRepository.findByQuartoIdAndStatusAndDataEntradaLessThanEqualAndDataSaidaGreaterThanEqual(
                        quartoId,
                        StatusReserva.ATIVA,
                        dataSaida,
                        dataEntrada
                );

        if (!reservasConflitantes.isEmpty()) {
            throw new RegraDeNegocioException("O quarto já possui uma reserva ativa para o período informado.");
        }

        List<Aluguel> alugueisConflitantes =
                aluguelRepository.findByQuartoIdAndDataEntradaLessThanEqualAndDataSaidaGreaterThanEqual(
                        quartoId,
                        dataSaida,
                        dataEntrada
                );

        if (!alugueisConflitantes.isEmpty()) {
            throw new RegraDeNegocioException("O quarto já está alugado no período informado.");
        }
    }

    private ReservaResponseDTO converterParaResponseDTO(Reserva reserva) {
        return new ReservaResponseDTO(
                reserva.getId(),
                reserva.getCliente().getId(),
                reserva.getQuarto().getId(),
                reserva.getDataEntrada(),
                reserva.getDataSaida(),
                reserva.getStatus().name()
        );
    }
}