package control;

import com.google.gson.Gson;
import java.io.IOException;
import model.User;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author IFControl
 */
public class Sessao {

    private String _login;
    private String _senha;
    private TrataServidor servidor;

    public String getLogin() {
        return _login;
    }

    public String getSenha() {
        return _senha;
    }

    public void setLogin(String _login) {
        this._login = _login;
    }

    public void setSenha(String _senha) {
        this._senha = _senha;
    }

    public int iniciarSessao() {
        try {
            servidor = new TrataServidor();
            servidor.conectar();
            new Thread(servidor).start();
            servidor.setResposta(null);
            return 1;
        } catch (IOException ex) {
            System.out.println("Erro ao se conectar: " + ex);
            return 0;
        }
    }
    
    public void encerrarSessao() {
        System.out.println("ENCERRANDO SESSAO");
        try {
            servidor.desconectar();
        } catch (IOException ex) {
            System.out.println("Erro ao se desconectar: " + ex);
        }
    }

    public String login(String login, String senha) {
        servidor.enviar("{\"_login\":\"" + login
                + "\",\"_senha\":\"" + senha
                + "\"}");
        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            System.out.println("FEZES COM A THREADE");
        }
        return servidor.getResposta();
    }
    
    public String cadastrar(User user) {
        
        Gson gson = new Gson();
        String u = gson.toJson(user);
        
        servidor.enviar(u);
        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            System.out.println("FEZES COM A THREADE");
        }
        return servidor.getResposta();
    }
    
    public void trataAcao(String acao){
        
        servidor.enviar(acao);
        
    }
    
    public String verificarResposta(){
        return servidor.getResposta();
    } 
    
    public void atualizar(){
        servidor.enviar("atualizar");
    }
    
    public void logs(){
        servidor.enviar("logs");
    }

}
