package br.pucminas.sistema_hospedagem.dto;

import br.pucminas.sistema_hospedagem.enums.TipoQuarto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class QuartoRequestDTO {

    @NotNull(message = "O tipo do quarto é obrigatório.")
    private TipoQuarto tipo;

    @NotNull(message = "O valor base da diária é obrigatório.")
    @Positive(message = "O valor da diária deve ser maior que zero.")
    private Double valorBaseDiaria;

    @NotNull(message = "Informe se possui ar-condicionado.")
    private Boolean possuiArCondicionado;

    @NotNull(message = "Informe se possui hidromassagem.")
    private Boolean possuiHidromassagem;

    @NotNull(message = "O id da residência é obrigatório.")
    private Long residenciaId;

    public QuartoRequestDTO() {
    }

    public TipoQuarto getTipo() {
        return tipo;
    }

    public void setTipo(TipoQuarto tipo) {
        this.tipo = tipo;
    }

    public Double getValorBaseDiaria() {
        return valorBaseDiaria;
    }

    public void setValorBaseDiaria(Double valorBaseDiaria) {
        this.valorBaseDiaria = valorBaseDiaria;
    }

    public Boolean getPossuiArCondicionado() {
        return possuiArCondicionado;
    }

    public void setPossuiArCondicionado(Boolean possuiArCondicionado) {
        this.possuiArCondicionado = possuiArCondicionado;
    }

    public Boolean getPossuiHidromassagem() {
        return possuiHidromassagem;
    }

    public void setPossuiHidromassagem(Boolean possuiHidromassagem) {
        this.possuiHidromassagem = possuiHidromassagem;
    }

    public Long getResidenciaId() {
        return residenciaId;
    }

    public void setResidenciaId(Long residenciaId) {
        this.residenciaId = residenciaId;
    }
}