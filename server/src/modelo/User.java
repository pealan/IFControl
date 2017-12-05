/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

/**
 *
 * @author Pedro Alan
 */
public class User {
    
    private int idUser;
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
    
    public User(){
        
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
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
