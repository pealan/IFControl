package com.example.pealan.ifcontrol.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.Window;
import android.widget.Toast;

import com.example.pealan.ifcontrol.R;
import com.example.pealan.ifcontrol.controle.SocketService;


public class SplashActivity extends Activity {

    AlertDialog.Builder alertDialog;

    SocketService mBoundService;
    MyReceiver myReceiver;
    private boolean mIsBound;

    String confirmacao;
    boolean conectado = false;
    boolean loginValido = false;
    boolean erro = false;
    String login,senha;

    /*@Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //Esconde a ActionBar
        ActionBar actionBar = getActionBar();
        actionBar.hide();

        //Criar tela de aviso
        alertDialog = new AlertDialog.Builder(SplashActivity.this);

        //Caso o usuario aperte "Sim" o servico é reiniciado, caso aperte "Não" o serviço é destruido e o aplicativo fechado
        alertDialog.setTitle("Erro de conexão")
                .setMessage("Tentar conectar novamente?")
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        stopService(new Intent(SplashActivity.this, SocketService.class));

                        myReceiver = new MyReceiver();
                        IntentFilter intentFilter = new IntentFilter();
                        intentFilter.addAction(SocketService.MY_ACTION);
                        registerReceiver(myReceiver, intentFilter);
                        startService(new Intent(SplashActivity.this, SocketService.class));
                        doBindService();

                        esperarConexao();
                    }
                })
                .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        stopService(new Intent(SplashActivity.this, SocketService.class));
                        finish();
                    }
                });

        //Atribui a essas variáveis as preferências de login e senha
        SharedPreferences settings = getSharedPreferences(LoginActivity.PREFS_NAME, 0);
        login = settings.getString("loginPreferencia", "");
        senha = settings.getString("senhaPreferencia", "");
    }

    @Override
    protected void onStart() {
        //Registra o "Listener" para essa Activity, que receberá Intents do serviço "SocketService"
        myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(SocketService.MY_ACTION);
        registerReceiver(myReceiver, intentFilter);

        //Inicia o SocketService
        startService(new Intent(SplashActivity.this, SocketService.class));
        doBindService();

        //Aguarda um "CONNECT_OK" e, se for o caso, LOGIN_OK referente ao "auto-login"
        esperarConexao();
        super.onStart();
    }


    @Override
    protected void onDestroy() {
        //Ao ir para próxima activity, retira o Listener e se desliga do Serviço
        unregisterReceiver(myReceiver);
        doUnbindService();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        //Caso o usuário aperte para voltar antes dos 3 segundos de confirmação, a Activity encerrará o SocketService
        stopService(new Intent(SplashActivity.this, SocketService.class));
        finish();
    }

    public void esperarConexao(){

        //Aguarda "CONNECT_OK" de conexão por 3 segundos
        final Handler handler =  new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!conectado) {//Caso o "CONNECT_OK" não chegue em 3 segundos, mostra o Dialog
                    alertDialog.show();
                }
                else {
                    if (login.equals("") && senha.equals("")) {//Se o usuáio não salvou nenhuma preferência
                        Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(i);
                        overridePendingTransition(R.animator.right_slide_in, R.animator.right_slide_out);
                    }
                    else {//AutoLogin
                        //Envia informações do login para o servidor por meio do SocketService
                        if(mBoundService!=null) {
                            mBoundService.sendMessage("{\"_login\":\"" + login + "\",\"_senha\":\"" + senha + "\"}");
                        }

                        final Handler handler2 =  new Handler();
                        handler2.postDelayed(new Runnable() {
                            //Após os 2 segundos
                            @Override
                            public void run() {
                                //Caso ocorra algo no cadastro do usuario, o usuário é mandado para a tela de Login
                                if (erro) {
                                    Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                                    startActivity(i);
                                }
                                else if (loginValido) {//Manda direto para a HomeActivity
                                    //Cria Bundle com os valores das variáveis que a HomeActivity necessita
                                    Bundle bundle = new Bundle();
                                    bundle.putString("login", login);
                                    bundle.putString("senha",senha);
                                    //Preenche Intent com Bundle
                                    Intent i = new Intent(SplashActivity.this, HomeActivity.class);
                                    startActivity(i.putExtras(bundle));
                                    overridePendingTransition(R.animator.right_slide_in, R.animator.right_slide_out);
                                }
                            }
                        }, 2000);
                    }
                }
            }
        }, 3000);
    }

    //Variável de Conexão com o Serviço
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

    //Se conectar ao SocketService
    private void doBindService(){
        bindService(new Intent(SplashActivity.this, SocketService.class), mConnection, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    //Se desconectar do SocketService
    private void doUnbindService(){
        if(mIsBound){
            unbindService(mConnection);
            mIsBound = false;
        }
    }

    //Classe que controla o que fazer com mensagens recebidas pelo serviço SocketService e enviadas para as Activities ligadas a ele.
    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context arg0, Intent intent){
            confirmacao = intent.getStringExtra("Mensagem");
            if (confirmacao.equals("CONNECTOK")){
                conectado = true;
            }else if (confirmacao.equals("LOGIN_OK")) {
                loginValido = true;
            }
            else if (confirmacao.equals("ERROR_BD_SELECT")) {
                erro = true;
            }
        }
    }
}
