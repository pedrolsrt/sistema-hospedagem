package br.pucminas.sistema_hospedagem.dto;

import java.time.LocalDateTime;

public class ReciboResponseDTO {

    private Long aluguelId;
    private String nomeCliente;
    private String enderecoResidencia;
    private Long quartoId;
    private String tipoQuarto;
    private LocalDateTime dataEntrada;
    private LocalDateTime dataSaida;
    private Integer quantidadeDiarias;
    private Integer numeroHospedes;
    private Boolean solicitouBerco;
    private Double valorFinal;
    private String statusPagamento;
    private String reciboFormatado;

    public ReciboResponseDTO() {
    }

    public ReciboResponseDTO(Long aluguelId,
                             String nomeCliente,
                             String enderecoResidencia,
                             Long quartoId,
                             String tipoQuarto,
                             LocalDateTime dataEntrada,
                             LocalDateTime dataSaida,
                             Integer quantidadeDiarias,
                             Integer numeroHospedes,
                             Boolean solicitouBerco,
                             Double valorFinal,
                             String statusPagamento,
                             String reciboFormatado) {
        this.aluguelId = aluguelId;
        this.nomeCliente = nomeCliente;
        this.enderecoResidencia = enderecoResidencia;
        this.quartoId = quartoId;
        this.tipoQuarto = tipoQuarto;
        this.dataEntrada = dataEntrada;
        this.dataSaida = dataSaida;
        this.quantidadeDiarias = quantidadeDiarias;
        this.numeroHospedes = numeroHospedes;
        this.solicitouBerco = solicitouBerco;
        this.valorFinal = valorFinal;
        this.statusPagamento = statusPagamento;
        this.reciboFormatado = reciboFormatado;
    }

    public Long getAluguelId() {
        return aluguelId;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public String getEnderecoResidencia() {
        return enderecoResidencia;
    }

    public Long getQuartoId() {
        return quartoId;
    }

    public String getTipoQuarto() {
        return tipoQuarto;
    }

    public LocalDateTime getDataEntrada() {
        return dataEntrada;
    }

    public LocalDateTime getDataSaida() {
        return dataSaida;
    }

    public Integer getQuantidadeDiarias() {
        return quantidadeDiarias;
    }

    public Integer getNumeroHospedes() {
        return numeroHospedes;
    }

    public Boolean getSolicitouBerco() {
        return solicitouBerco;
    }

    public Double getValorFinal() {
        return valorFinal;
    }

    public String getStatusPagamento() {
        return statusPagamento;
    }

    public String getReciboFormatado() {
        return reciboFormatado;
    }
}