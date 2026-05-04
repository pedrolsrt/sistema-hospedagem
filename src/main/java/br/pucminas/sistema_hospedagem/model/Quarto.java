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
import jakarta.validation.constraints.PositiveOrZero;
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

    @NotNull(message = "A quantidade de camas de solteiro é obrigatória.")
    @PositiveOrZero(message = "A quantidade de camas de solteiro não pode ser negativa.")
    @Column(name = "quantidade_camas_solteiro", nullable = false)
    private Integer quantidadeCamasSolteiro;

    @NotNull(message = "A quantidade de camas de casal é obrigatória.")
    @PositiveOrZero(message = "A quantidade de camas de casal não pode ser negativa.")
    @Column(name = "quantidade_camas_casal", nullable = false)
    private Integer quantidadeCamasCasal;

    @NotNull(message = "A quantidade de camas queen é obrigatória.")
    @PositiveOrZero(message = "A quantidade de camas queen não pode ser negativa.")
    @Column(name = "quantidade_camas_queen", nullable = false)
    private Integer quantidadeCamasQueen;

    @NotNull(message = "A quantidade de camas king é obrigatória.")
    @PositiveOrZero(message = "A quantidade de camas king não pode ser negativa.")
    @Column(name = "quantidade_camas_king", nullable = false)
    private Integer quantidadeCamasKing;

    @NotNull(message = "A capacidade máxima do quarto é obrigatória.")
    @Positive(message = "A capacidade máxima deve ser maior que zero.")
    @Column(name = "capacidade_maxima", nullable = false)
    private Integer capacidadeMaxima;

    @NotNull(message = "A quantidade de ambientes é obrigatória.")
    @Positive(message = "A quantidade de ambientes deve ser maior que zero.")
    @Column(name = "quantidade_ambientes", nullable = false)
    private Integer quantidadeAmbientes;

    @NotNull(message = "Informe se o quarto permite berço.")
    @Column(name = "permite_berco", nullable = false)
    private Boolean permiteBerco;

    @NotNull(message = "A residência do quarto é obrigatória.")
    @ManyToOne
    @JoinColumn(name = "residencia_id", nullable = false)
    private Residencia residencia;
}