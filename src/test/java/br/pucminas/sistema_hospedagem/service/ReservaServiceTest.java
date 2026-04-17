package br.pucminas.sistema_hospedagem.service;

import br.pucminas.sistema_hospedagem.dto.ReservaRequestDTO;
import br.pucminas.sistema_hospedagem.enums.StatusReserva;
import br.pucminas.sistema_hospedagem.exception.RegraDeNegocioException;
import br.pucminas.sistema_hospedagem.model.Cliente;
import br.pucminas.sistema_hospedagem.model.Quarto;
import br.pucminas.sistema_hospedagem.model.Reserva;
import br.pucminas.sistema_hospedagem.repository.ClienteRepository;
import br.pucminas.sistema_hospedagem.repository.QuartoRepository;
import br.pucminas.sistema_hospedagem.repository.ReservaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class ReservaServiceTest {

    private ReservaRepository reservaRepository;
    private ClienteRepository clienteRepository;
    private QuartoRepository quartoRepository;
    private ReservaService reservaService;

    @BeforeEach
    void setUp() {
        reservaRepository = Mockito.mock(ReservaRepository.class);
        clienteRepository = Mockito.mock(ClienteRepository.class);
        quartoRepository = Mockito.mock(QuartoRepository.class);

        reservaService = new ReservaService(reservaRepository, clienteRepository, quartoRepository);
    }

    @Test
    void deveLancarExcecaoQuandoHouverConflitoDeReservaNoMesmoPeriodo() {
        Cliente cliente = new Cliente();
        cliente.setId(1L);

        Quarto quarto = new Quarto();
        quarto.setId(1L);

        Reserva reservaExistente = new Reserva();
        reservaExistente.setId(10L);
        reservaExistente.setCliente(cliente);
        reservaExistente.setQuarto(quarto);
        reservaExistente.setDataEntrada(LocalDateTime.now().plusDays(5));
        reservaExistente.setDataSaida(LocalDateTime.now().plusDays(7));
        reservaExistente.setStatus(StatusReserva.ATIVA);

        ReservaRequestDTO reservaRequestDTO = new ReservaRequestDTO();
        reservaRequestDTO.setClienteId(1L);
        reservaRequestDTO.setQuartoId(1L);
        reservaRequestDTO.setDataEntrada(LocalDateTime.now().plusDays(6));
        reservaRequestDTO.setDataSaida(LocalDateTime.now().plusDays(8));

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(quartoRepository.findById(1L)).thenReturn(Optional.of(quarto));
        when(reservaRepository.findByQuartoIdAndStatusAndDataEntradaLessThanEqualAndDataSaidaGreaterThanEqual(
                Mockito.eq(1L),
                Mockito.eq(StatusReserva.ATIVA),
                Mockito.any(LocalDateTime.class),
                Mockito.any(LocalDateTime.class)
        )).thenReturn(List.of(reservaExistente));

        assertThrows(RegraDeNegocioException.class, () -> {
            reservaService.cadastrarReserva(reservaRequestDTO);
        });
    }
}