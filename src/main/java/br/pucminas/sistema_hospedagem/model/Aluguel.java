package br.pucminas.sistema_hospedagem.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "alugueis")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Aluguel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "O cliente do aluguel é obrigatório.")
    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @NotNull(message = "O quarto do aluguel é obrigatório.")
    @ManyToOne
    @JoinColumn(name = "quarto_id", nullable = false)
    private Quarto quarto;

    @NotNull(message = "A residência do aluguel é obrigatória.")
    @ManyToOne
    @JoinColumn(name = "residencia_id", nullable = false)
    private Residencia residencia;

    @NotNull(message = "A data de entrada é obrigatória.")
    @Column(name = "data_entrada", nullable = false)
    private LocalDateTime dataEntrada;

    @NotNull(message = "A data de saída é obrigatória.")
    @Column(name = "data_saida", nullable = false)
    private LocalDateTime dataSaida;

    @NotNull(message = "A quantidade de diárias é obrigatória.")
    @Positive(message = "A quantidade de diárias deve ser maior que zero.")
    @Column(name = "quantidade_diarias", nullable = false)
    private Integer quantidadeDiarias;

    @NotNull(message = "O número de hóspedes é obrigatório.")
    @Positive(message = "O número de hóspedes deve ser maior que zero.")
    @Column(name = "numero_hospedes", nullable = false)
    private Integer numeroHospedes;

    @NotNull(message = "Informe se foi solicitado berço.")
    @Column(name = "solicitou_berco", nullable = false)
    private Boolean solicitouBerco;

    @NotNull(message = "O valor final é obrigatório.")
    @Positive(message = "O valor final deve ser maior que zero.")
    @Column(name = "valor_final", nullable = false)
    private Double valorFinal;

    @NotNull(message = "O pagamento associado é obrigatório.")
    @OneToOne
    @JoinColumn(name = "pagamento_id", nullable = false, unique = true)
    private Pagamento pagamento;
}