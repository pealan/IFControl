package com.example.pealan.ifcontrol.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pealan.ifcontrol.R;
import com.example.pealan.ifcontrol.controle.SocketService;

public class LoginActivity extends Activity {

    EditText edtLogin;
    EditText edtSenha;
    Button btnLogin;
    TextView txtCadastro,txtLabel;
    CheckBox chkPreferencia;

    SocketService mBoundService;
    MyReceiver myReceiver;
    private boolean mIsBound;

    public static final String PREFS_NAME = "Preferencias";
    boolean loginValido,erro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtLogin = (EditText) findViewById(R.id.edtLogin);
        edtSenha = (EditText) findViewById(R.id.edtSenha);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        chkPreferencia = (CheckBox) findViewById(R.id.chkPreferencia);
        txtCadastro = (TextView) findViewById(R.id.txtCadastro);
        txtLabel  = (TextView) findViewById(R.id.txtLabel);

        txtLabel.setTypeface(Typeface.SANS_SERIF);
        //Sublinha o texto para cadastro
        txtCadastro.setPaintFlags(txtCadastro.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        txtCadastro.setText("Não possui cadastro?");

        //"txtCadastro" abre uma tela de cadastro
        txtCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, CadastroActivity.class);
                startActivity(i);
            }
        });

        //"btnLogin" manda os dados de login para o servidor
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aguardarValidacao();
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
        doUnbindService();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        checarPreferencias();
        stopService(new Intent(LoginActivity.this, SocketService.class));
        finish();
    }

    public void aguardarValidacao(){
        //Envia informações do login para o servidor por meio do SocketService
        if(mBoundService!=null) {
            mBoundService.sendMessage("{\"_login\":\"" + edtLogin.getText().toString() + "\",\"_senha\":\"" + edtSenha.getText().toString() + "\"}");
        }
        else{
            return;
        }

        //Cria o ProgressDialog
        final ProgressDialog progressDialog = ProgressDialog.show(LoginActivity.this,"","Validando Login...",true);

        //Mostra o Dialog
        progressDialog.show();

        //ProgressDialog dura 2 segundos
        final Handler handler =  new Handler();
        handler.postDelayed(new Runnable() {
            //Após os 3 segundos
            @Override
            public void run() {
                progressDialog.dismiss();//Retira o ProgressDialog
                if (erro) {
                    Toast.makeText(LoginActivity.this, "Erro de conexao com o servidor", Toast.LENGTH_LONG).show();
                    erro = false;
                } else {
                    if (loginValido) {
                        checarPreferencias();
                        Bundle bundle = new Bundle();
                        bundle.putString("login", edtLogin.getText().toString());
                        bundle.putString("senha",edtSenha.getText().toString());

                        Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(i.putExtras(bundle));
                        overridePendingTransition(R.animator.right_slide_in, R.animator.right_slide_out);
                        finish();
                    } else if (!loginValido) {//Tava dando errado sem essa condicao
                        Toast.makeText(LoginActivity.this, "Login Invalido", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }, 3000);

    }

    public void checarPreferencias(){
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();

        if (chkPreferencia.isChecked()) {
            editor.putString("loginPreferencia", edtLogin.getText().toString());
            editor.putString("senhaPreferencia", edtSenha.getText().toString());
            //Confirma a gravação dos dados
            editor.commit();
        } else {
            editor.putString("loginPreferencia", "");
            editor.putString("senhaPreferencia", "");
            //Confirma a gravação dos dados
            editor.commit();
        }
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
        bindService(new Intent(LoginActivity.this, SocketService.class), mConnection, Context.BIND_AUTO_CREATE);
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
            if (resposta.equals("LOGIN_OK")){
                loginValido = true;
            }
            else if (resposta.equals("LOGIN_NOTOK")){
                loginValido = false;
            }
            else if (resposta.equals("ERROR_BD_SELECT")){
                erro = true;
            }
        }
    }


}
