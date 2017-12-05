package com.example.pealan.ifcontrol.modelo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pealan.ifcontrol.R;

import java.text.SimpleDateFormat;
import java.util.List;

/**Classe que herda funcionalidades do ArrayAdapter de modo a gerar uma lista
 * personalizada de objetos da classe Acao, para mostrar-la em um ListView
 * Created by Pedro Alan on 05/07/2015.
 */
public class ListaLogsAdapter extends ArrayAdapter<Acao> {

    private Context context;
    private List<Acao> logs = null;

    public ListaLogsAdapter(Context context, List<Acao> logs) {
        super(context, 0, logs);
        this.context = context;
        this.logs = logs;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        Acao acao = logs.get(position);

        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.item_lista_logs,null);

        //Varia o texto e a imagem dependendo da acao
        String tipoAcao;
        int imagem = R.drawable.ic_settings_remote_black_24dp;
        switch (acao.getTipoAcao()){
            case "LUZON.":tipoAcao = "ligou a luz";imagem = R.drawable.lampada_on125;
                break;
            case "LUZOFF.":tipoAcao = "desligou a luz";imagem = R.drawable.lampada_off125;
                break;
            case "ARON.":tipoAcao = "ligou o ar";imagem = R.drawable.floco_on125;
                break;
            case "AROFF.":tipoAcao = "desligou o ar";imagem = R.drawable.floco_off125;
                break;
            default: tipoAcao = "mudou a temperatura do ar";
        }
        //Última verificação caso seja uma ação de mudanca de horario de ativação
        if (acao.getTipoAcao().contains("HA")){
            tipoAcao = "mudou o horario de ativação";
            imagem = R.drawable.ic_alarm_on_black_24dp;
        }else if(acao.getTipoAcao().contains("HD")){
            tipoAcao = "mudou o horario de desativação";
            imagem = R.drawable.ic_alarm_on_black_24dp;
        }

        //Muda imagem do item da lista dependendo da ação
        ImageView imgTipoAcao = (ImageView) view.findViewById(R.id.imageTipoAcao);
        imgTipoAcao.setImageResource(imagem);

        //Muda o texto do item da lista dependendo da ação
        TextView txtFrase = (TextView) view.findViewById(R.id.textFrase);
        String frase = acao.getLogin()+" "+tipoAcao+" da Sala "+acao.getnSala()+"!";
        txtFrase.setText(frase);

        TextView txtTempo = (TextView) view.findViewById(R.id.textTempoAcao);
        SimpleDateFormat s = new SimpleDateFormat("dd/MM/yyyy");
        String data = s.format(acao.getDataAcao().getTime());
        String hora = acao.getHoraAcao().toString();
        txtTempo.setText(data+" às "+hora);

        return view;
    }
}
