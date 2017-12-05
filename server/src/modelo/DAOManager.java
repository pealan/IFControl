/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 *
 * @author Pedro Alan
 */
public class DAOManager {

    private Connection conexao;

    /**
     * Estabelece uma conexão com o banco de dados
     *
     * @return status da conexao
     */
    public int connect() {
        try {
            this.conexao = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/ifcontrol", "root", "ifcontrol");
            return 1;
        } catch (SQLException e) {
            System.out.println("Erro ao conectar o banco de dados: " + e.getMessage());
            return 0;
        }
    }

    /**
     * Insere um determinado usuário no banco de dados
     *
     * @param user
     * @return Se a operação foi bem sucedida(1) ou não (0).
     */
    public int inserirUser(User user) {

        String sql = "insert into user "
                + "(siap,nome,login,senha)"
                + " values (?,?,?,?)";
        try {
            PreparedStatement stmt = conexao.prepareStatement(sql);
            stmt.setLong(1, user.getSiap());
            stmt.setString(2, user.getNome());
            stmt.setString(3, user.getLogin());
            stmt.setString(4, user.getSenha());

            stmt.execute();
            stmt.close();
            return 1;
        } catch (SQLException e) {
            System.out.println("Erro ao inserir o usuário: " + e.getMessage());
            return 0;
        }

    }

    /**
     * Retorna uma lista de logs, da classe Acao, que estão no Banco de Dados.
     *
     * @return Lista de Ações
     */
    public List<Acao> consultarLogs() {

        List<Acao> acoes = new ArrayList<>();
        try {
            PreparedStatement stmt = this.conexao.prepareStatement("SELECT * FROM acao");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                // criando o objeto  Acao
                Acao acao = new Acao();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(rs.getDate("dataAcao"));
                acao.setDataAcao(calendar);
                acao.setHoraAcao(rs.getTime("horaAcao"));
                acao.setIdAcao(rs.getInt("idAcao"));
                acao.setIdUser(rs.getInt("idUser"));
                acao.setLogin(rs.getString("login"));
                acao.setStatus(rs.getBoolean("statusAcao"));
                acao.setTipoAcao(rs.getString("tipoAcao"));
                acao.setnSala(rs.getInt(("nSala")));

                acoes.add(acao);

            }
            rs.close();
        } catch (SQLException e) {
            System.out.println("Erro ao consultar os logs: " + e.getMessage());
        }

        return acoes;

    }

    /**
     * Retorna a lista de salas cadastradas no Banco de Dados
     *
     * @return Lista de Salas
     * @throws SQLException
     */
    public List<Sala> consultarSalas() {
        List<Sala> salas = new ArrayList<>();

        try {

            PreparedStatement stmt = this.conexao.prepareStatement("SELECT * FROM sala");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Sala sala = new Sala();
                sala.setnSala(rs.getInt("nSala"));
                sala.setEstadoLuzes(rs.getBoolean("est_Luzes"));
                sala.setEstadoAr(rs.getBoolean("est_Ar"));
                sala.setTemperatura(rs.getDouble("temperatura"));
                sala.setUmidade(rs.getDouble("umidade"));
                sala.setTempAr(rs.getInt("temp_Ar"));
                sala.setPresenca(rs.getBoolean("presenca"));
                sala.setHoraAtivacao(rs.getTime("horaAtivacao"));
                sala.setHoraDesativacao(rs.getTime("horaDesativacao"));

                salas.add(sala);
            }

            rs.close();

        } catch (SQLException e) {
            System.out.println("Erro ao consultar as salas: " + e.getMessage());
        }

        return salas;
    }

    /**
     * Retorna um objeto Sala com dados de uma sala específica a partir do
     * número dela.
     *
     * @param nSala
     * @return Sala requerida
     * @throws java.sql.SQLException
     */
    public Sala procuraSala(int nSala) {
        Sala sala = new Sala();
        String sql = "SELECT * FROM sala WHERE nSala = ?";
        try {
            PreparedStatement stmt = this.conexao.prepareStatement(sql);
            stmt.setInt(1, nSala);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                sala.setnSala(rs.getInt("nSala"));
                sala.setEstadoAr(rs.getBoolean("est_Ar"));
                sala.setEstadoLuzes(rs.getBoolean("est_Luzes"));
                sala.setTempAr(rs.getInt("temp_Ar"));
                sala.setHoraAtivacao(rs.getTime("horaAtivacao"));
                sala.setHoraDesativacao(rs.getTime("horaDesativacao"));
                sala.atualizaSala(rs.getDouble("temperatura"), rs.getDouble("umidade"), rs.getBoolean("presenca"));
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Erro ao procurar a sala: " + e.getMessage());
        }

        return sala;
    }

    /**
     * Altera os dados de uma determinada Sala no banco de dados, a partir de um
     * objeto Sala passado como parâmetro.
     *
     * @param sala
     * @return Se a operação foi bem sucedida(1) ou não (0).
     */
    public int alteraSala(Sala sala) {
        String sql = "UPDATE sala SET est_Luzes=?, est_Ar=?,temperatura=?,umidade=?,"
                + "temp_Ar=?,presenca=?,horaAtivacao=?,horaDesativacao=? WHERE nSala=?";

        try {
            PreparedStatement stmt = this.conexao.prepareStatement(sql);
            stmt.setBoolean(1, sala.isEstadoLuzes());
            stmt.setBoolean(2, sala.isEstadoAr());
            stmt.setDouble(3, sala.getTemperatura());
            stmt.setDouble(4, sala.getUmidade());
            stmt.setDouble(5, sala.getTempAr());
            stmt.setBoolean(6, sala.isPresenca());

            //Caso o usuario queira retirar um horario de desativação ou ativação
            if (sala.getHoraAtivacao() == null) {
                stmt.setNull(7, java.sql.Types.TIME);
            } else {
                stmt.setTime(7, sala.getHoraAtivacao());
            }
            if (sala.getHoraDesativacao() == null) {
                stmt.setNull(8, java.sql.Types.TIME);
            } else {
                stmt.setTime(8, sala.getHoraDesativacao());
            }

            stmt.setInt(9, sala.getnSala());
            stmt.execute();
            stmt.close();
            return 1;

        } catch (SQLException e) {
            System.out.println("Erro ao alterar a sala: " + e.getMessage());
            return 0;
        }
    }

    /**
     * Retorna um objeto do tipo User a partir de um login e senha como
     * parâmetro.Caso não exista um usuário com esses dados, o método retorna um
     * objeto User com o atributo "login" vazio.
     *
     * @param login
     * @param senha
     * @return Objeto User
     * @throws java.sql.SQLException
     */
    public User validaLogin(String login, String senha) {
        User user = new User();

        String sql = "SELECT * FROM user WHERE login = ? and senha = ?";
        try {
            PreparedStatement stmt = this.conexao.prepareStatement(sql);

            stmt.setString(1, login);
            stmt.setString(2, senha);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                user.setIdUser(rs.getInt("idUser"));
                user.setLogin(rs.getString("login"));
                user.setSenha(rs.getString("senha"));
                user.setSiap(rs.getLong("siap"));

                rs.close();
                stmt.close();
                return user;
            } else {
                user.setLogin(null);
                rs.close();
                stmt.close();
                return user;
            }
        } catch (SQLException e) {
            System.out.println("Erro ao validar login: " + e.getMessage());
            return null;
        }

    }

    /**
     * Retorna verdadeiro ou falso em relação a existência de um SIAP, evitando
     * que um usuário possua dois cadastros.
     *
     * @param siap
     * @return Verdadeiro ou falso
     */
    public boolean siapExiste(Long siap) {
        try {
            String sql = "SELECT * FROM user WHERE siap = ?";
            PreparedStatement stmt = this.conexao.prepareStatement(sql);
            stmt.setLong(1, siap);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Erro ao procurar siap: " + e.getMessage());
        }

        return false;
    }

    /**
     * Retorna verdadeiro ou falso em relação a existência de um login, evitando
     * que haja dois login de mesmo nome no Banco de Dados.
     *
     * @param login
     * @return Verdadeiro ou falso
     */
    public boolean loginExiste(String login) {
        try {
            String sql = "SELECT * FROM user WHERE login = ?";
            PreparedStatement stmt = this.conexao.prepareStatement(sql);
            stmt.setString(1, login);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Erro ao procurar login: " + e.getMessage());
        }

        return false;
    }

    /**
     * Insere um registro de Acao(log) no Banco de dados.
     *
     * @param acao
     * @return Se a operação foi bem sucedida(1) ou não (0).
     */
    public int inserirAcao(Acao acao) {
        try {
            String sql = "insert into acao "
                    + "(idUser,nSala,tipoAcao,statusAcao,login,dataAcao,horaAcao)"
                    + " values (?,?,?,?,?,CURDATE(),CURTIME())";
            try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
                stmt.setInt(1, acao.getIdUser());
                stmt.setInt(2, acao.getnSala());
                stmt.setString(3, acao.getTipoAcao());
                stmt.setBoolean(4, acao.isStatus());
                stmt.setString(5, acao.getLogin());

                stmt.execute();
                stmt.close();
            }
            return 1;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return 0;
        }
    }

    public void atualizaSala(double temperatura, double umidade, boolean presenca) {
        try {
            String sql = "UPDATE sala SET temperatura=?, umidade=?, presenca=? WHERE nSala=1";
            PreparedStatement stmt = this.conexao.prepareStatement(sql);
            stmt.setDouble(1, temperatura);
            stmt.setDouble(2, umidade);
            stmt.setBoolean(3, presenca);
            stmt.execute();
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar sala: " + e.getMessage());
        }
    }
}
