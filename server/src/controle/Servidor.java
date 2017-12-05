package controle;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import modelo.DAOManager;
import modelo.Sala;

/**
 *
 * @author Pedro Alan
 */
public class Servidor {

    private final int porta;
    private boolean encerrar;
    private List<PrintStream> clientes;
    private String message;
    private List<Sala> salas;
    protected static DAOManager manager;
    private List<Agendamento> agendamentos;
    private ServerSocket servidor;

    public static void main(String args[]) {

        try {
            new Servidor(808).executa();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }

    public Servidor(int porta) {
        this.porta = porta;
        this.clientes = new ArrayList<>();
        this.message = "IFControl";
    }

    public void executa() throws IOException {

        servidor = new ServerSocket(porta);
        manager = new DAOManager();
        
        System.out.println("Conectado a porta " + porta);

        // Para a execução caso não consiga estabelecer conexão
        // com o banco de dados
        if (manager.connect() == 0) {
            System.exit(0);
        } else {
            System.out.println("Conectado ao banco de dados");
        }

        iniciarAgendamentos();

        new Thread(new AtualizaSala(manager, this)).start();

        while (!encerrar) {
            Socket cliente = null;
            cliente = servidor.accept();
            System.out.println("Nova conexao com o cliente "
                    + cliente.getInetAddress().getHostAddress());

            this.clientes.add(new PrintStream(cliente.getOutputStream()));
            new Thread(new TrataCliente(cliente.getInputStream(), new PrintStream(cliente.getOutputStream()), this, manager)).start();

        }

    }

    /**
     * Envia determinada mensagem para todos os atuais clientes Socket.
     *
     * @param msg
     */
    public void distribuiMsg(String msg) {
        for (PrintStream cliente : this.clientes) {
            cliente.println(msg);
        }
    }

    public void setMessage(String str) {
        this.message = str;
        System.out.println(str);
    }

    /**
     * Percorre vetor de salas e verifica os "horarioAtivacao" e
     * "horarioDesativacao" delas, e, se o valor for diferente de nulo, adiciona
     * a instarncia da classe que vai cuidar do TimerTask referente aquela sala.
     */
    public void iniciarAgendamentos() {
        //Apaga timerTask se já haviam outros
        agendamentos = new ArrayList<>();

        //Pega dados da sala no banco de dados
        salas = manager.consultarSalas();

        for (Sala s : salas) {
            if (s.getHoraAtivacao() != null) {
                agendamentos.add(new Agendamento(s.getHoraAtivacao(), "ON", manager, s, this));
            }
            if (s.getHoraDesativacao() != null) {
                agendamentos.add(new Agendamento(s.getHoraDesativacao(), "OFF", manager, s, this));
            }
        }
    }

}
