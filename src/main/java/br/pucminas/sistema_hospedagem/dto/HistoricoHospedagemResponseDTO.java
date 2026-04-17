package br.pucminas.sistema_hospedagem.dto;

import java.time.LocalDateTime;

public class HistoricoHospedagemResponseDTO {

    private Long aluguelId;
    private Long clienteId;
    private String nomeCliente;
    private Long quartoId;
    private LocalDateTime dataEntrada;
    private LocalDateTime dataSaida;
    private Integer quantidadeDiarias;
    private Double valorFinal;
    private String statusPagamento;

    public HistoricoHospedagemResponseDTO() {
    }

    public HistoricoHospedagemResponseDTO(Long aluguelId,
                                          Long clienteId,
                                          String nomeCliente,
                                          Long quartoId,
                                          LocalDateTime dataEntrada,
                                          LocalDateTime dataSaida,
                                          Integer quantidadeDiarias,
                                          Double valorFinal,
                                          String statusPagamento) {
        this.aluguelId = aluguelId;
        this.clienteId = clienteId;
        this.nomeCliente = nomeCliente;
        this.quartoId = quartoId;
        this.dataEntrada = dataEntrada;
        this.dataSaida = dataSaida;
        this.quantidadeDiarias = quantidadeDiarias;
        this.valorFinal = valorFinal;
        this.statusPagamento = statusPagamento;
    }

    public Long getAluguelId() {
        return aluguelId;
    }

    public void setAluguelId(Long aluguelId) {
        this.aluguelId = aluguelId;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
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

    public Integer getQuantidadeDiarias() {
        return quantidadeDiarias;
    }

    public void setQuantidadeDiarias(Integer quantidadeDiarias) {
        this.quantidadeDiarias = quantidadeDiarias;
    }

    public Double getValorFinal() {
        return valorFinal;
    }

    public void setValorFinal(Double valorFinal) {
        this.valorFinal = valorFinal;
    }

    public String getStatusPagamento() {
        return statusPagamento;
    }

    public void setStatusPagamento(String statusPagamento) {
        this.statusPagamento = statusPagamento;
    }
}