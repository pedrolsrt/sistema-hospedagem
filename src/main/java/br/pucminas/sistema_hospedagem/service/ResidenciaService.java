package br.pucminas.sistema_hospedagem.service;

import br.pucminas.sistema_hospedagem.dto.ResidenciaRequestDTO;
import br.pucminas.sistema_hospedagem.dto.ResidenciaResponseDTO;
import br.pucminas.sistema_hospedagem.exception.RecursoNaoEncontradoException;
import br.pucminas.sistema_hospedagem.model.Residencia;
import br.pucminas.sistema_hospedagem.repository.ResidenciaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResidenciaService {

    private final ResidenciaRepository residenciaRepository;

    public ResidenciaService(ResidenciaRepository residenciaRepository) {
        this.residenciaRepository = residenciaRepository;
    }

    public ResidenciaResponseDTO cadastrarResidencia(ResidenciaRequestDTO residenciaRequestDTO) {
        Residencia residencia = converterParaEntidade(residenciaRequestDTO);
        Residencia residenciaSalva = residenciaRepository.save(residencia);
        return converterParaResponseDTO(residenciaSalva);
    }

    public List<ResidenciaResponseDTO> listarTodas() {
        return residenciaRepository.findAll()
                .stream()
                .map(this::converterParaResponseDTO)
                .toList();
    }

    public ResidenciaResponseDTO buscarPorId(Long id) {
        Residencia residencia = residenciaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Residência não encontrada com o id: " + id));

        return converterParaResponseDTO(residencia);
    }

    public ResidenciaResponseDTO atualizarResidencia(Long id, ResidenciaRequestDTO residenciaRequestDTO) {
        Residencia residenciaExistente = residenciaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Residência não encontrada com o id: " + id));

        residenciaExistente.setEndereco(residenciaRequestDTO.getEndereco());
        residenciaExistente.setNumero(residenciaRequestDTO.getNumero());
        residenciaExistente.setBairro(residenciaRequestDTO.getBairro());
        residenciaExistente.setCep(residenciaRequestDTO.getCep());
        residenciaExistente.setTelefone(residenciaRequestDTO.getTelefone());
        residenciaExistente.setEmail(residenciaRequestDTO.getEmail());

        Residencia residenciaAtualizada = residenciaRepository.save(residenciaExistente);
        return converterParaResponseDTO(residenciaAtualizada);
    }

    public void excluirResidencia(Long id) {
        Residencia residencia = residenciaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Residência não encontrada com o id: " + id));

        residenciaRepository.delete(residencia);
    }

    private Residencia converterParaEntidade(ResidenciaRequestDTO dto) {
        Residencia residencia = new Residencia();
        residencia.setEndereco(dto.getEndereco());
        residencia.setNumero(dto.getNumero());
        residencia.setBairro(dto.getBairro());
        residencia.setCep(dto.getCep());
        residencia.setTelefone(dto.getTelefone());
        residencia.setEmail(dto.getEmail());
        return residencia;
    }

    private ResidenciaResponseDTO converterParaResponseDTO(Residencia residencia) {
        return new ResidenciaResponseDTO(
                residencia.getId(),
                residencia.getEndereco(),
                residencia.getNumero(),
                residencia.getBairro(),
                residencia.getCep(),
                residencia.getTelefone(),
                residencia.getEmail()
        );
    }
}