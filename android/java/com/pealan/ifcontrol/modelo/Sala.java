package com.example.pealan.ifcontrol.modelo;

import java.io.Serializable;
import java.sql.Time;

/**
 * Created by Pedro Alan on 23/06/2015.
 * Classe cópia da classe Sala do servidor utilizada para facilitar a comunicação
 * cliente-servidor por meio de objetos JSON que são retirados dessa classe
 */
public class Sala implements Serializable {

    private int nSala;
    private boolean estadoLuzes;
    private boolean estadoAr;
    private int tempAr;
    private double temperatura;
    private double umidade;
    private boolean presenca;
    private Time horaAtivacao;
    private Time horaDesativacao;

    public void setnSala(int nSala) {
        this.nSala = nSala;
    }

    public int getnSala() {
        return nSala;
    }


    public boolean isEstadoLuzes() {
        return estadoLuzes;
    }

    public void setEstadoLuzes(boolean estadoLuzes) {
        this.estadoLuzes = estadoLuzes;
    }

    public boolean isEstadoAr() {
        return estadoAr;
    }

    public void setEstadoAr(boolean estadoAr) {
        this.estadoAr = estadoAr;
    }

    public double getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(double temperatura) {
        this.temperatura = temperatura;
    }

    public double getUmidade() {
        return umidade;
    }

    public void setUmidade(double umidade) {
        this.umidade = umidade;
    }

    public boolean isPresenca() {
        return presenca;
    }

    public void setPresenca(boolean presenca) {
        this.presenca = presenca;
    }


    public int getTempAr() {
        return tempAr;
    }

    public void setTempAr(int tempAr) {
        this.tempAr = tempAr;
    }

    public Time getHoraAtivacao() {
        return horaAtivacao;
    }

    public void setHoraAtivacao(Time horaAtivacao) {
        this.horaAtivacao = horaAtivacao;
    }

    public Time getHoraDesativacao() {
        return horaDesativacao;
    }

    public void setHoraDesativacao(Time horaDesativacao) {
        this.horaDesativacao = horaDesativacao;
    }

    public void atualizaSala( double temperatura, double umidade, boolean presenca ){
        this.temperatura = temperatura;
        this.umidade = umidade;
        this.presenca = presenca;
    }
}
