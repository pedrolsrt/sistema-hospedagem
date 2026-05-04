package br.pucminas.sistema_hospedagem.service;

import br.pucminas.sistema_hospedagem.dto.QuartoRequestDTO;
import br.pucminas.sistema_hospedagem.dto.QuartoResponseDTO;
import br.pucminas.sistema_hospedagem.enums.TipoQuarto;
import br.pucminas.sistema_hospedagem.exception.RegraDeNegocioException;
import br.pucminas.sistema_hospedagem.exception.RecursoNaoEncontradoException;
import br.pucminas.sistema_hospedagem.model.Quarto;
import br.pucminas.sistema_hospedagem.model.Residencia;
import br.pucminas.sistema_hospedagem.repository.QuartoRepository;
import br.pucminas.sistema_hospedagem.repository.ResidenciaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuartoService {

    private final QuartoRepository quartoRepository;
    private final ResidenciaRepository residenciaRepository;

    public QuartoService(QuartoRepository quartoRepository, ResidenciaRepository residenciaRepository) {
        this.quartoRepository = quartoRepository;
        this.residenciaRepository = residenciaRepository;
    }

    public QuartoResponseDTO cadastrarQuarto(QuartoRequestDTO quartoRequestDTO) {
        validarRegrasDoTipoDeQuarto(quartoRequestDTO);

        Residencia residencia = residenciaRepository.findById(quartoRequestDTO.getResidenciaId())
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Residência não encontrada com o id: " + quartoRequestDTO.getResidenciaId()
                ));

        Quarto quarto = converterParaEntidade(quartoRequestDTO, residencia);
        Quarto quartoSalvo = quartoRepository.save(quarto);

        return converterParaResponseDTO(quartoSalvo);
    }

    public List<QuartoResponseDTO> listarTodos() {
        return quartoRepository.findAll()
                .stream()
                .map(this::converterParaResponseDTO)
                .toList();
    }

    public QuartoResponseDTO buscarPorId(Long id) {
        Quarto quarto = quartoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Quarto não encontrado com o id: " + id));

        return converterParaResponseDTO(quarto);
    }

    public QuartoResponseDTO atualizarQuarto(Long id, QuartoRequestDTO quartoRequestDTO) {
        validarRegrasDoTipoDeQuarto(quartoRequestDTO);

        Quarto quartoExistente = quartoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Quarto não encontrado com o id: " + id));

        Residencia residencia = residenciaRepository.findById(quartoRequestDTO.getResidenciaId())
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Residência não encontrada com o id: " + quartoRequestDTO.getResidenciaId()
                ));

        quartoExistente.setTipo(quartoRequestDTO.getTipo());
        quartoExistente.setValorBaseDiaria(quartoRequestDTO.getValorBaseDiaria());
        quartoExistente.setPossuiArCondicionado(quartoRequestDTO.getPossuiArCondicionado());
        quartoExistente.setPossuiHidromassagem(quartoRequestDTO.getPossuiHidromassagem());
        quartoExistente.setQuantidadeCamasSolteiro(quartoRequestDTO.getQuantidadeCamasSolteiro());
        quartoExistente.setQuantidadeCamasCasal(quartoRequestDTO.getQuantidadeCamasCasal());
        quartoExistente.setQuantidadeCamasQueen(quartoRequestDTO.getQuantidadeCamasQueen());
        quartoExistente.setQuantidadeCamasKing(quartoRequestDTO.getQuantidadeCamasKing());
        quartoExistente.setCapacidadeMaxima(quartoRequestDTO.getCapacidadeMaxima());
        quartoExistente.setQuantidadeAmbientes(quartoRequestDTO.getQuantidadeAmbientes());
        quartoExistente.setPermiteBerco(quartoRequestDTO.getPermiteBerco());
        quartoExistente.setResidencia(residencia);

        Quarto quartoAtualizado = quartoRepository.save(quartoExistente);
        return converterParaResponseDTO(quartoAtualizado);
    }

    public void excluirQuarto(Long id) {
        Quarto quarto = quartoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Quarto não encontrado com o id: " + id));

        quartoRepository.delete(quarto);
    }

    private void validarRegrasDoTipoDeQuarto(QuartoRequestDTO dto) {
        int totalCamas = dto.getQuantidadeCamasSolteiro()
                + dto.getQuantidadeCamasCasal()
                + dto.getQuantidadeCamasQueen()
                + dto.getQuantidadeCamasKing();

        if (totalCamas <= 0) {
            throw new RegraDeNegocioException("O quarto deve possuir pelo menos uma cama.");
        }

        if (dto.getTipo() == TipoQuarto.INDIVIDUAL) {
            validarQuartoIndividual(dto);
        }

        if (dto.getTipo() == TipoQuarto.CASAL) {
            validarQuartoCasal(dto);
        }

        if (dto.getTipo() == TipoQuarto.FAMILIA) {
            validarQuartoFamilia(dto);
        }
    }

    private void validarQuartoIndividual(QuartoRequestDTO dto) {
        if (Boolean.TRUE.equals(dto.getPermiteBerco())) {
            throw new RegraDeNegocioException("Quarto individual não permite berço.");
        }

        if (dto.getQuantidadeCamasCasal() > 0
                || dto.getQuantidadeCamasQueen() > 0
                || dto.getQuantidadeCamasKing() > 0) {
            throw new RegraDeNegocioException("Quarto individual deve possuir apenas camas de solteiro.");
        }

        if (dto.getCapacidadeMaxima() > dto.getQuantidadeCamasSolteiro()) {
            throw new RegraDeNegocioException("No quarto individual, a capacidade máxima deve ser proporcional ao número de camas de solteiro.");
        }
    }

    private void validarQuartoCasal(QuartoRequestDTO dto) {
        int camasDeCasal = dto.getQuantidadeCamasCasal()
                + dto.getQuantidadeCamasQueen()
                + dto.getQuantidadeCamasKing();

        if (dto.getQuantidadeCamasSolteiro() > 0) {
            throw new RegraDeNegocioException("Quarto casal não deve possuir cama de solteiro.");
        }

        if (camasDeCasal != 1) {
            throw new RegraDeNegocioException("Quarto casal deve possuir exatamente uma cama de casal, queen ou king.");
        }

        if (dto.getCapacidadeMaxima() > 2) {
            throw new RegraDeNegocioException("Quarto casal deve possuir capacidade máxima de até 2 hóspedes.");
        }
    }

    private void validarQuartoFamilia(QuartoRequestDTO dto) {
        if (dto.getCapacidadeMaxima() < 3) {
            throw new RegraDeNegocioException("Quarto família deve possuir capacidade maior, com pelo menos 3 hóspedes.");
        }

        if (dto.getQuantidadeAmbientes() < 1) {
            throw new RegraDeNegocioException("Quarto família deve possuir pelo menos um ambiente.");
        }
    }

    private Quarto converterParaEntidade(QuartoRequestDTO dto, Residencia residencia) {
        Quarto quarto = new Quarto();
        quarto.setTipo(dto.getTipo());
        quarto.setValorBaseDiaria(dto.getValorBaseDiaria());
        quarto.setPossuiArCondicionado(dto.getPossuiArCondicionado());
        quarto.setPossuiHidromassagem(dto.getPossuiHidromassagem());
        quarto.setQuantidadeCamasSolteiro(dto.getQuantidadeCamasSolteiro());
        quarto.setQuantidadeCamasCasal(dto.getQuantidadeCamasCasal());
        quarto.setQuantidadeCamasQueen(dto.getQuantidadeCamasQueen());
        quarto.setQuantidadeCamasKing(dto.getQuantidadeCamasKing());
        quarto.setCapacidadeMaxima(dto.getCapacidadeMaxima());
        quarto.setQuantidadeAmbientes(dto.getQuantidadeAmbientes());
        quarto.setPermiteBerco(dto.getPermiteBerco());
        quarto.setResidencia(residencia);
        return quarto;
    }

    private QuartoResponseDTO converterParaResponseDTO(Quarto quarto) {
        return new QuartoResponseDTO(
                quarto.getId(),
                quarto.getTipo(),
                quarto.getValorBaseDiaria(),
                quarto.getPossuiArCondicionado(),
                quarto.getPossuiHidromassagem(),
                quarto.getQuantidadeCamasSolteiro(),
                quarto.getQuantidadeCamasCasal(),
                quarto.getQuantidadeCamasQueen(),
                quarto.getQuantidadeCamasKing(),
                quarto.getCapacidadeMaxima(),
                quarto.getQuantidadeAmbientes(),
                quarto.getPermiteBerco(),
                quarto.getResidencia().getId()
        );
    }
}