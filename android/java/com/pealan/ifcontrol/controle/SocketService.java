package com.example.pealan.ifcontrol.controle;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class SocketService extends Service {
    TCPClient mTcpClient;
    public final static String MY_ACTION = "MY_ACTION";

    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }

    private final IBinder myBinder = new LocalBinder();
    public class LocalBinder extends Binder {
        public SocketService getService() {
            return SocketService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        super.onStartCommand(intent, flags, startId);
        new ConnectTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        return START_STICKY;
    }

    @Override
    public void onDestroy(){
        mTcpClient.stopClient();
        super.onDestroy();
    }

    private void sendMessageToActivity(String msg){
        Intent intent = new Intent();
        intent.setAction(MY_ACTION);
        intent.putExtra("Mensagem", msg);
        sendBroadcast(intent);
    }

    public void sendMessage(String msg){
        mTcpClient.sendMessage(msg);
    }

    public class ConnectTask extends AsyncTask<String, String, TCPClient> {
        @Override
        protected TCPClient doInBackground(String... message) {
            //criar cliente TCP
            mTcpClient = new TCPClient(new TCPClient.OnMessageReceived() {
                @Override
                public void messageReceived(String message) {
                    publishProgress(message);
                    Log.i("message", "messageReceived: " + message);
                }
            });
            mTcpClient.run();

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            Log.i("onProgressUpdate", values[0]);
            sendMessageToActivity(values[0]);
        }
    }
}
