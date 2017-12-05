/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.sql.Time;

/**
 *
 * @author Pedro Alan
 */
public class Sala {
    
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
