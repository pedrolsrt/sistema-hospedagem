package br.pucminas.sistema_hospedagem.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ResidenciaRequestDTO {

    @NotBlank(message = "O endereço da residência é obrigatório.")
    @Size(max = 200, message = "O endereço deve ter no máximo 200 caracteres.")
    private String endereco;

    @NotBlank(message = "O número da residência é obrigatório.")
    @Size(max = 20, message = "O número deve ter no máximo 20 caracteres.")
    private String numero;

    @NotBlank(message = "O bairro da residência é obrigatório.")
    @Size(max = 100, message = "O bairro deve ter no máximo 100 caracteres.")
    private String bairro;

    @NotBlank(message = "O CEP da residência é obrigatório.")
    @Pattern(
            regexp = "^\\d{8}$",
            message = "O CEP deve conter exatamente 8 dígitos numéricos."
    )
    private String cep;

    @NotBlank(message = "O telefone da residência é obrigatório.")
    @Pattern(
            regexp = "^\\d{10,11}$",
            message = "O telefone deve conter 10 ou 11 dígitos numéricos."
    )
    private String telefone;

    @NotBlank(message = "O e-mail da residência é obrigatório.")
    @Email(message = "O e-mail informado é inválido.")
    @Size(max = 150, message = "O e-mail deve ter no máximo 150 caracteres.")
    private String email;

    public ResidenciaRequestDTO() {
    }

    public ResidenciaRequestDTO(String endereco, String numero, String bairro, String cep, String telefone, String email) {
        this.endereco = endereco;
        this.numero = numero;
        this.bairro = bairro;
        this.cep = cep;
        this.telefone = telefone;
        this.email = email;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}