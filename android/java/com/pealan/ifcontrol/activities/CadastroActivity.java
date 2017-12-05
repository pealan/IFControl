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
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pealan.ifcontrol.R;
import com.example.pealan.ifcontrol.controle.SocketService;
import com.example.pealan.ifcontrol.modelo.User;
import com.google.gson.Gson;

public class CadastroActivity extends Activity {

    AlertDialog.Builder alertDialog;
    SocketService mBoundService;
    MyReceiver myReceiver;
    private boolean mIsBound;
    EditText edtSiap,edtNome,edtLogin,edtSenha;
    Button btnCadastro;
    boolean cadastroValidado = false;
    boolean siapInvalido, loginInvalido, erro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        edtSiap = (EditText) findViewById(R.id.edtSiap);
        edtNome = (EditText) findViewById(R.id.edtNome);
        edtLogin = (EditText) findViewById(R.id.edtLoginCad);
        edtSenha = (EditText) findViewById(R.id.edtSenhaCad);
        btnCadastro = (Button) findViewById(R.id.btnCadastro);

        btnCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.show();
            }
        });

        //Criar tela de aviso
        alertDialog = new AlertDialog.Builder(CadastroActivity.this);
        alertDialog.setTitle("Confirmação de Cadastro")
                .setMessage("Cadastrar-se no IFControl com esses dados?")
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        aguardarValidacao();
                    }
                })
                .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
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
        doUnbindService();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        doUnbindService();
        super.onDestroy();
    }

    public void aguardarValidacao(){
        User user = new User();

        //Preenche os campos do objeto user
        user.setSiap(Long.valueOf(edtSiap.getText().toString()));
        user.setNome(edtNome.getText().toString());
        user.setLogin(edtLogin.getText().toString());
        user.setSenha(edtSenha.getText().toString());
        Gson gson = new Gson();

        //Transforma o objeto user em um JSON
        String dadosCadastro = gson.toJson(user);

        //Envia o JSON para o servidor
        if(mBoundService!=null) {
            mBoundService.sendMessage(dadosCadastro);
        }
        else{
            return;
        }

        //Espera 3 segundos
        final ProgressDialog progressDialog = ProgressDialog.show(CadastroActivity.this,"","Validando Dados...",true);
        progressDialog.show();
        final Handler handler =  new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
                if (cadastroValidado) {//Termina Activity
                    Toast.makeText(getApplicationContext(), "Cadastro efetuado com sucesso", Toast.LENGTH_LONG).show();

                    finish();
                } else {
                    if (siapInvalido) {
                        edtSiap.setText("");
                        edtSiap.requestFocus();
                        Toast.makeText(getApplicationContext(), "SIAP inválido!", Toast.LENGTH_LONG).show();
                        siapInvalido = false;
                    } else if (loginInvalido) {
                        edtLogin.setText("");
                        edtLogin.requestFocus();
                        Toast.makeText(getApplicationContext(), "Login já esta sendo utilizado", Toast.LENGTH_LONG).show();
                        loginInvalido = false;
                    } else if (erro) {
                        Toast.makeText(getApplicationContext(), "Erro de conexao com o servidor", Toast.LENGTH_LONG).show();
                        erro = false;
                    }
                }
            }
        }, 3000);
    }

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
        bindService(new Intent(CadastroActivity.this, SocketService.class), mConnection, Context.BIND_AUTO_CREATE);
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
            if (resposta.equals("CAD_OK")){
                cadastroValidado = true;
            }
            else if(resposta.equals("INVALID_SIAP")){
                siapInvalido = true;
            }
            else if (resposta.equals("INVALID_LOGIN")){
                loginInvalido = true;
            }
            else{
                erro = true;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cadastro, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.home){
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
