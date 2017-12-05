package com.example.pealan.ifcontrol.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.example.pealan.ifcontrol.R;
import com.example.pealan.ifcontrol.controle.SocketService;
import com.example.pealan.ifcontrol.modelo.Acao;
import com.example.pealan.ifcontrol.modelo.ListaLogsAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LogsActivity extends Activity {//Cuidado com esse extend

    SocketService mBoundService;
    MyReceiver myReceiver;
    private boolean mIsBound;

    List<Acao> logs;
    Gson gson;
    ListaLogsAdapter logsAdapter;
    ListView listaLogs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logs);
        gson = new Gson();
        listaLogs = (ListView) findViewById(android.R.id.list);
    }

    @Override
    protected void onStart() {
        //Registra receptores de Broadcast com o Servico
        myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SocketService.MY_ACTION);
        registerReceiver(myReceiver, intentFilter);

        //Se liga ao servico de conexao com o servidor
        doBindService();
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(myReceiver);
        doUnbindService();
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_logs, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        if (id == android.R.id.home) {
            finish();
            overridePendingTransition(R.animator.right_slide_out, R.animator.right_slide_out_move);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.animator.right_slide_out, R.animator.right_slide_out_move);
    }

    public void pegarLogs(){
        if(mBoundService!=null) {
            //Ao iniciar activity já pede ao servidor a lista de logs
            mBoundService.sendMessage("logs");
        }
        else{
            Toast.makeText(getApplicationContext(), "Erro de Conexão", Toast.LENGTH_LONG);
            return;
        }

        //Cria o ProgressDialog
        final ProgressDialog progressDialog = ProgressDialog.show(LogsActivity.this,"","Aguardando dados...",true);

        //Mostra o Dialog
        progressDialog.show();

        //ProgressDialog dura 3 segundos
        final Handler handler =  new Handler();
        handler.postDelayed(new Runnable() {
            //Após os 3 segundos
            @Override
            public void run() {
                progressDialog.dismiss();//Retira o ProgressDialog
            }
        }, 3000);
    }

    //Codigo padrão de conexao
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBoundService = ((SocketService.LocalBinder)service).getService();
            pegarLogs();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBoundService = null;
        }
    };

    private void doBindService(){
        bindService(new Intent(LogsActivity.this, SocketService.class), mConnection, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    private void doUnbindService(){
        if(mIsBound){
            unbindService(mConnection);
            mIsBound = false;
        }
    }

    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context arg0,Intent intent){
            String resposta = intent.getStringExtra("Mensagem");
            if (resposta.contains("dataAcao")) {
                //Atualiza o Arraylist
                Type tipoLista = new TypeToken<ArrayList<Acao>>() {
                }.getType();
                logs = gson.fromJson(resposta, tipoLista);

                //Inverte para ordem de mais recente para o mais antigo
                Collections.reverse(logs);

                //Transforma o ArrayList em views de uma ListView por meio ArrayAdapter
                logsAdapter = new ListaLogsAdapter(LogsActivity.this, logs);
                listaLogs.setAdapter(logsAdapter);
            }
        }
    }
}
