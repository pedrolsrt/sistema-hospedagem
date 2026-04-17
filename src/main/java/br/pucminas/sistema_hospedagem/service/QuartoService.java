package br.pucminas.sistema_hospedagem.service;

import br.pucminas.sistema_hospedagem.dto.QuartoRequestDTO;
import br.pucminas.sistema_hospedagem.dto.QuartoResponseDTO;
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
        Residencia residencia = residenciaRepository.findById(quartoRequestDTO.getResidenciaId())
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Residência não encontrada com o id: " + quartoRequestDTO.getResidenciaId()
                ));

        Quarto quarto = new Quarto();
        quarto.setTipo(quartoRequestDTO.getTipo());
        quarto.setValorBaseDiaria(quartoRequestDTO.getValorBaseDiaria());
        quarto.setPossuiArCondicionado(quartoRequestDTO.getPossuiArCondicionado());
        quarto.setPossuiHidromassagem(quartoRequestDTO.getPossuiHidromassagem());
        quarto.setResidencia(residencia);

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
        quartoExistente.setResidencia(residencia);

        Quarto quartoAtualizado = quartoRepository.save(quartoExistente);
        return converterParaResponseDTO(quartoAtualizado);
    }

    public void excluirQuarto(Long id) {
        Quarto quarto = quartoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Quarto não encontrado com o id: " + id));

        quartoRepository.delete(quarto);
    }

    private QuartoResponseDTO converterParaResponseDTO(Quarto quarto) {
        return new QuartoResponseDTO(
                quarto.getId(),
                quarto.getTipo().name(),
                quarto.getValorBaseDiaria(),
                quarto.getPossuiArCondicionado(),
                quarto.getPossuiHidromassagem(),
                quarto.getResidencia().getId()
        );
    }
}