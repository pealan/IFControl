package com.example.pealan.ifcontrol.controle;


import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by Pedro Alan on 29/05/2015.
 */
public class TCPClient {

    public static final String SERVER_IP = "192.168.0.100";
    public static final int SERVER_PORT = 808;
    //Envia mensagem para o servidor
    private String mServerMessage;
    //Envia notificações de 'Mensagens recebidas'
    private OnMessageReceived mMessageListener = null;
    //Enquanto true, server continua rodando
    private boolean mRun = false;
    //Usado para enviar mensagens
    private PrintWriter mBufferOut;
    //Usado para receber mensagens
    private BufferedReader mBufferIn;

    public TCPClient(OnMessageReceived listener) {
        mMessageListener = listener;
    }

    public void sendMessage(String message){
        if (mBufferOut != null && !mBufferOut.checkError()){
            mBufferOut.println(message);
            mBufferOut.flush();
        }
    }

    public void stopClient(){
        Log.i("Debug", "stopClient");

        mRun = false;

        if(mBufferOut!=null){
            mBufferOut.flush();
            mBufferOut.close();
        }
        mMessageListener = null;
        mBufferIn = null;
        mBufferOut = null;
        mServerMessage = null;
    }

    public void run(){
        mRun = true;

        try{
            InetAddress serveAddr = InetAddress.getByName(SERVER_IP);
            Log.e("TCP Client", "C: Connecting...");

            Socket socket = new Socket(serveAddr,SERVER_PORT);
            try{
                Log.i("Debug", "inside try catch");
                mBufferOut = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                mBufferIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                while(mRun){
                    mServerMessage = mBufferIn.readLine();
                    if(mServerMessage!=null && mMessageListener!= null){
                        mMessageListener.messageReceived(mServerMessage);
                    }
                }
                Log.e("RESPONSE FROM SERVER", "S: Received Message: '" + mServerMessage + "'");
            }catch (Exception e) {

                Log.e("TCP", "S: Error", e);
            } finally {
                socket.close();
            }
        }catch (Exception e) {
            Log.e("TCP", "C: Error", e);
        }
    }

    public interface OnMessageReceived {
        public void messageReceived(String message);
    }
}
