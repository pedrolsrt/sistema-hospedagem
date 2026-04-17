package br.pucminas.sistema_hospedagem.dto;

import java.time.LocalDateTime;

public class AluguelResponseDTO {

    private Long id;
    private Long clienteId;
    private Long quartoId;
    private Long residenciaId;
    private LocalDateTime dataEntrada;
    private LocalDateTime dataSaida;
    private Integer quantidadeDiarias;
    private Double valorFinal;
    private Long pagamentoId;
    private String statusPagamento;
    private String recibo;

    public AluguelResponseDTO() {
    }

    public AluguelResponseDTO(Long id,
                              Long clienteId,
                              Long quartoId,
                              Long residenciaId,
                              LocalDateTime dataEntrada,
                              LocalDateTime dataSaida,
                              Integer quantidadeDiarias,
                              Double valorFinal,
                              Long pagamentoId,
                              String statusPagamento,
                              String recibo) {
        this.id = id;
        this.clienteId = clienteId;
        this.quartoId = quartoId;
        this.residenciaId = residenciaId;
        this.dataEntrada = dataEntrada;
        this.dataSaida = dataSaida;
        this.quantidadeDiarias = quantidadeDiarias;
        this.valorFinal = valorFinal;
        this.pagamentoId = pagamentoId;
        this.statusPagamento = statusPagamento;
        this.recibo = recibo;
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

    public Long getResidenciaId() {
        return residenciaId;
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

    public Double getValorFinal() {
        return valorFinal;
    }

    public Long getPagamentoId() {
        return pagamentoId;
    }

    public String getStatusPagamento() {
        return statusPagamento;
    }

    public String getRecibo() {
        return recibo;
    }
}