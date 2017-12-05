package controle;

import java.io.InputStream;
import java.util.Scanner;
import com.google.gson.Gson;
import java.io.PrintStream;
import java.text.ParseException;
import modelo.Acao;
import modelo.DAOManager;
import modelo.User;

/**
 *
 * @author Pedro Alan
 */
class TrataCliente implements Runnable {

    private InputStream clienteIn;
    private PrintStream clienteOut;
    private Servidor servidor;
    private Sessao sessao;
    private User user;
    private Acao acao;
    private Gson gson;
    private DAOManager manager;

    public TrataCliente(InputStream clienteIn, PrintStream clienteOut, Servidor servidor, DAOManager manager) {
        this.clienteIn = clienteIn;
        this.servidor = servidor;
        this.clienteOut = clienteOut;
        this.manager = manager;
        clienteOut.println("CONNECTOK");
        gson = new Gson();
    }

    @Override
    public void run() {
        try (Scanner s = new Scanner(this.clienteIn)) {
            while (s.hasNextLine()) {
                String msg = s.nextLine();
                servidor.setMessage(msg);
                if (msg.contains("_login")) {
                    sessao = gson.fromJson(msg, Sessao.class);
                    sessao.setManager(manager);
                    clienteOut.println(sessao.login());
                } else if (msg.contains("siap")) {//Cadastro
                    user = gson.fromJson(msg, User.class);
                    sessao = new Sessao();
                    sessao.setManager(manager);
                    clienteOut.println(sessao.cadastrarUser(user.getSiap(), user.getNome(), user.getLogin(), user.getSenha()));
                } else if (msg.contains("tipoAcao")) {
                    acao = gson.fromJson(msg, Acao.class);
                    String r = sessao.tratarAcao(acao.getTipoAcao(), acao.getnSala());
                    servidor.distribuiMsg(sessao.pegarSalas());
                    servidor.iniciarAgendamentos();
                } else if (msg.contains("logs")) {
                    clienteOut.println(sessao.pegarLogs());
                } else if (msg.contains("atualizar")) {
                    servidor.distribuiMsg(sessao.pegarSalas());
                }
            }
        } catch (ParseException ex) {
            System.out.println(ex.getMessage());
        }
    }

}
