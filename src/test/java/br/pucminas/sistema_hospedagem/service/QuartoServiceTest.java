package br.pucminas.sistema_hospedagem.service;

import br.pucminas.sistema_hospedagem.dto.QuartoRequestDTO;
import br.pucminas.sistema_hospedagem.dto.QuartoResponseDTO;
import br.pucminas.sistema_hospedagem.enums.TipoQuarto;
import br.pucminas.sistema_hospedagem.exception.RegraDeNegocioException;
import br.pucminas.sistema_hospedagem.model.Quarto;
import br.pucminas.sistema_hospedagem.model.Residencia;
import br.pucminas.sistema_hospedagem.repository.QuartoRepository;
import br.pucminas.sistema_hospedagem.repository.ResidenciaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class QuartoServiceTest {

    private QuartoRepository quartoRepository;
    private ResidenciaRepository residenciaRepository;
    private QuartoService quartoService;
    private Residencia residencia;

    @BeforeEach
    void setUp() {
        quartoRepository = Mockito.mock(QuartoRepository.class);
        residenciaRepository = Mockito.mock(ResidenciaRepository.class);
        quartoService = new QuartoService(quartoRepository, residenciaRepository);

        residencia = new Residencia();
        residencia.setId(1L);
        residencia.setEndereco("Rua das Flores");
        residencia.setNumero("100");
        residencia.setBairro("Centro");
        residencia.setCep("30000000");
        residencia.setTelefone("31999999999");
        residencia.setEmail("residencia@email.com");
    }

    @Test
    @DisplayName("Deve impedir quarto individual com cama de casal")
    void deveImpedirQuartoIndividualComCamaDeCasal() {
        QuartoRequestDTO request = criarQuartoIndividualValido();
        request.setQuantidadeCamasSolteiro(0);
        request.setQuantidadeCamasCasal(1);
        request.setCapacidadeMaxima(1);

        assertThrows(RegraDeNegocioException.class, () -> quartoService.cadastrarQuarto(request));
    }

    @Test
    @DisplayName("Deve impedir quarto individual com cama queen")
    void deveImpedirQuartoIndividualComCamaQueen() {
        QuartoRequestDTO request = criarQuartoIndividualValido();
        request.setQuantidadeCamasSolteiro(0);
        request.setQuantidadeCamasQueen(1);
        request.setCapacidadeMaxima(1);

        assertThrows(RegraDeNegocioException.class, () -> quartoService.cadastrarQuarto(request));
    }

    @Test
    @DisplayName("Deve impedir quarto individual com cama king")
    void deveImpedirQuartoIndividualComCamaKing() {
        QuartoRequestDTO request = criarQuartoIndividualValido();
        request.setQuantidadeCamasSolteiro(0);
        request.setQuantidadeCamasKing(1);
        request.setCapacidadeMaxima(1);

        assertThrows(RegraDeNegocioException.class, () -> quartoService.cadastrarQuarto(request));
    }

    @Test
    @DisplayName("Deve validar quantidade de camas do quarto casal")
    void deveValidarQuantidadeDeCamasDoQuartoCasal() {
        QuartoRequestDTO request = criarQuartoCasalValido();
        request.setQuantidadeCamasCasal(2);

        assertThrows(RegraDeNegocioException.class, () -> quartoService.cadastrarQuarto(request));
    }

    @Test
    @DisplayName("Deve validar capacidade mínima do quarto família")
    void deveValidarCapacidadeMinimaDoQuartoFamilia() {
        QuartoRequestDTO request = criarQuartoFamiliaValido();
        request.setCapacidadeMaxima(2);

        assertThrows(RegraDeNegocioException.class, () -> quartoService.cadastrarQuarto(request));
    }

    @Test
    @DisplayName("Deve cadastrar quarto família válido")
    void deveCadastrarQuartoFamiliaValido() {
        QuartoRequestDTO request = criarQuartoFamiliaValido();

        when(residenciaRepository.findById(1L)).thenReturn(Optional.of(residencia));

        when(quartoRepository.save(Mockito.any(Quarto.class))).thenAnswer(invocation -> {
            Quarto quarto = invocation.getArgument(0);
            quarto.setId(10L);
            return quarto;
        });

        QuartoResponseDTO response = quartoService.cadastrarQuarto(request);

        assertNotNull(response);
        assertEquals(10L, response.getId());
        assertEquals(TipoQuarto.FAMILIA, response.getTipo());
        assertEquals(5, response.getCapacidadeMaxima());
        assertEquals(1L, response.getResidenciaId());
    }

    private QuartoRequestDTO criarQuartoIndividualValido() {
        QuartoRequestDTO request = criarQuartoBase(TipoQuarto.INDIVIDUAL);
        request.setValorBaseDiaria(120.0);
        request.setQuantidadeCamasSolteiro(1);
        request.setQuantidadeCamasCasal(0);
        request.setQuantidadeCamasQueen(0);
        request.setQuantidadeCamasKing(0);
        request.setCapacidadeMaxima(1);
        request.setQuantidadeAmbientes(1);
        request.setPermiteBerco(false);
        return request;
    }

    private QuartoRequestDTO criarQuartoCasalValido() {
        QuartoRequestDTO request = criarQuartoBase(TipoQuarto.CASAL);
        request.setValorBaseDiaria(180.0);
        request.setQuantidadeCamasSolteiro(0);
        request.setQuantidadeCamasCasal(1);
        request.setQuantidadeCamasQueen(0);
        request.setQuantidadeCamasKing(0);
        request.setCapacidadeMaxima(2);
        request.setQuantidadeAmbientes(1);
        request.setPermiteBerco(true);
        return request;
    }

    private QuartoRequestDTO criarQuartoFamiliaValido() {
        QuartoRequestDTO request = criarQuartoBase(TipoQuarto.FAMILIA);
        request.setValorBaseDiaria(250.0);
        request.setQuantidadeCamasSolteiro(2);
        request.setQuantidadeCamasCasal(1);
        request.setQuantidadeCamasQueen(0);
        request.setQuantidadeCamasKing(0);
        request.setCapacidadeMaxima(5);
        request.setQuantidadeAmbientes(2);
        request.setPermiteBerco(true);
        return request;
    }

    private QuartoRequestDTO criarQuartoBase(TipoQuarto tipo) {
        QuartoRequestDTO request = new QuartoRequestDTO();
        request.setTipo(tipo);
        request.setPossuiArCondicionado(false);
        request.setPossuiHidromassagem(false);
        request.setResidenciaId(1L);
        return request;
    }
}