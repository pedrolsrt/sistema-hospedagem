package br.pucminas.sistema_hospedagem.controller;

import br.pucminas.sistema_hospedagem.dto.QuartoRequestDTO;
import br.pucminas.sistema_hospedagem.dto.QuartoResponseDTO;
import br.pucminas.sistema_hospedagem.service.QuartoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/quartos")
public class QuartoController {

    private final QuartoService quartoService;

    public QuartoController(QuartoService quartoService) {
        this.quartoService = quartoService;
    }

    @PostMapping
    public ResponseEntity<QuartoResponseDTO> cadastrarQuarto(@Valid @RequestBody QuartoRequestDTO quartoRequestDTO) {
        QuartoResponseDTO novoQuarto = quartoService.cadastrarQuarto(quartoRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoQuarto);
    }

    @GetMapping
    public ResponseEntity<List<QuartoResponseDTO>> listarTodos() {
        List<QuartoResponseDTO> quartos = quartoService.listarTodos();
        return ResponseEntity.ok(quartos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuartoResponseDTO> buscarPorId(@PathVariable Long id) {
        QuartoResponseDTO quarto = quartoService.buscarPorId(id);
        return ResponseEntity.ok(quarto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuartoResponseDTO> atualizarQuarto(@PathVariable Long id,
                                                             @Valid @RequestBody QuartoRequestDTO quartoRequestDTO) {
        QuartoResponseDTO quartoAtualizado = quartoService.atualizarQuarto(id, quartoRequestDTO);
        return ResponseEntity.ok(quartoAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirQuarto(@PathVariable Long id) {
        quartoService.excluirQuarto(id);
        return ResponseEntity.noContent().build();
    }
}