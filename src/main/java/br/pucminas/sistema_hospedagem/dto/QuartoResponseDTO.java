package br.pucminas.sistema_hospedagem.dto;

import br.pucminas.sistema_hospedagem.enums.TipoQuarto;

public class QuartoResponseDTO {

    private Long id;
    private TipoQuarto tipo;
    private Double valorBaseDiaria;
    private Boolean possuiArCondicionado;
    private Boolean possuiHidromassagem;
    private Integer quantidadeCamasSolteiro;
    private Integer quantidadeCamasCasal;
    private Integer quantidadeCamasQueen;
    private Integer quantidadeCamasKing;
    private Integer capacidadeMaxima;
    private Integer quantidadeAmbientes;
    private Boolean permiteBerco;
    private Long residenciaId;

    public QuartoResponseDTO() {
    }

    public QuartoResponseDTO(Long id,
                             TipoQuarto tipo,
                             Double valorBaseDiaria,
                             Boolean possuiArCondicionado,
                             Boolean possuiHidromassagem,
                             Integer quantidadeCamasSolteiro,
                             Integer quantidadeCamasCasal,
                             Integer quantidadeCamasQueen,
                             Integer quantidadeCamasKing,
                             Integer capacidadeMaxima,
                             Integer quantidadeAmbientes,
                             Boolean permiteBerco,
                             Long residenciaId) {
        this.id = id;
        this.tipo = tipo;
        this.valorBaseDiaria = valorBaseDiaria;
        this.possuiArCondicionado = possuiArCondicionado;
        this.possuiHidromassagem = possuiHidromassagem;
        this.quantidadeCamasSolteiro = quantidadeCamasSolteiro;
        this.quantidadeCamasCasal = quantidadeCamasCasal;
        this.quantidadeCamasQueen = quantidadeCamasQueen;
        this.quantidadeCamasKing = quantidadeCamasKing;
        this.capacidadeMaxima = capacidadeMaxima;
        this.quantidadeAmbientes = quantidadeAmbientes;
        this.permiteBerco = permiteBerco;
        this.residenciaId = residenciaId;
    }

    public Long getId() {
        return id;
    }

    public TipoQuarto getTipo() {
        return tipo;
    }

    public Double getValorBaseDiaria() {
        return valorBaseDiaria;
    }

    public Boolean getPossuiArCondicionado() {
        return possuiArCondicionado;
    }

    public Boolean getPossuiHidromassagem() {
        return possuiHidromassagem;
    }

    public Integer getQuantidadeCamasSolteiro() {
        return quantidadeCamasSolteiro;
    }

    public Integer getQuantidadeCamasCasal() {
        return quantidadeCamasCasal;
    }

    public Integer getQuantidadeCamasQueen() {
        return quantidadeCamasQueen;
    }

    public Integer getQuantidadeCamasKing() {
        return quantidadeCamasKing;
    }

    public Integer getCapacidadeMaxima() {
        return capacidadeMaxima;
    }

    public Integer getQuantidadeAmbientes() {
        return quantidadeAmbientes;
    }

    public Boolean getPermiteBerco() {
        return permiteBerco;
    }

    public Long getResidenciaId() {
        return residenciaId;
    }
}