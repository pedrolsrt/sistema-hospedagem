package br.pucminas.sistema_hospedagem.controller;

import br.pucminas.sistema_hospedagem.dto.ReservaRequestDTO;
import br.pucminas.sistema_hospedagem.dto.ReservaResponseDTO;
import br.pucminas.sistema_hospedagem.service.ReservaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservas")
public class ReservaController {

    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @PostMapping
    public ResponseEntity<ReservaResponseDTO> cadastrarReserva(@Valid @RequestBody ReservaRequestDTO reservaRequestDTO) {
        ReservaResponseDTO novaReserva = reservaService.cadastrarReserva(reservaRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaReserva);
    }

    @GetMapping
    public ResponseEntity<List<ReservaResponseDTO>> listarTodas() {
        List<ReservaResponseDTO> reservas = reservaService.listarTodas();
        return ResponseEntity.ok(reservas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservaResponseDTO> buscarPorId(@PathVariable Long id) {
        ReservaResponseDTO reserva = reservaService.buscarPorId(id);
        return ResponseEntity.ok(reserva);
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<ReservaResponseDTO> cancelarReserva(@PathVariable Long id) {
        ReservaResponseDTO reservaCancelada = reservaService.cancelarReserva(id);
        return ResponseEntity.ok(reservaCancelada);
    }
}