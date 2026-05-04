package br.pucminas.sistema_hospedagem.dto;

import br.pucminas.sistema_hospedagem.enums.TipoQuarto;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public class QuartoRequestDTO {

    @NotNull(message = "O tipo do quarto é obrigatório.")
    private TipoQuarto tipo;

    @NotNull(message = "O valor base da diária é obrigatório.")
    @Positive(message = "O valor base da diária deve ser maior que zero.")
    private Double valorBaseDiaria;

    @NotNull(message = "Informe se o quarto possui ar-condicionado.")
    private Boolean possuiArCondicionado;

    @NotNull(message = "Informe se o quarto possui hidromassagem.")
    private Boolean possuiHidromassagem;

    @NotNull(message = "A quantidade de camas de solteiro é obrigatória.")
    @PositiveOrZero(message = "A quantidade de camas de solteiro não pode ser negativa.")
    private Integer quantidadeCamasSolteiro;

    @NotNull(message = "A quantidade de camas de casal é obrigatória.")
    @PositiveOrZero(message = "A quantidade de camas de casal não pode ser negativa.")
    private Integer quantidadeCamasCasal;

    @NotNull(message = "A quantidade de camas queen é obrigatória.")
    @PositiveOrZero(message = "A quantidade de camas queen não pode ser negativa.")
    private Integer quantidadeCamasQueen;

    @NotNull(message = "A quantidade de camas king é obrigatória.")
    @PositiveOrZero(message = "A quantidade de camas king não pode ser negativa.")
    private Integer quantidadeCamasKing;

    @NotNull(message = "A capacidade máxima é obrigatória.")
    @Positive(message = "A capacidade máxima deve ser maior que zero.")
    private Integer capacidadeMaxima;

    @NotNull(message = "A quantidade de ambientes é obrigatória.")
    @Positive(message = "A quantidade de ambientes deve ser maior que zero.")
    private Integer quantidadeAmbientes;

    @NotNull(message = "Informe se o quarto permite berço.")
    private Boolean permiteBerco;

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

    public Integer getQuantidadeCamasSolteiro() {
        return quantidadeCamasSolteiro;
    }

    public void setQuantidadeCamasSolteiro(Integer quantidadeCamasSolteiro) {
        this.quantidadeCamasSolteiro = quantidadeCamasSolteiro;
    }

    public Integer getQuantidadeCamasCasal() {
        return quantidadeCamasCasal;
    }

    public void setQuantidadeCamasCasal(Integer quantidadeCamasCasal) {
        this.quantidadeCamasCasal = quantidadeCamasCasal;
    }

    public Integer getQuantidadeCamasQueen() {
        return quantidadeCamasQueen;
    }

    public void setQuantidadeCamasQueen(Integer quantidadeCamasQueen) {
        this.quantidadeCamasQueen = quantidadeCamasQueen;
    }

    public Integer getQuantidadeCamasKing() {
        return quantidadeCamasKing;
    }

    public void setQuantidadeCamasKing(Integer quantidadeCamasKing) {
        this.quantidadeCamasKing = quantidadeCamasKing;
    }

    public Integer getCapacidadeMaxima() {
        return capacidadeMaxima;
    }

    public void setCapacidadeMaxima(Integer capacidadeMaxima) {
        this.capacidadeMaxima = capacidadeMaxima;
    }

    public Integer getQuantidadeAmbientes() {
        return quantidadeAmbientes;
    }

    public void setQuantidadeAmbientes(Integer quantidadeAmbientes) {
        this.quantidadeAmbientes = quantidadeAmbientes;
    }

    public Boolean getPermiteBerco() {
        return permiteBerco;
    }

    public void setPermiteBerco(Boolean permiteBerco) {
        this.permiteBerco = permiteBerco;
    }

    public Long getResidenciaId() {
        return residenciaId;
    }

    public void setResidenciaId(Long residenciaId) {
        this.residenciaId = residenciaId;
    }
}