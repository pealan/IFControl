package com.example.pealan.ifcontrol.modelo;

/**
 * Created by Pedro Alan on 21/06/2015.
 *
 * Classe cópia da classe User do servidor utilizada para facilitar a comunicação
 * cliente-servidor por meio de objetos JSON que são retirados dessa classeC
 */
public class User {
    private Long siap;
    private String nome;
    private String login;
    private String senha;


    public User(Long siap, String nome, String login, String senha) {
        this.siap = siap;
        this.nome = nome;
        this.login = login;
        this.senha = senha;
    }

    public User() {

    }

    public Long getSiap() {
        return siap;
    }

    public String getNome() {
        return nome;
    }

    public String getLogin() {
        return login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSiap(Long siap) {
        this.siap = siap;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
