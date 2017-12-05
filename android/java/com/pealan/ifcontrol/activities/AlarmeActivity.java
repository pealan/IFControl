package com.example.pealan.ifcontrol.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.pealan.ifcontrol.R;
import com.example.pealan.ifcontrol.controle.SocketService;
import com.example.pealan.ifcontrol.modelo.Sala;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AlarmeActivity extends Activity {

    SocketService mBoundService;
    MyReceiver myReceiver;
    private boolean mIsBound;

    LinearLayout lHoraAtivacao,lHoraDesativacao;
    TextView txtHoraAtivacao,txtHoraDesativacao;
    Switch swtAtivacao,swtDesativacao;
    TimePickerDialog tpkHora;
    AlertDialog.Builder alertDialog;

    Sala sala;
    //List<Sala> salas;
    Gson gson;
    Type tipoLista;

    private int horaHA,minutoHA,horaHD,minutoHD;
    String mensagem;
    boolean confirmacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarme);
        confirmacao = false;

        //Inicia Gson e sala a partir do intent da HomeActivity
        gson = new Gson();
        sala = (Sala) getIntent().getSerializableExtra("sala");

        //Define o tipo de lista que essa activity vai receber periodicamente do servidor
        tipoLista = new TypeToken<ArrayList<Sala>>(){
        }.getType();

        //Criar tela de aviso
        alertDialog = new AlertDialog.Builder(AlarmeActivity.this);


        lHoraAtivacao = (LinearLayout) findViewById(R.id.layoutHA);
        lHoraDesativacao = (LinearLayout) findViewById(R.id.layoutHD);
        txtHoraAtivacao = (TextView) findViewById(R.id.txtHoraAtv);
        txtHoraDesativacao = (TextView) findViewById(R.id.txtHoraDtv);
        swtAtivacao = (Switch) findViewById(R.id.swtAtivacao);
        swtDesativacao = (Switch) findViewById(R.id.swtDesativacao);

        swtAtivacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mensagem = "{\"tipoAcao\":\"HA";
                if (swtAtivacao.isChecked()) {
                    tpkHora = new TimePickerDialog(AlarmeActivity.this, mTimeSetListener, horaHA, minutoHA, false);
                    tpkHora.setButton(DialogInterface.BUTTON_POSITIVE, "Salvar", tpkHora);
                    tpkHora.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancelar", tpkHora);
                    tpkHora.show();
                } else {
                    alertDialog.setTitle("Horário de Ativação")
                            .setMessage("Deseja apagar o Horário de Ativação")
                            .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //swtAtivacao.setChecked(false);
                                    if (mBoundService != null) {
                                        mBoundService.sendMessage(mensagem + "99:99:99.\",\"nSala\":" + sala.getnSala() + "}");
                                        esperarConfirmacao(0);
                                    } else {
                                        Toast.makeText(AlarmeActivity.this, "Erro de Conexão", Toast.LENGTH_LONG).show();
                                    }
                                }
                            })
                            .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //Reverte ação
                                    swtAtivacao.setChecked(true);
                                }
                            });
                    alertDialog.show();
                }
            }
        });

        swtDesativacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mensagem = "{\"tipoAcao\":\"HD";
                if (swtDesativacao.isChecked()) {
                    tpkHora = new TimePickerDialog(AlarmeActivity.this, mTimeSetListener, horaHD, minutoHD, false);
                    tpkHora.setButton(DialogInterface.BUTTON_POSITIVE, "Salvar", tpkHora);
                    tpkHora.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancelar", tpkHora);
                    tpkHora.show();
                } else {
                    alertDialog.setTitle("Horário de Desativação")
                            .setMessage("Deseja apagar o Horário de Desativação")
                            .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //swtDesativacao.setChecked(false);
                                    if (mBoundService != null) {
                                        mBoundService.sendMessage(mensagem + "99:99:99.\",\"nSala\":" + sala.getnSala() + "}");
                                        esperarConfirmacao(1);
                                    } else {
                                        Toast.makeText(AlarmeActivity.this, "Erro de Conexão", Toast.LENGTH_LONG).show();
                                    }
                                }
                            })
                            .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //Reverte ação
                                    swtDesativacao.setChecked(true);
                                }
                            });
                    alertDialog.show();
                }
            }
        });

        lHoraAtivacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mensagem = "{\"tipoAcao\":\"HA";
                tpkHora = new TimePickerDialog(AlarmeActivity.this, mTimeSetListener, horaHA, minutoHA, false);
                tpkHora.setButton(DialogInterface.BUTTON_POSITIVE, "Salvar", tpkHora);
                tpkHora.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancelar", tpkHora);
                tpkHora.show();
            }
        });

        lHoraDesativacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mensagem = "{\"tipoAcao\":\"HD";
                tpkHora = new TimePickerDialog(AlarmeActivity.this, mTimeSetListener, horaHD, minutoHD, false);
                tpkHora.setButton(DialogInterface.BUTTON_POSITIVE, "Salvar", tpkHora);
                tpkHora.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancelar", tpkHora);
                tpkHora.show();
            }
        });

        atualizaHorarios();

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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_alarme, menu);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
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

    //Quando o usuario escolhe um horario do TimePickerDialog
    private TimePickerDialog.OnTimeSetListener mTimeSetListener =
            new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    if (mBoundService != null) {
                        mBoundService.sendMessage(mensagem+formatar(hourOfDay)+":"+formatar(minute)+":00"+
                                ".\",\"nSala\":" + sala.getnSala()+"}");
                        esperarConfirmacao(2);
                    } else {
                        Toast.makeText(AlarmeActivity.this, "Erro de Conexão", Toast.LENGTH_LONG).show();
                    }
                }
            };

    //Formatar texto para numeros menores que 10
    private static String formatar(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    public void esperarConfirmacao(final int tipo){
        final ProgressDialog progressDialog = ProgressDialog.show(AlarmeActivity.this, "", "Aguardando confirmação", true);
        progressDialog.show();

        //Aguarda confirmação de conexao por 3 segundos
        final Handler handler =  new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (confirmacao) {
                    Toast.makeText(AlarmeActivity.this,"Alterações concluídas",Toast.LENGTH_LONG).show();
                    confirmacao = false;
                }
                else{
                    switch (tipo){
                        //Se o erro ocorreu no switch de ativacao
                        case 0:
                            if(swtAtivacao.isChecked()) {
                                swtAtivacao.setChecked(false);
                            }else{
                                swtAtivacao.setChecked(true);
                            }
                            break;

                        //Se o erro ocorreu no switch de desativacao
                        case 1:
                            if(swtDesativacao.isChecked()) {
                                swtDesativacao.setChecked(false);
                            }else{
                                swtDesativacao.setChecked(true);
                            }
                            break;

                        //Se o o erro ocorrou o TimePickerDialog
                        case 2:
                            //Volta ao horarios que estao no objeto, pois não foram alterados
                            atualizaHorarios();
                    }
                    Toast.makeText(AlarmeActivity.this, "Erro de conexão",Toast.LENGTH_LONG).show();
                }
                progressDialog.dismiss();
            }
        }, 3000);
    }

    public void atualizaHorarios(){
        Calendar calendar = Calendar.getInstance();
        if (sala.getHoraAtivacao() != null){
            calendar.setTime(sala.getHoraAtivacao());
            horaHA = calendar.get(Calendar.HOUR_OF_DAY);
            minutoHA = calendar.get(Calendar.MINUTE);
            txtHoraAtivacao.setText(formatar(horaHA)+ ":"+formatar(minutoHA));
            swtAtivacao.setChecked(true);
        }else{
            txtHoraAtivacao.setText("--:--");
            horaHA = 6;
            minutoHA = 0;
            swtAtivacao.setChecked(false);
        }

        if (sala.getHoraDesativacao() != null){
            calendar.setTime(sala.getHoraDesativacao());
            horaHD = calendar.get(Calendar.HOUR_OF_DAY);
            minutoHD = calendar.get(Calendar.MINUTE);
            txtHoraDesativacao.setText(formatar(horaHD)+ ":"+formatar(minutoHD));
            swtDesativacao.setChecked(true);
        }else{
            txtHoraDesativacao.setText("--:--");
            horaHD = 18;
            minutoHD = 0;
            swtDesativacao.setChecked(false);
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
        bindService(new Intent(AlarmeActivity.this, SocketService.class), mConnection, Context.BIND_AUTO_CREATE);
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
            String resposta = intent.getStringExtra("Mensagem");
            confirmacao = true;
            HomeActivity.salas = gson.fromJson(resposta, tipoLista);
            for (Sala s : HomeActivity.salas) {
                if (s.getnSala() == sala.getnSala()) {
                    sala = s;
                    System.out.println("Recebi o objeto");
                }
            }
            atualizaHorarios();
        }
    }
}
