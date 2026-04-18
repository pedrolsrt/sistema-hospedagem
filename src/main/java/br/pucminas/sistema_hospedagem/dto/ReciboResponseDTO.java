package br.pucminas.sistema_hospedagem.dto;

import java.time.LocalDateTime;

public class ReciboResponseDTO {

    private Long aluguelId;
    private String nomeCliente;
    private String enderecoResidencia;
    private Long quartoId;
    private LocalDateTime dataEntrada;
    private LocalDateTime dataSaida;
    private Integer quantidadeDiarias;
    private Double valorFinal;
    private String statusPagamento;
    private String reciboFormatado;

    public ReciboResponseDTO() {
    }

    public ReciboResponseDTO(Long aluguelId,
                             String nomeCliente,
                             String enderecoResidencia,
                             Long quartoId,
                             LocalDateTime dataEntrada,
                             LocalDateTime dataSaida,
                             Integer quantidadeDiarias,
                             Double valorFinal,
                             String statusPagamento,
                             String reciboFormatado) {
        this.aluguelId = aluguelId;
        this.nomeCliente = nomeCliente;
        this.enderecoResidencia = enderecoResidencia;
        this.quartoId = quartoId;
        this.dataEntrada = dataEntrada;
        this.dataSaida = dataSaida;
        this.quantidadeDiarias = quantidadeDiarias;
        this.valorFinal = valorFinal;
        this.statusPagamento = statusPagamento;
        this.reciboFormatado = reciboFormatado;
    }

    public Long getAluguelId() {
        return aluguelId;
    }

    public void setAluguelId(Long aluguelId) {
        this.aluguelId = aluguelId;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public String getEnderecoResidencia() {
        return enderecoResidencia;
    }

    public void setEnderecoResidencia(String enderecoResidencia) {
        this.enderecoResidencia = enderecoResidencia;
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

    public String getReciboFormatado() {
        return reciboFormatado;
    }

    public void setReciboFormatado(String reciboFormatado) {
        this.reciboFormatado = reciboFormatado;
    }
}