package br.pucminas.sistema_hospedagem.dto;

public class QuartoResponseDTO {

    private Long id;
    private String tipo;
    private Double valorBaseDiaria;
    private Boolean possuiArCondicionado;
    private Boolean possuiHidromassagem;
    private Long residenciaId;

    public QuartoResponseDTO() {
    }

    public QuartoResponseDTO(Long id, String tipo, Double valorBaseDiaria,
                             Boolean possuiArCondicionado,
                             Boolean possuiHidromassagem,
                             Long residenciaId) {
        this.id = id;
        this.tipo = tipo;
        this.valorBaseDiaria = valorBaseDiaria;
        this.possuiArCondicionado = possuiArCondicionado;
        this.possuiHidromassagem = possuiHidromassagem;
        this.residenciaId = residenciaId;
    }

    public Long getId() {
        return id;
    }

    public String getTipo() {
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

    public Long getResidenciaId() {
        return residenciaId;
    }
}