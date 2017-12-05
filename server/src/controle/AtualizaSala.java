/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controle;

import java.io.IOException;
import modelo.DAOManager;
import modelo.Sala;

/**
 *
 * @author Pedro Alan
 */
public class AtualizaSala extends SocketArduino implements Runnable {

    private DAOManager manager;
    private Servidor servidor;
    private Sala sala;

    public AtualizaSala(DAOManager manager, Servidor servidor) {
        this.manager = manager;
        this.servidor = servidor;
        sala = new Sala();
    }

    @Override
    public void run() {
        double temp;
        double umi;
        boolean presenca;
        while (true) {
            try {
                conectarArduino();
                enviar("TEMP.");
                temp = Double.parseDouble(ler());
                desconectarArduino();

                conectarArduino();
                enviar("UMIDADE.");
                umi = Double.parseDouble(ler());
                desconectarArduino();

                conectarArduino();
                enviar("PRESENCA.");
                presenca = Boolean.parseBoolean(ler());
                desconectarArduino();

                servidor.setMessage(temp + " " + umi + " " + presenca);
                manager.atualizaSala(temp, umi, presenca);
                Thread.sleep(3000);
            } catch (IOException e) {
                System.out.println(": " + e.getMessage());
            } catch (InterruptedException ex) {
                System.out.println("AtualizaSala foi interrompido: " + ex.getMessage());
            }
                
        }
    }
}
