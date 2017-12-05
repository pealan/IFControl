package com.example.pealan.ifcontrol.modelo;

import java.sql.Time;
import java.util.Calendar;

/**
 * Created by Pedro Alan on 24/06/2015.
 */
public class Acao {
    private Calendar dataAcao;
    private Time horaAcao;
    private String tipoAcao;
    private int idUser;
    private int nSala;
    private boolean status;
    private int idAcao;
    private String login;

    public Acao(){
    }




    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }


    public int getIdAcao() {
        return idAcao;
    }

    public void setIdAcao(int idAcao) {
        this.idAcao = idAcao;
    }

    public Calendar getDataAcao() {
        return dataAcao;
    }

    public void setDataAcao(Calendar dataAcao) {
        this.dataAcao = dataAcao;
    }

    public Time getHoraAcao() {
        return horaAcao;
    }

    public void setHoraAcao(Time horaAcao) {
        this.horaAcao = horaAcao;
    }

    public String getTipoAcao() {
        return tipoAcao;
    }

    public void setTipoAcao(String tipoAcao) {
        this.tipoAcao = tipoAcao;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public int getnSala() {
        return nSala;
    }

    public void setnSala(int nSala) {
        this.nSala = nSala;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
