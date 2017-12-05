/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controle;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 *
 * @author Pedro Alan
 */
public class SocketArduino {
    
    protected Socket arduino;
    protected DataOutputStream out;
    protected BufferedReader input;
    
    public void conectarArduino() throws IOException {
        
        arduino = new Socket("192.168.0.101.",80);
        out = new DataOutputStream(arduino.getOutputStream());
        input = new BufferedReader(new InputStreamReader(arduino.getInputStream()));
        
    }
    
    public void enviar(String req) throws IOException{
        out.writeBytes(req);
    }
    
    public String ler() throws IOException{
        String str = input.readLine();
        return str;
    }
    
    public void desconectarArduino() throws IOException{
        out.close();
        input.close();
        arduino.close();
    }
}
