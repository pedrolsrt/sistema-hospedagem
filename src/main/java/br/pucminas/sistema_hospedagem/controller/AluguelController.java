package br.pucminas.sistema_hospedagem.controller;

import br.pucminas.sistema_hospedagem.dto.AluguelRequestDTO;
import br.pucminas.sistema_hospedagem.dto.AluguelResponseDTO;
import br.pucminas.sistema_hospedagem.dto.ReciboResponseDTO;
import br.pucminas.sistema_hospedagem.service.AluguelService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alugueis")
public class AluguelController {

    private final AluguelService aluguelService;

    public AluguelController(AluguelService aluguelService) {
        this.aluguelService = aluguelService;
    }

    @PostMapping
    public ResponseEntity<AluguelResponseDTO> cadastrarAluguel(@Valid @RequestBody AluguelRequestDTO aluguelRequestDTO) {
        AluguelResponseDTO novoAluguel = aluguelService.cadastrarAluguel(aluguelRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoAluguel);
    }

    @GetMapping
    public ResponseEntity<List<AluguelResponseDTO>> listarTodos() {
        List<AluguelResponseDTO> alugueis = aluguelService.listarTodos();
        return ResponseEntity.ok(alugueis);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AluguelResponseDTO> buscarPorId(@PathVariable Long id) {
        AluguelResponseDTO aluguel = aluguelService.buscarPorId(id);
        return ResponseEntity.ok(aluguel);
    }

    @PatchMapping("/{id}/pagar")
    public ResponseEntity<AluguelResponseDTO> marcarPagamentoComoPago(@PathVariable Long id) {
        AluguelResponseDTO aluguelPago = aluguelService.marcarPagamentoComoPago(id);
        return ResponseEntity.ok(aluguelPago);
    }

    @GetMapping("/{id}/recibo")
    public ResponseEntity<ReciboResponseDTO> gerarRecibo(@PathVariable Long id) {
        ReciboResponseDTO recibo = aluguelService.gerarRecibo(id);
        return ResponseEntity.ok(recibo);
    }
}