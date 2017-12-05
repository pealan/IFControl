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
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pealan.ifcontrol.R;
import com.example.pealan.ifcontrol.controle.SocketService;
import com.example.pealan.ifcontrol.modelo.Sala;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import at.markushi.ui.CircleButton;

public class SalaActivity extends Activity {

    SocketService mBoundService;
    MyReceiver myReceiver;
    private boolean mIsBound;

    TextView txtTemperatura,txtUmidade,txtPresenca, txtTempAr;
    SeekBar skbTempAr;
    CircleButton btnLuz, btnAr;

    Sala sala;
    //List<Sala> salas;
    Gson gson;
    Type tipoLista;

    int tempEscolhida;
    String resposta = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sala);
        //getActionBar().setDisplayHomeAsUpEnabled(true);//Cuidado aqui

        //Inicia Gson e sala a partir do intent da HomeActivity
        gson = new Gson();
        sala = (Sala) getIntent().getSerializableExtra("sala");

        //Define o tipo de lista que essa activity vai receber periodicamente do servidor
        tipoLista = new TypeToken<ArrayList<Sala>>(){
        }.getType();

        //Muda título da Actionbar
        setTitle("Sala "+sala.getnSala());

        btnLuz = (CircleButton) findViewById(R.id.btnLuz);
        btnAr = (CircleButton) findViewById(R.id.btnAr);
        txtTemperatura = (TextView) findViewById(R.id.txtTemperatura);
        txtUmidade = (TextView) findViewById(R.id.txtUmidade);
        txtPresenca = (TextView) findViewById(R.id.txtPresenca);
        txtTempAr = (TextView) findViewById(R.id.txtTempAr);
        skbTempAr = (SeekBar) findViewById(R.id.skbTemperatura);

        skbTempAr.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tempEscolhida = progress;
                verificaTxtTempAr(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //tempInicial = Integer.parseInt(txtTempAr.getText().toString());
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //Envia mudanca de temperatura ao parar de mexer a SeekBar
                if (mBoundService != null) {
                    mBoundService.sendMessage("{\"tipoAcao\":\"AR" + verificaTemperatura(tempEscolhida) +
                            ".\",\"nSala\":" + sala.getnSala()+"}");
                    criaProgressDialog("Enviando dados...", 3000);
                }
            }
        });

        btnLuz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Envia mudanca no estado da luz dependo do texto do botao
                if (mBoundService != null) {
                    mBoundService.sendMessage("{\"tipoAcao\":\"LUZ" + verificaEstadoBotao(sala.isEstadoLuzes()) +
                            ".\",\"nSala\":" + sala.getnSala()+"}");
                    criaProgressDialog("Enviando dados...", 3000);
                } else {
                    Toast.makeText(getApplicationContext(), "Erro de Conexão", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnAr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Envia mudanca no estado do ar
                if (mBoundService != null) {
                    mBoundService.sendMessage("{\"tipoAcao\":\"AR" + verificaEstadoBotao(sala.isEstadoAr()) +
                            ".\",\"nSala\":" + sala.getnSala()+"}");
                    criaProgressDialog("Enviando dados...", 3000);
                } else {
                    Toast.makeText(getApplicationContext(), "Erro de Conexão", Toast.LENGTH_LONG).show();
                }
            }
        });

        atualizaInterface();
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
    protected void onDestroy() {
        doUnbindService();
        super.onDestroy();
    }

    //Muda txtTempAr referente a posicao do SeekBar
    public void verificaTxtTempAr(int p){
        switch (p){
            case 0: txtTempAr.setText("20°");
                break;
            case 1: txtTempAr.setText("21°");
                break;
            case 2: txtTempAr.setText("22°");
                break;
            case 3: txtTempAr.setText("23°");
                break;
            case 4: txtTempAr.setText("24°");
                break;
            case 5: txtTempAr.setText("25°");
                break;
        }
    }

    //Retorna posicao no SeekBar referente a temperatura do ar recebida
    public int verificaSeekBar(int tempAr ){
        switch (tempAr){
            case 20: return 0;
            case 21: return 1;
            case 22: return 2;
            case 23: return 3;
            case 24: return 4;
            case 25: return 5;
        }
        return -1;

    }

    //Retorna temperatura referente ao escolhido no SeekBar
    public int verificaTemperatura(int p){
        switch (p){
            case 0: return 20;
            case 1: return 21;
            case 2: return 22;
            case 3: return 23;
            case 4: return 24;
            case 5: return 25;
        }
        return -1;
    }

    //Se está ligado manda comando para desligar e vice-versa
    public String verificaEstadoBotao(boolean ligado){
        if (ligado)
            return "OFF";
            else return "ON";
    }

    //Atualiza Layout com valores da sala
    public void atualizaInterface(){
        if(sala.isEstadoLuzes()){
            btnLuz.setImageResource(R.drawable.lampada_on125);
        }else{
            btnLuz.setImageResource(R.drawable.lampada_off125);
        }

        if(sala.isEstadoAr()){
            btnAr.setImageResource(R.drawable.floco_on125);
            skbTempAr.setEnabled(true);
        }else{
            btnAr.setImageResource(R.drawable.floco_off125);
            skbTempAr.setEnabled(false);
        }

        txtTemperatura.setText("" + sala.getTemperatura() + "°");
        txtUmidade.setText(""+sala.getUmidade()+"%");
        if(sala.isPresenca()){
            txtPresenca.setText("Há pessoas na sala!");
        }else {
            txtPresenca.setText("Não há pessoas na sala.");
        }
        skbTempAr.setProgress(verificaSeekBar(sala.getTempAr()));
        verificaTxtTempAr(skbTempAr.getProgress());

    }

    public void criaProgressDialog(String nomeProcesso, int tempo){
        final ProgressDialog progressDialog = ProgressDialog.show(SalaActivity.this,"",nomeProcesso,true);
        progressDialog.show();
        final Handler handler =  new Handler();
        handler.postDelayed(new Runnable() {
            //Após os t segundos
            @Override
            public void run() {
                if (resposta == null){
                    Toast.makeText(SalaActivity.this,"Erro de Conexão",Toast.LENGTH_LONG).show();
                }
                progressDialog.dismiss();//Retira o ProgressDialog
            }
        }, tempo);
        resposta = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sala, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_alarme:
                Intent i = new Intent(SalaActivity.this, AlarmeActivity.class);
                i.putExtra("sala",sala);
                startActivity(i);
                overridePendingTransition(R.animator.right_slide_in, R.animator.right_slide_out);
                return true;
            case android.R.id.home:
                finish();
                overridePendingTransition(R.animator.right_slide_out, R.animator.right_slide_out_move);
                return true;
            case R.id.action_refresh:
                if (mBoundService != null) {
                    mBoundService.sendMessage("atualizar");
                    criaProgressDialog("Atualizando...", 3000);
                } else {
                    Toast.makeText(SalaActivity.this, "Erro de Conexão", Toast.LENGTH_LONG).show();
                }

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.animator.right_slide_out, R.animator.right_slide_out_move);
    }

    public void atualizar(){

    }


    //Codigo padrão de conexao
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBoundService = ((SocketService.LocalBinder)service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBoundService = null;
        }
    };

    private void doBindService(){
        bindService(new Intent(SalaActivity.this, SocketService.class), mConnection, Context.BIND_AUTO_CREATE);
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
            resposta = intent.getStringExtra("Mensagem");
            HomeActivity.salas = gson.fromJson(resposta, tipoLista);
                for (Sala s : HomeActivity.salas) {
                    if (s.getnSala() == sala.getnSala()) {
                        sala = s;
                    }
                }
            atualizaInterface();
            }
        }
}
