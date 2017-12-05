package control;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

//////////// TrATA SERVIDãO
/**
 *
 * @author Pedro Alan
 */
public class TrataServidor implements Runnable {

    private final String host = "0.0.0.0";
    ;
    private final int porta = 808;
    private Socket cliente;
    private PrintStream out;
    private Scanner in;
    private String resposta = null;

    public TrataServidor() throws IOException {
        cliente = new Socket(host, porta);
    }

    public void conectar() throws IOException {
        out = new PrintStream(cliente.getOutputStream());
        in = new Scanner(cliente.getInputStream());
    }

    public void enviar(String message) {
        out.println(message);
    }

    public void desconectar() throws IOException {
        cliente.close();
        in.close();
        out.close();
    }

    @Override
    public void run() {
        while (in.hasNextLine()) {
            resposta = in.nextLine();
            System.out.println(resposta);
        }
    }

    public String getResposta() {
        return resposta;
    }

    public void setResposta(String resposta) {
        this.resposta = resposta;
    }


}
