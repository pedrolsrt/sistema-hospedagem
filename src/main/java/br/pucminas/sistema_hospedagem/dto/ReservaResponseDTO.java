package br.pucminas.sistema_hospedagem.dto;

import java.time.LocalDateTime;

public class ReservaResponseDTO {

    private Long id;
    private Long clienteId;
    private Long quartoId;
    private LocalDateTime dataEntrada;
    private LocalDateTime dataSaida;
    private String status;

    public ReservaResponseDTO() {
    }

    public ReservaResponseDTO(Long id, Long clienteId, Long quartoId,
                              LocalDateTime dataEntrada, LocalDateTime dataSaida,
                              String status) {
        this.id = id;
        this.clienteId = clienteId;
        this.quartoId = quartoId;
        this.dataEntrada = dataEntrada;
        this.dataSaida = dataSaida;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public Long getQuartoId() {
        return quartoId;
    }

    public LocalDateTime getDataEntrada() {
        return dataEntrada;
    }

    public LocalDateTime getDataSaida() {
        return dataSaida;
    }

    public String getStatus() {
        return status;
    }
}