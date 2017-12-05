/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controle;

import com.google.gson.Gson;
import java.io.IOException;
import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import modelo.DAOManager;
import modelo.Acao;
import modelo.Sala;

/**
 *
 * @author Pedro Alan
 */
public class Agendamento extends SocketArduino {

    private int horas;
    private int minutos;
    private String tipo;
    private DAOManager manager;
    private Sala s;
    private Gson gson;
    private Servidor servidor;

    public Agendamento(Time horario, String tipo, DAOManager manager, Sala s, Servidor servidor) {
        this.tipo = tipo;
        this.manager = manager;
        this.s = s;
        this.servidor = servidor;
        this.gson = new Gson();

        Calendar c = Calendar.getInstance();
        c.setTime(horario);
        horas = c.get(Calendar.HOUR_OF_DAY);
        minutos = c.get(Calendar.MINUTE);
        iniciar();

    }

    //@Override
    private void iniciar() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, horas);
        c.set(Calendar.MINUTE, minutos);
        c.set(Calendar.SECOND, 0);
        Date horario = c.getTime();

        Calendar cal = Calendar.getInstance();
        Date horaAtual = cal.getTime();

        //Se o horario já passou no sistema
        if (horario.before(horaAtual)) {
            //Primeira execução um dia depois
            c.add(Calendar.DATE, 1);
            horario = c.getTime();
        }

        final Timer t = new Timer();
        t.schedule(new TimerTask() {

            @Override
            public void run() {
                Acao acao1 = new Acao();
                Acao acao2 = new Acao();
                String r;
                //Muda no objeto 
                if (tipo.equals("ON")) {

                    acao1.setTipoAcao("LUZON.");
                    if (s.getnSala() == 1) {
                        r = enviarArduino("LUZON.");
                    } else {
                        r = "OK";
                    }
                    if (r.equals("OK")) {
                        s.setEstadoLuzes(true);
                        acao1.setStatus(true);
                    } else {
                        acao1.setStatus(false);
                    }

                    acao2.setTipoAcao("ARON.");
                     if (s.getnSala() == 1) {
                        r = enviarArduino("ARON.");
                    } else {
                        r = "OK";
                    }
                    if (r.equals("OK")) {
                        s.setEstadoAr(true);
                        acao2.setStatus(true);
                    } else {
                        acao2.setStatus(false);
                    }

                } else {

                    acao1.setTipoAcao("LUZOFF.");
                     if (s.getnSala() == 1) {
                        r = enviarArduino("LUZOFF.");
                    } else {
                        r = "OK";
                    }
                    if (r.equals("OK")) {
                        s.setEstadoLuzes(false);
                        acao1.setStatus(true);
                    } else {
                        acao1.setStatus(false);
                    }

                    acao2.setTipoAcao("AROFF.");
                     if (s.getnSala() == 1) {
                        r = enviarArduino("AROFF.");
                    } else {
                        r = "OK";
                    }
                    if (r.equals("OK")) {
                        s.setEstadoAr(false);
                        acao2.setStatus(true);
                    } else {
                        acao2.setStatus(false);
                    }
                }

                acao1.setIdUser(1);
                acao2.setIdUser(1);
                acao1.setnSala(s.getnSala());
                acao2.setnSala(s.getnSala());
                acao1.setLogin("SISTEMA");
                acao2.setLogin("SISTEMA");

                //Altera no Banco de dados
                if (manager.alteraSala(s) == 1 && manager.inserirAcao(acao1) == 1 && manager.inserirAcao(acao2) == 1) {
                    System.out.println("Agendamento foi um sucesso!");
                } else {
                    System.out.println("Houve uma falha!");
                }

                //Atualiza dados das salas para todos os clientes
                String salas;
                salas = gson.toJson(manager.consultarSalas());
                servidor.distribuiMsg(salas);

            }
        }, horario);
    }

    public String enviarArduino(String acao) {
        String resposta = "";
        try {
            conectarArduino();
            enviar(acao);
            resposta = ler();
            desconectarArduino();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return resposta;
    }

}
