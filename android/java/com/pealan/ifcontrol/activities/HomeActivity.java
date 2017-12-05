package com.example.pealan.ifcontrol.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.IBinder;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
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

public class HomeActivity extends Activity {

    SocketService mBoundService;
    MyReceiver myReceiver;
    private boolean mIsBound;

    AlertDialog.Builder alertDialog;
    ImageView imgLuz1,imgAr1,imgPresenca1,imgLuz2,imgAr2,imgPresenca2,imgLuz3,imgAr3,imgPresenca3;
    ImageButton btnHorario1,btnHorario2,btnHorario3;
    CardView cdvSala1,cdvSala2,cdvSala3;

    protected static List<Sala> salas;
    Gson gson;
    static String login,senha;
    String respostaServer;
    boolean retornando;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //Variavel que impede que o serviço seja destruido caso o usuario só queira retornar a tela de login
        retornando = false;

        TextView txtSaudacao = (TextView) findViewById(R.id.txtSaudacao);
        //Muda de acordo com o usuario
        login = this.getIntent().getStringExtra("login");
        txtSaudacao.setText("Seja bem vindo, " + login + "!");
        senha = this.getIntent().getStringExtra("senha");

        //Dados da sala 1
        imgLuz1 = (ImageView) findViewById(R.id.imgLuz1);
        imgAr1 = (ImageView) findViewById(R.id.imgAr1);
        imgPresenca1 = (ImageView) findViewById(R.id.imgPresenca1);

        //Dados da sala 2
        imgLuz2 = (ImageView) findViewById(R.id.imgLuz2);
        imgAr2 = (ImageView) findViewById(R.id.imgAr2);
        imgPresenca2 = (ImageView) findViewById(R.id.imgPresenca2);

        //Dados da sala 3
        imgLuz3 = (ImageView) findViewById(R.id.imgLuz3);
        imgAr3 = (ImageView) findViewById(R.id.imgAr3);
        imgPresenca3 = (ImageView) findViewById(R.id.imgPresenca3);

        salas = new ArrayList<Sala>();
        gson = new Gson();

        //Botoes
        btnHorario1 = (ImageButton) findViewById(R.id.btnHorario1);
        btnHorario2 = (ImageButton) findViewById(R.id.btnHorario2);
        btnHorario3 = (ImageButton) findViewById(R.id.btnHorario3);
        cdvSala1 = (CardView) findViewById(R.id.sala1);
        cdvSala2 = (CardView) findViewById(R.id.sala2);
        cdvSala3 = (CardView) findViewById(R.id.sala3);


        cdvSala1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irProximaActivity(0,SalaActivity.class);
            }
        });

        cdvSala2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irProximaActivity(1, SalaActivity.class);
            }
        });

        cdvSala3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irProximaActivity(2, SalaActivity.class);
            }
        });

        btnHorario1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irProximaActivity(0, AlarmeActivity.class);
            }
        });

        btnHorario2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irProximaActivity(1, AlarmeActivity.class);
            }
        });

        btnHorario3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                irProximaActivity(2, AlarmeActivity.class);
            }
        });

        //Criar tela de aviso
        alertDialog = new AlertDialog.Builder(HomeActivity.this);

        //Caso o usuario aperte "Sim" o servico é reiniciado, caso aperte "Não" o serviço é destruido e o aplicativo fechado
        alertDialog.setTitle("Logout")
                .setMessage("Sair para a tela de login?")
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        retornando = true;
                        startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                        finish();
                    }
                })
                .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Do something
                    }
                });
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
        //Destroi serviço
        doUnbindService();
        if (!retornando) {
            stopService(new Intent(HomeActivity.this, SocketService.class));
        }
        super.onDestroy();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    public void atualizar(int tipo){
        if(mBoundService!=null) {
            mBoundService.sendMessage("atualizar");

            //Caso em que o usuario pressiona o botao de Atualizar
            if(tipo == 1) {
                //Cria o ProgressDialog
                final ProgressDialog progressDialog = ProgressDialog.show(HomeActivity.this, "", "Aguardando dados...", true);

                //Mostra o Dialog
                progressDialog.show();

                //ProgressDialog dura 2 segundos
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    //Após os 3 segundos
                    @Override
                    public void run() {
                        progressDialog.dismiss();//Retira o ProgressDialog
                        if (respostaServer == null) {
                            Toast.makeText(HomeActivity.this, "Erro de Conexão", Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(HomeActivity.this, "Dados Atualizados", Toast.LENGTH_LONG).show();
                        }
                    }
                }, 3000);
            }
            //Atualizacao automatica do app
            else{
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    //Após os 3 segundos
                    @Override
                    public void run() {
                        if(respostaServer== null) {
                            Toast.makeText(HomeActivity.this, "Erro de Conexão", Toast.LENGTH_LONG).show();

                            //Destroi serviço
                            stopService(new Intent(HomeActivity.this, SocketService.class));

                            //Reinicia serviço
                            myReceiver = new MyReceiver();
                            IntentFilter intentFilter = new IntentFilter();
                            intentFilter.addAction(SocketService.MY_ACTION);
                            registerReceiver(myReceiver, intentFilter);

                            startService(new Intent(HomeActivity.this, SocketService.class));
                            doBindService();

                        }
                    }
                }, 3000);
            }

            respostaServer = null;
        }
        else{
            Toast.makeText(HomeActivity.this, "Erro de Conexão", Toast.LENGTH_LONG).show();
        }

    }

    public void irProximaActivity(int nLista, Class proxTela){
        Intent i = new Intent(HomeActivity.this, proxTela);
        i.putExtra("sala",salas.get(nLista));
        startActivity(i);
        overridePendingTransition(R.animator.right_slide_in, R.animator.right_slide_out);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                atualizar(1);
                break;
            case R.id.action_logs:
                startActivity(new Intent(HomeActivity.this,LogsActivity.class));
                overridePendingTransition(R.animator.right_slide_in, R.animator.right_slide_out);
                break;
            case R.id.item_sair:
                alertDialog.show();
                break;
        }
        return true;
    }


    //Codigo padrão de conexao
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBoundService = ((SocketService.LocalBinder)service).getService();
            atualizar(0);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBoundService = null;
        }
    };

    private void doBindService(){
        bindService(new Intent(HomeActivity.this, SocketService.class), mConnection, Context.BIND_AUTO_CREATE);
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
        public void onReceive(Context arg0,Intent intent) {
            respostaServer = intent.getStringExtra("Mensagem");
            if (respostaServer.contains("estadoLuzes")) {
                Type tipoLista = new TypeToken<ArrayList<Sala>>() {
                }.getType();
                salas = gson.fromJson(respostaServer, tipoLista);

                //Para sala 1
                if (salas.get(0).isEstadoLuzes()) {
                    imgLuz1.setImageResource(R.drawable.ic_luz_on);
                } else {
                    imgLuz1.setImageResource(R.drawable.ic_luz_off);
                }
                if (salas.get(0).isEstadoAr()) {
                    imgAr1.setImageResource(R.drawable.ic_ar_on);
                } else {
                    imgAr1.setImageResource(R.drawable.ic_ar_off);
                }
                if (salas.get(0).isPresenca()) {
                    imgPresenca1.setImageResource(R.drawable.ic_pessoa_on);
                } else {
                    imgPresenca1.setImageResource(R.drawable.ic_pessoa_off);
                }

                //Para sala 2
                if (salas.get(1).isEstadoLuzes()) {
                    imgLuz2.setImageResource(R.drawable.ic_luz_on);
                } else {
                    imgLuz2.setImageResource(R.drawable.ic_luz_off);
                }
                if (salas.get(1).isEstadoAr()) {
                    imgAr2.setImageResource(R.drawable.ic_ar_on);
                } else {
                    imgAr2.setImageResource(R.drawable.ic_ar_off);
                }
                if (salas.get(1).isPresenca()) {
                    imgPresenca2.setImageResource(R.drawable.ic_pessoa_on);
                } else {
                    imgPresenca2.setImageResource(R.drawable.ic_pessoa_off);
                }

                //Para sala 3
                if (salas.get(2).isEstadoLuzes()) {
                    imgLuz3.setImageResource(R.drawable.ic_luz_on);
                } else {
                    imgLuz3.setImageResource(R.drawable.ic_luz_off);
                }
                if (salas.get(2).isEstadoAr()) {
                    imgAr3.setImageResource(R.drawable.ic_ar_on);
                } else {
                    imgAr3.setImageResource(R.drawable.ic_ar_off);
                }
                if (salas.get(2).isPresenca()) {
                    imgPresenca3.setImageResource(R.drawable.ic_pessoa_on);
                } else {
                    imgPresenca3.setImageResource(R.drawable.ic_pessoa_off);
                }
            }
        }
    }


}
