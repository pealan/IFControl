/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controle;

import modelo.Acao;
import modelo.User;
import modelo.Sala;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import modelo.*;

/**
 *
 * @author Pedro Alan
 */
public class Sessao extends SocketArduino {

    private String _login;
    private String _senha;
    private DAOManager manager;
    private User user;
    private Acao acao;
    private Sala sala;
    private Gson gson;

    public Sessao() {
        gson = new Gson();
    }

    public void setManager(DAOManager manager) {
        this.manager = manager;
    }

    /**
     * Retorna verdadeiro ou falso caso login foi autenticado ou não.
     *
     * @return Verdadeiro ou falso
     */
    public String login() {

        user = manager.validaLogin(_login, _senha);
        if (user != null) {
            if (user.getLogin() != null) {
                return "LOGIN_OK";
            } else {
                return "LOGIN_NOTOK";
            }
        } else {
            return "ERROR_BD_SELECT";
        }

    }

    /**
     * Retorna uma mensagem dizendo se o cadastro foi efetuado com sucesso ou
     * não Monta os campos do objeto user com os parametros e envia o objeto
     * para ser inserido no banco de dados.
     *
     * @param siap
     * @param nome
     * @param login
     * @param senha
     * @return
     */
    public String cadastrarUser(Long siap, String nome, String login, String senha) {
        if (manager.siapExiste(siap)) {
            return "INVALID_SIAP";
        } else if (manager.loginExiste(login)) {
            return "INVALID_LOGIN";
        } else {
            user = new User();
            user.setLogin(login);
            user.setNome(nome);
            user.setSiap(siap);
            user.setSenha(senha);
            if (manager.inserirUser(user) == 1) {
                return "CAD_OK";
            } else {
                return "ERROR_BD_INSERT";
            }
        }
    }

    /**
     * Metodo disparado quando o usuario executa alguma acao na sala Se obtém
     * sucesso no envio da acao ao Arduino segue para a adição da ação no banco
     * de dados
     *
     * @param acao
     * @param nSala
     * @return
     * @throws java.text.ParseException
     */
    public String tratarAcao(String acao, int nSala) throws ParseException {
        this.acao = new Acao();
        sala = new Sala();
        String resposta = "";

        //Preenche dados da acao
        this.acao.setTipoAcao(acao);
        this.acao.setIdUser(user.getIdUser());
        this.acao.setnSala(nSala);
        this.acao.setLogin(_login);

        sala = manager.procuraSala(nSala);

        //Caso seja uma ação de verdade
        if (!acao.contains("HD") && !acao.contains("HA")) {
            if (nSala == 1) {
                try {
                    conectarArduino();
                    enviar(acao);
                    resposta = ler();
                    desconectarArduino();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }

                if (resposta.equals("OK")) {
                    this.acao.setStatus(true);
                } else {
                    this.acao.setStatus(false);
                }
            } else {
                this.acao.setStatus(true);
            }

            //Muda campo da sala de acordo com a acao
            switch (acao) {
                case "LUZOFF.":
                    sala.setEstadoLuzes(false);
                    break;
                case "LUZON.":
                    sala.setEstadoLuzes(true);
                    break;
                case "AROFF.":
                    sala.setEstadoAr(false);
                    break;
                case "ARON.":
                    sala.setEstadoAr(true);
                    break;
                case "AR20.":
                    sala.setTempAr(20);
                    break;
                case "AR21.":
                    sala.setTempAr(21);
                    break;
                case "AR22.":
                    sala.setTempAr(22);
                    break;
                case "AR23.":
                    sala.setTempAr(23);
                    break;
                case "AR24.":
                    sala.setTempAr(24);
                    break;
                case "AR25.":
                    sala.setTempAr(25);
                    break;

            }
        } else {
            this.acao.setStatus(true);
            if (acao.contains("HA")) {
                String horaHA = acao.substring(2, 10);
                if (horaHA.equals("99:99:99")) {
                    sala.setHoraAtivacao(null);
                } else {
                    SimpleDateFormat formatador = new SimpleDateFormat("HH:mm:ss");
                    Date data = formatador.parse(horaHA);
                    Time time = new Time(data.getTime());
                    sala.setHoraAtivacao(time);
                }
            } else if (acao.contains("HD")) {
                String horaHD = acao.substring(2, 10);
                if (horaHD.equals("99:99:99")) {
                    sala.setHoraDesativacao(null);
                } else {
                    SimpleDateFormat formatador = new SimpleDateFormat("HH:mm:ss");
                    Date data = formatador.parse(horaHD);
                    Time time = new Time(data.getTime());
                    sala.setHoraDesativacao(time);
                }
            }
        }

        //Insere novo registro de acao e atualiza dados da sala no BD
        if (manager.inserirAcao(this.acao) == 1 && manager.alteraSala(sala) == 1) {
            return "ACAO_OK";
        } else if (manager.inserirAcao(this.acao) != 1) {
            return "ERROR_BD_INSERT";
        } else {
            return "ERROR_BD_UPDATE";
        }
    }

    /**
     * Retorna um Json de todos os registros de ações(logs) do Banco de Dados.
     *
     * @return Json
     */
    public String pegarLogs() {

        List<Acao> logs = manager.consultarLogs();
        if (logs != null) {
            return gson.toJson(logs);
        } else {
            return "ERROR_SELECT_LOGS";
        }

    }

    /**
     * Retorna um Json de todos as salas registradas no Banco de Dados.
     *
     * @return
     */
    public String pegarSalas() {

        List<Sala> salas = manager.consultarSalas();
        if (salas != null) {
            return gson.toJson(salas);
        } else {
            return "ERROR_SELECT_LOGS";
        }
    }

    public String getLogin() {
        return _login;
    }

    public void setLogin(String _login) {
        this._login = _login;
    }

    public String getSenha() {
        return _senha;
    }

    public void setSenha(String _senha) {
        this._senha = _senha;
    }

}
