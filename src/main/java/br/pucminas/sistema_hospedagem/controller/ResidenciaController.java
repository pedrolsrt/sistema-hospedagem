package br.pucminas.sistema_hospedagem.controller;

import br.pucminas.sistema_hospedagem.dto.HistoricoHospedagemResponseDTO;
import br.pucminas.sistema_hospedagem.dto.ResidenciaRequestDTO;
import br.pucminas.sistema_hospedagem.dto.ResidenciaResponseDTO;
import br.pucminas.sistema_hospedagem.service.ResidenciaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/residencias")
public class ResidenciaController {

    private final ResidenciaService residenciaService;

    public ResidenciaController(ResidenciaService residenciaService) {
        this.residenciaService = residenciaService;
    }

    @PostMapping
    public ResponseEntity<ResidenciaResponseDTO> cadastrarResidencia(@Valid @RequestBody ResidenciaRequestDTO residenciaRequestDTO) {
        ResidenciaResponseDTO novaResidencia = residenciaService.cadastrarResidencia(residenciaRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaResidencia);
    }

    @GetMapping
    public ResponseEntity<List<ResidenciaResponseDTO>> listarTodas() {
        List<ResidenciaResponseDTO> residencias = residenciaService.listarTodas();
        return ResponseEntity.ok(residencias);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResidenciaResponseDTO> buscarPorId(@PathVariable Long id) {
        ResidenciaResponseDTO residencia = residenciaService.buscarPorId(id);
        return ResponseEntity.ok(residencia);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResidenciaResponseDTO> atualizarResidencia(@PathVariable Long id,
                                                                     @Valid @RequestBody ResidenciaRequestDTO residenciaRequestDTO) {
        ResidenciaResponseDTO residenciaAtualizada = residenciaService.atualizarResidencia(id, residenciaRequestDTO);
        return ResponseEntity.ok(residenciaAtualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirResidencia(@PathVariable Long id) {
        residenciaService.excluirResidencia(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/historico")
    public ResponseEntity<List<HistoricoHospedagemResponseDTO>> listarHistoricoHospedagens(@PathVariable Long id) {
        List<HistoricoHospedagemResponseDTO> historico = residenciaService.listarHistoricoHospedagens(id);
        return ResponseEntity.ok(historico);
    }
}