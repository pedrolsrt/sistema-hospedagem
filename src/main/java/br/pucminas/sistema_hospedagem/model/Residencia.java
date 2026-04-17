package br.pucminas.sistema_hospedagem.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "residencias")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Residencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O endereço da residência é obrigatório.")
    @Size(max = 200, message = "O endereço deve ter no máximo 200 caracteres.")
    @Column(name = "endereco", nullable = false, length = 200)
    private String endereco;

    @NotBlank(message = "O número da residência é obrigatório.")
    @Size(max = 20, message = "O número deve ter no máximo 20 caracteres.")
    @Column(name = "numero", nullable = false, length = 20)
    private String numero;

    @NotBlank(message = "O bairro da residência é obrigatório.")
    @Size(max = 100, message = "O bairro deve ter no máximo 100 caracteres.")
    @Column(name = "bairro", nullable = false, length = 100)
    private String bairro;

    @NotBlank(message = "O CEP da residência é obrigatório.")
    @Size(max = 20, message = "O CEP deve ter no máximo 20 caracteres.")
    @Column(name = "cep", nullable = false, length = 20)
    private String cep;

    @NotBlank(message = "O telefone da residência é obrigatório.")
    @Size(max = 20, message = "O telefone deve ter no máximo 20 caracteres.")
    @Column(name = "telefone", nullable = false, length = 20)
    private String telefone;

    @NotBlank(message = "O e-mail da residência é obrigatório.")
    @Email(message = "O e-mail informado é inválido.")
    @Size(max = 150, message = "O e-mail deve ter no máximo 150 caracteres.")
    @Column(name = "email", nullable = false, length = 150)
    private String email;

    @OneToMany(mappedBy = "residencia", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Quarto> quartos = new ArrayList<>();
}