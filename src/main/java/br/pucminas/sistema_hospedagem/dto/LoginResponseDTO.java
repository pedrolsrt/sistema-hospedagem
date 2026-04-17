package br.pucminas.sistema_hospedagem.dto;

public class LoginResponseDTO {

    private Long id;
    private String nome;
    private String email;
    private String mensagem;

    public LoginResponseDTO() {
    }

    public LoginResponseDTO(Long id, String nome, String email, String mensagem) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.mensagem = mensagem;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getMensagem() {
        return mensagem;
    }
}