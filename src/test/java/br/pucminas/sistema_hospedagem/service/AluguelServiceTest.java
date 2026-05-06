package br.pucminas.sistema_hospedagem.service;

import br.pucminas.sistema_hospedagem.dto.AluguelRequestDTO;
import br.pucminas.sistema_hospedagem.dto.AluguelResponseDTO;
import br.pucminas.sistema_hospedagem.enums.StatusPagamento;
import br.pucminas.sistema_hospedagem.enums.StatusReserva;
import br.pucminas.sistema_hospedagem.enums.TipoQuarto;
import br.pucminas.sistema_hospedagem.exception.RegraDeNegocioException;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class AluguelServiceTest {

    private AluguelRepository aluguelRepository;
    private PagamentoRepository pagamentoRepository;
    private ClienteRepository clienteRepository;
    private QuartoRepository quartoRepository;
    private ReservaRepository reservaRepository;
    private AluguelService aluguelService;

    private Cliente cliente;
    private Residencia residencia;
    private Quarto quartoFamilia;

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

        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("Pedro Lucas");

        residencia = new Residencia();
        residencia.setId(1L);
        residencia.setEndereco("Rua das Flores");
        residencia.setNumero("100");
        residencia.setBairro("Centro");
        residencia.setCep("30000000");
        residencia.setTelefone("31999999999");
        residencia.setEmail("residencia@email.com");

        quartoFamilia = new Quarto();
        quartoFamilia.setId(1L);
        quartoFamilia.setTipo(TipoQuarto.FAMILIA);
        quartoFamilia.setValorBaseDiaria(200.0);
        quartoFamilia.setPossuiArCondicionado(true);
        quartoFamilia.setPossuiHidromassagem(true);
        quartoFamilia.setQuantidadeCamasSolteiro(2);
        quartoFamilia.setQuantidadeCamasCasal(1);
        quartoFamilia.setQuantidadeCamasQueen(1);
        quartoFamilia.setQuantidadeCamasKing(0);
        quartoFamilia.setCapacidadeMaxima(5);
        quartoFamilia.setQuantidadeAmbientes(3);
        quartoFamilia.setPermiteBerco(true);
        quartoFamilia.setResidencia(residencia);
    }

    @Test
    @DisplayName("Deve validar capacidade máxima de hóspedes")
    void deveValidarCapacidadeMaximaDeHospedes() {
        AluguelRequestDTO request = criarAluguelValido();
        request.setNumeroHospedes(6);

        prepararBuscaClienteEQuarto();

        assertThrows(
                RegraDeNegocioException.class,
                () -> aluguelService.cadastrarAluguel(request)
        );
    }

    @Test
    @DisplayName("Deve validar solicitação de berço")
    void deveValidarSolicitacaoDeBerco() {
        AluguelRequestDTO request = criarAluguelValido();
        request.setSolicitaBerco(true);
        quartoFamilia.setPermiteBerco(false);

        prepararBuscaClienteEQuarto();

        assertThrows(
                RegraDeNegocioException.class,
                () -> aluguelService.cadastrarAluguel(request)
        );
    }

    @Test
    @DisplayName("Deve calcular diárias corretamente")
    void deveCalcularDiariasCorretamente() {
        AluguelRequestDTO request = criarAluguelValido();
        request.setDataEntrada(LocalDateTime.of(2026, 5, 1, 13, 0));
        request.setDataSaida(LocalDateTime.of(2026, 5, 5, 10, 0));

        prepararCenarioValido();

        AluguelResponseDTO response = aluguelService.cadastrarAluguel(request);

        assertEquals(4, response.getQuantidadeDiarias());
    }

    @Test
    @DisplayName("Deve aplicar regra do meio-dia")
    void deveAplicarRegraDoMeioDia() {
        AluguelRequestDTO request = criarAluguelValido();
        request.setDataEntrada(LocalDateTime.of(2026, 5, 1, 13, 0));
        request.setDataSaida(LocalDateTime.of(2026, 5, 5, 13, 0));

        prepararCenarioValido();

        AluguelResponseDTO response = aluguelService.cadastrarAluguel(request);

        assertEquals(5, response.getQuantidadeDiarias());
    }

    @Test
    @DisplayName("Deve calcular valor final")
    void deveCalcularValorFinal() {
        AluguelRequestDTO request = criarAluguelValido();

        prepararCenarioValido();

        AluguelResponseDTO response = aluguelService.cadastrarAluguel(request);

        assertNotNull(response.getValorFinal());
        assertTrue(response.getValorFinal() > 0);
    }

    @Test
    @DisplayName("Deve aplicar desconto progressivo para quarto família")
    void deveAplicarDescontoProgressivoParaQuartoFamilia() {
        AluguelRequestDTO request = criarAluguelValido();
        request.setNumeroHospedes(5);

        prepararCenarioValido();

        AluguelResponseDTO response = aluguelService.cadastrarAluguel(request);

        assertEquals(5, response.getNumeroHospedes());
        assertEquals(1782.0, response.getValorFinal(), 0.01);
    }

    @Test
    @DisplayName("Deve impedir reserva conflitante")
    void deveImpedirReservaConflitante() {
        AluguelRequestDTO request = criarAluguelValido();

        Reserva reservaConflitante = new Reserva();
        reservaConflitante.setId(1L);
        reservaConflitante.setStatus(StatusReserva.ATIVA);

        prepararBuscaClienteEQuarto();

        when(aluguelRepository.findByQuartoIdAndDataEntradaLessThanEqualAndDataSaidaGreaterThanEqual(
                Mockito.eq(1L),
                Mockito.any(LocalDateTime.class),
                Mockito.any(LocalDateTime.class)
        )).thenReturn(List.of());

        when(reservaRepository.findByQuartoIdAndStatusAndDataEntradaLessThanEqualAndDataSaidaGreaterThanEqual(
                Mockito.eq(1L),
                Mockito.eq(StatusReserva.ATIVA),
                Mockito.any(LocalDateTime.class),
                Mockito.any(LocalDateTime.class)
        )).thenReturn(List.of(reservaConflitante));

        assertThrows(
                RegraDeNegocioException.class,
                () -> aluguelService.cadastrarAluguel(request)
        );
    }

    @Test
    @DisplayName("Deve impedir quarto ocupado")
    void deveImpedirQuartoOcupado() {
        AluguelRequestDTO request = criarAluguelValido();

        Aluguel aluguelConflitante = new Aluguel();
        aluguelConflitante.setId(2L);

        prepararBuscaClienteEQuarto();

        when(aluguelRepository.findByQuartoIdAndDataEntradaLessThanEqualAndDataSaidaGreaterThanEqual(
                Mockito.eq(1L),
                Mockito.any(LocalDateTime.class),
                Mockito.any(LocalDateTime.class)
        )).thenReturn(List.of(aluguelConflitante));

        assertThrows(
                RegraDeNegocioException.class,
                () -> aluguelService.cadastrarAluguel(request)
        );
    }

    @Test
    @DisplayName("Deve associar pagamento ao aluguel")
    void deveAssociarPagamentoAoAluguel() {
        AluguelRequestDTO request = criarAluguelValido();

        prepararCenarioValido();

        AluguelResponseDTO response = aluguelService.cadastrarAluguel(request);

        assertNotNull(response.getPagamentoId());
        assertEquals(StatusPagamento.PENDENTE.name(), response.getStatusPagamento());
    }

    private AluguelRequestDTO criarAluguelValido() {
        AluguelRequestDTO request = new AluguelRequestDTO();
        request.setClienteId(1L);
        request.setQuartoId(1L);
        request.setDataEntrada(LocalDateTime.of(2026, 5, 1, 13, 0));
        request.setDataSaida(LocalDateTime.of(2026, 5, 5, 10, 0));
        request.setNumeroHospedes(4);
        request.setSolicitaBerco(false);
        return request;
    }

    private void prepararBuscaClienteEQuarto() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(quartoRepository.findById(1L)).thenReturn(Optional.of(quartoFamilia));
    }

    private void prepararCenarioValido() {
        prepararBuscaClienteEQuarto();

        when(aluguelRepository.findByQuartoIdAndDataEntradaLessThanEqualAndDataSaidaGreaterThanEqual(
                Mockito.eq(1L),
                Mockito.any(LocalDateTime.class),
                Mockito.any(LocalDateTime.class)
        )).thenReturn(List.of());

        when(reservaRepository.findByQuartoIdAndStatusAndDataEntradaLessThanEqualAndDataSaidaGreaterThanEqual(
                Mockito.eq(1L),
                Mockito.eq(StatusReserva.ATIVA),
                Mockito.any(LocalDateTime.class),
                Mockito.any(LocalDateTime.class)
        )).thenReturn(List.of());

        when(pagamentoRepository.save(Mockito.any(Pagamento.class))).thenAnswer(invocation -> {
            Pagamento pagamento = invocation.getArgument(0);
            pagamento.setId(1L);
            return pagamento;
        });

        when(aluguelRepository.save(Mockito.any(Aluguel.class))).thenAnswer(invocation -> {
            Aluguel aluguel = invocation.getArgument(0);
            aluguel.setId(1L);
            return aluguel;
        });
    }
}