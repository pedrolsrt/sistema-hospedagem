package br.pucminas.sistema_hospedagem.model;

import br.pucminas.sistema_hospedagem.enums.TipoQuarto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "quartos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Quarto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "O tipo do quarto é obrigatório.")
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false, length = 20)
    private TipoQuarto tipo;

    @NotNull(message = "O valor base da diária é obrigatório.")
    @Positive(message = "O valor base da diária deve ser maior que zero.")
    @Column(name = "valor_base_diaria", nullable = false)
    private Double valorBaseDiaria;

    @NotNull(message = "A informação de ar-condicionado é obrigatória.")
    @Column(name = "possui_ar_condicionado", nullable = false)
    private Boolean possuiArCondicionado;

    @NotNull(message = "A informação de hidromassagem é obrigatória.")
    @Column(name = "possui_hidromassagem", nullable = false)
    private Boolean possuiHidromassagem;

    @NotNull(message = "A residência do quarto é obrigatória.")
    @ManyToOne
    @JoinColumn(name = "residencia_id", nullable = false)
    private Residencia residencia;
}