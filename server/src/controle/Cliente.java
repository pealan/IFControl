/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controle;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author Pedro Alan
 */
public class Cliente {
    public static void main(String[] args) throws IOException {
    
        new Cliente("0.0.0.0", 808).executa();
    
    }

    private String host;
    private int porta;

    public Cliente(String host, int porta) {
        this.host = host;
        this.porta = porta;
    }

    public void executa() throws IOException {

        try (Socket cliente = new Socket(this.host, this.porta)) {
            System.out.println("O Cliente se conectou ao servidor!");

            new Thread(new Recebedor(cliente.getInputStream())).start();

            try (Scanner in = new Scanner(System.in);
                    PrintStream out = new PrintStream(cliente.getOutputStream())) {
                while (in.hasNextLine()) {
                    out.println(in.nextLine());
                }

            }
        }

    }

}
    
