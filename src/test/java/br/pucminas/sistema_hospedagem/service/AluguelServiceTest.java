package br.pucminas.sistema_hospedagem.service;

import br.pucminas.sistema_hospedagem.dto.AluguelRequestDTO;
import br.pucminas.sistema_hospedagem.dto.AluguelResponseDTO;
import br.pucminas.sistema_hospedagem.enums.StatusPagamento;
import br.pucminas.sistema_hospedagem.exception.RegraDeNegocioException;
import br.pucminas.sistema_hospedagem.model.Aluguel;
import br.pucminas.sistema_hospedagem.model.Cliente;
import br.pucminas.sistema_hospedagem.model.Pagamento;
import br.pucminas.sistema_hospedagem.model.Quarto;
import br.pucminas.sistema_hospedagem.model.Residencia;
import br.pucminas.sistema_hospedagem.repository.AluguelRepository;
import br.pucminas.sistema_hospedagem.repository.ClienteRepository;
import br.pucminas.sistema_hospedagem.repository.PagamentoRepository;
import br.pucminas.sistema_hospedagem.repository.QuartoRepository;
import br.pucminas.sistema_hospedagem.repository.ReservaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class AluguelServiceTest {

    private AluguelRepository aluguelRepository;
    private PagamentoRepository pagamentoRepository;
    private ClienteRepository clienteRepository;
    private QuartoRepository quartoRepository;
    private ReservaRepository reservaRepository;
    private AluguelService aluguelService;

    @BeforeEach
    void setUp() {
        aluguelRepository = Mockito.mock(AluguelRepository.class);
        pagamentoRepository = Mockito.mock(PagamentoRepository.class);
        clienteRepository = Mockito.mock(ClienteRepository.class);
        quartoRepository = Mockito.mock(QuartoRepository.class);
        reservaRepository = Mockito.mock(ReservaRepository.class);

        aluguelService = new AluguelService(
                aluguelRepository,
                pagamentoRepository,
                clienteRepository,
                quartoRepository,
                reservaRepository
        );
    }

    @Test
    void deveLancarExcecaoQuandoQuartoJaEstiverOcupadoNoPeriodo() {
        Cliente cliente = new Cliente();
        cliente.setId(1L);

        Residencia residencia = new Residencia();
        residencia.setId(1L);

        Quarto quarto = new Quarto();
        quarto.setId(1L);
        quarto.setResidencia(residencia);

        Aluguel aluguelExistente = new Aluguel();
        aluguelExistente.setId(10L);
        aluguelExistente.setQuarto(quarto);
        aluguelExistente.setDataEntrada(LocalDateTime.now().plusDays(3));
        aluguelExistente.setDataSaida(LocalDateTime.now().plusDays(5));

        AluguelRequestDTO aluguelRequestDTO = new AluguelRequestDTO();
        aluguelRequestDTO.setClienteId(1L);
        aluguelRequestDTO.setQuartoId(1L);
        aluguelRequestDTO.setDataEntrada(LocalDateTime.now().plusDays(4));
        aluguelRequestDTO.setDataSaida(LocalDateTime.now().plusDays(6));

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(quartoRepository.findById(1L)).thenReturn(Optional.of(quarto));
        when(aluguelRepository.findByQuartoIdAndDataEntradaLessThanEqualAndDataSaidaGreaterThanEqual(
                Mockito.eq(1L),
                Mockito.any(LocalDateTime.class),
                Mockito.any(LocalDateTime.class)
        )).thenReturn(List.of(aluguelExistente));

        assertThrows(RegraDeNegocioException.class, () -> {
            aluguelService.cadastrarAluguel(aluguelRequestDTO);
        });
    }

    @Test
    void deveCalcularQuantidadeDeDiariasCorretamenteConsiderandoRegraDoMeioDia() {
        Cliente cliente = new Cliente();
        cliente.setId(1L);

        Residencia residencia = new Residencia();
        residencia.setId(1L);

        Quarto quarto = new Quarto();
        quarto.setId(1L);
        quarto.setResidencia(residencia);
        quarto.setValorBaseDiaria(100.0);
        quarto.setPossuiArCondicionado(false);
        quarto.setPossuiHidromassagem(false);

        Pagamento pagamentoSalvo = new Pagamento();
        pagamentoSalvo.setId(1L);
        pagamentoSalvo.setStatus(StatusPagamento.PENDENTE);
        pagamentoSalvo.setValor(100.0);

        Aluguel aluguelSalvo = new Aluguel();
        aluguelSalvo.setId(1L);
        aluguelSalvo.setCliente(cliente);
        aluguelSalvo.setQuarto(quarto);
        aluguelSalvo.setResidencia(residencia);
        aluguelSalvo.setDataEntrada(LocalDateTime.of(2026, 4, 20, 13, 0));
        aluguelSalvo.setDataSaida(LocalDateTime.of(2026, 4, 21, 13, 30));
        aluguelSalvo.setQuantidadeDiarias(2);
        aluguelSalvo.setValorFinal(200.0);
        aluguelSalvo.setPagamento(pagamentoSalvo);

        AluguelRequestDTO aluguelRequestDTO = new AluguelRequestDTO();
        aluguelRequestDTO.setClienteId(1L);
        aluguelRequestDTO.setQuartoId(1L);
        aluguelRequestDTO.setDataEntrada(LocalDateTime.of(2026, 4, 20, 13, 0));
        aluguelRequestDTO.setDataSaida(LocalDateTime.of(2026, 4, 21, 13, 30));

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(quartoRepository.findById(1L)).thenReturn(Optional.of(quarto));
        when(aluguelRepository.findByQuartoIdAndDataEntradaLessThanEqualAndDataSaidaGreaterThanEqual(
                Mockito.eq(1L),
                Mockito.any(LocalDateTime.class),
                Mockito.any(LocalDateTime.class)
        )).thenReturn(List.of());
        when(reservaRepository.findByQuartoIdAndStatusAndDataEntradaLessThanEqualAndDataSaidaGreaterThanEqual(
                Mockito.eq(1L),
                Mockito.any(),
                Mockito.any(LocalDateTime.class),
                Mockito.any(LocalDateTime.class)
        )).thenReturn(List.of());
        when(pagamentoRepository.save(Mockito.any(Pagamento.class))).thenReturn(pagamentoSalvo);
        when(aluguelRepository.save(Mockito.any(Aluguel.class))).thenReturn(aluguelSalvo);

        AluguelResponseDTO response = aluguelService.cadastrarAluguel(aluguelRequestDTO);

        assertEquals(2, response.getQuantidadeDiarias());
        assertEquals(200.0, response.getValorFinal());
    }
}