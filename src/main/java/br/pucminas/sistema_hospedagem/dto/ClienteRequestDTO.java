package br.pucminas.sistema_hospedagem.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ClienteRequestDTO {

    @NotBlank(message = "O nome do cliente é obrigatório.")
    @Size(max = 150, message = "O nome do cliente deve ter no máximo 150 caracteres.")
    private String nome;

    @NotBlank(message = "O CPF do cliente é obrigatório.")
    @Pattern(regexp = "\\d{11}", message = "O CPF deve conter exatamente 11 dígitos numéricos.")
    private String cpf;

    @NotBlank(message = "O endereço do cliente é obrigatório.")
    @Size(max = 200, message = "O endereço deve ter no máximo 200 caracteres.")
    private String endereco;

    @NotBlank(message = "O telefone do cliente é obrigatório.")
    @Size(max = 20, message = "O telefone deve ter no máximo 20 caracteres.")
    private String telefone;

    @NotBlank(message = "O e-mail do cliente é obrigatório.")
    @Email(message = "O e-mail informado é inválido.")
    @Size(max = 150, message = "O e-mail deve ter no máximo 150 caracteres.")
    private String email;

    @NotBlank(message = "A senha do cliente é obrigatória.")
    @Size(min = 6, max = 100, message = "A senha deve ter entre 6 e 100 caracteres.")
    private String senha;

    public ClienteRequestDTO() {
    }

    public ClienteRequestDTO(String nome, String cpf, String endereco, String telefone, String email, String senha) {
        this.nome = nome;
        this.cpf = cpf;
        this.endereco = endereco;
        this.telefone = telefone;
        this.email = email;
        this.senha = senha;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }


    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
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
 
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}