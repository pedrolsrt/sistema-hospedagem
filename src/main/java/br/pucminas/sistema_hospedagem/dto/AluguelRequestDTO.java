package br.pucminas.sistema_hospedagem.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class AluguelRequestDTO {

    @NotNull(message = "O id do cliente é obrigatório.")
    private Long clienteId;

    @NotNull(message = "O id do quarto é obrigatório.")
    private Long quartoId;

    @NotNull(message = "A data de entrada é obrigatória.")
    private LocalDateTime dataEntrada;

    @NotNull(message = "A data de saída é obrigatória.")
    private LocalDateTime dataSaida;

    public AluguelRequestDTO() {
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public Long getQuartoId() {
        return quartoId;
    }

    public void setQuartoId(Long quartoId) {
        this.quartoId = quartoId;
    }

    public LocalDateTime getDataEntrada() {
        return dataEntrada;
    }

    public void setDataEntrada(LocalDateTime dataEntrada) {
        this.dataEntrada = dataEntrada;
    }

    public LocalDateTime getDataSaida() {
        return dataSaida;
    }

    public void setDataSaida(LocalDateTime dataSaida) {
        this.dataSaida = dataSaida;
    }
}