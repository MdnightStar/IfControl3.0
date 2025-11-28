/**
 * Descrição:
 */
package Modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * @author Jeison e Adriel
 * @version 3.0
 */
public class DAOManager {

    private Connection conexao;

    /**
     * O metodo connect serve para estabelecer a conexão com o banco de dados,
     * caso haja mais versões do IFControl no futuro, é necessario mudar a senha
     * do banco.
     *
     * @return F se aconexão falhar ou V se houver conexão
     * @exception Uma excção SQLException será lançada se a conexão falhar
     */
    public DAOManager() {
        connect();
    }

    public boolean connect() {

        try {
            this.conexao = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/ifcontrol3", "root", "meo0511"); //alterei o nome do banco
            return true;
        } catch (SQLException e) {
            System.out.println("Erro ao conectar o banco de dados: " + e.getMessage());
            return false;
        }
    }

    /**
     * O método inserirUser insere um novo usúario no banco de dados caso seja
     * realizado um novo cadastro.
     *
     * @return F se o cadastro falhar e V para cadastro bem sucedido
     * @param user, instancia da classe User
     * @exception Uma excção SQLException será lançada se o cadastro falhar
     */
    public boolean inserirUser(User user) {

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
            return true;
        } catch (SQLException e) {
            System.out.println("Erro ao inserir o usuário: " + e.getMessage());
            return false;
        }

    }

    /**
     * O método zera a conexão com o arduino, ou seja, modifica nos banco de
     * dados o estado da sala e a conexão colocando as em falso
     *
     * @exception Uma excção SQLException será lançada se a comunicação falhar
     */
    public void resetSalas() {

        String sql = "UPDATE sala SET est_sala=?, conexao=?";
        PreparedStatement stmt;
        try {
            stmt = this.conexao.prepareStatement(sql);
            stmt.setBoolean(1, false);;
            stmt.setBoolean(2, false);
            stmt.execute();
            stmt.close();//Na versão anterior esqueceram de fechar o PreparedStatement, causando problema no sistema, nunca esqueaçam
        } catch (SQLException ex) {
            Logger.getLogger(DAOManager.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * O método confere quais salas estão com conexão, na versão anterior tinha
     * apectos que limitavam o número de salas, porém se acheitou o problema.
     *
     * @exception Uma excção SQLException será lançada se a comunicação falhar
     */
    public boolean[] statusConexao() {
        ArrayList<Boolean> salasDisponiveis = new ArrayList();

        try {
            PreparedStatement stmt = this.conexao.prepareStatement("SELECT * FROM sala");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                salasDisponiveis.add((boolean) rs.getBoolean("conexao")); //insere estado da conexao

            }
            stmt.close();
            rs.close();
        } catch (SQLException e) {
            System.out.println("Erro ao consultar as salas: " + e.getMessage());
        }
        boolean[] salasDisponiveisC = new boolean[salasDisponiveis.size()];

        for (int i = 0; i < salasDisponiveis.size(); i++) {
            salasDisponiveisC[i] = salasDisponiveis.get(i);

        }
        return salasDisponiveisC;
    }

    /**
     * O método atuliza a conexão de uma sala especifica, serve para uma
     * atualização constante cada vez que o metodo for chamado ele vai mudificar
     * o estatus de uma sala.
     *
     * @param op o valor que boolean que a sala vai receber para o seu estatus
     * (ou seja, esse metodo não verifica se a sala estabeleceu conexão, somente
     * atualiza no banco de dados).
     * @param nsala é o número da sala que vai ser atualizada
     * @exception Uma excção SQLException será lançada se a comunicação falhar
     */
    public void efetivarConexao(boolean op, int nsala) {
        String sql = "UPDATE sala SET conexao=? WHERE nSala=?";

        PreparedStatement stmt;
        try {
            stmt = this.conexao.prepareStatement(sql);
            if (!op) {
                stmt.setBoolean(1, false);
            } else {
                stmt.setBoolean(1, true);
            }
            stmt.setInt(2, nsala);
            stmt.execute();
            stmt.close();
        } catch (SQLException ex) {
            Logger.getLogger(DAOManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * O método consultarLogs retorna uma lista de todas as ações realizada
     * realiçoes pelos usúarios.
     *
     * @return retorna uma List de Acao
     * @exception Uma excção SQLException será lançada se a comunicação falhar
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
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Erro ao consultar os logs: " + e.getMessage());
        }
        return acoes;
    }

    /**
     * O método consultarsLalas retorna uma lista de todas as salas, com todos
     * seus atributos nesse momento serve maiormente para fazer a atualização
     * constante dos atributos da sala na interface.
     *
     * @return retorna uma List de Sala
     * @exception Uma excção SQLException será lançada se a comunicação falhar
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
                sala.setEstadoSala(rs.getBoolean("est_sala"));    //inserir estado sala
                sala.setEstadoDataShow(rs.getBoolean("est_datashow"));  //insere estado datashow
                sala.setConexao(rs.getBoolean("conexao")); //insere estado da conexao
                sala.setIP(rs.getString("IP"));
                salas.add(sala);
            }

            rs.close();
            stmt.close();
            return salas;
        } catch (SQLException e) {
            System.out.println("Erro ao consultar as salas: " + e.getMessage());
        }
        return salas;
    }

    /**
     * O método procurarSala segue o mesmo conceito que o anterior
     * (consultarSalas) pois tem como finalidade retornar as informações de uma
     * sala em especifico.
     *
     * @param nSala número da sala
     * @return retorna uma instancia de Sala
     * @exception Uma excção SQLException será lançada se a comunicação falhar
     */
    public Sala procuraSala(int nSala) {  //cria o objeto da sala, recebe valores do bd e envia
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
                sala.setEstadoSala(rs.getBoolean("est_sala")); //insere estado sala
                sala.setEstadoDataShow(rs.getBoolean("est_datashow")); //insere estado datashow
                sala.setConexao(rs.getBoolean("conexao")); //insere estado da conexao
                sala.setTempAr(rs.getInt("temp_Ar"));
                sala.setHoraAtivacao(rs.getTime("horaAtivacao"));
                sala.setHoraDesativacao(rs.getTime("horaDesativacao"));
                sala.atualizaSala(rs.getDouble("temperatura"), rs.getDouble("umidade"), rs.getBoolean("presenca"));
                sala.setIP(rs.getString("IP"));
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Erro ao procurar a sala: " + e.getMessage());
        }

        return sala;
    }

    /**
     * O método alterarSala tem por finalidade alterar uma sola, ou melhor dito,
     * atualizar uma sala, atribuindo novos valores por meio de uma instancia
     * sala passada por parâmetro.
     *
     * @param Sala instancia de uma Sala
     * @return retorna V para atualização bem sucedida e F caso falhe
     * @exception Uma excção SQLException será lançada se a comunicação falhar
     */
    public boolean alteraSala(Sala sala) {
        String sql = "UPDATE sala SET est_Luzes=?, est_Ar=?,temperatura=?,umidade=?,"
                + "temp_Ar=?,presenca=?,horaAtivacao=?,horaDesativacao=?,est_sala=?,est_datashow=? WHERE nSala=?";  //inserido o campo estado sala e datashow

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
            stmt.setBoolean(9, sala.isEstadoSala()); //atualizar bd
            stmt.setBoolean(10, sala.isEstadoDataShow()); //atualiza estado data show
            stmt.setInt(11, sala.getnSala());
            stmt.execute();
            stmt.close();
            return true;

        } catch (SQLException e) {
            System.out.println("Erro ao alterar a sala: " + e.getMessage());
            return false;
        }
    }

    /**
     * O método validaLogin retorna um objeto do tipo User a partir de um login
     * e senha como parâmetro.Caso não exista um usuário com esses dados, o
     * método retorna um objeto User com os atributos vazios.
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
     * O método retorna verdadeiro ou falso em relação a existência de um SIAP e
     * verificando se ele já esta ligado a um usúario evitando que um usuário
     * possua dois cadastros.
     *
     * @param siap
     * @return Verdadeiro ou falso
     */
    public boolean siapExiste(Long siap) {
        try {
            var sql = "SELECT * FROM user WHERE siap = ?";
            PreparedStatement stmt = this.conexao.prepareStatement(sql);
            stmt.setLong(1, siap);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return true;
            }
            stmt.close();
            rs.close();
        } catch (SQLException e) {
            System.out.println("Erro ao procurar siap: " + e.getMessage());
        }
        return false;
    }

    /**
     * O método loginExiste retorna verdadeiro ou falso em relação a existência
     * de um login, evitando que haja dois login de mesmo nome no Banco de
     * Dados.
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
     * O método inserirAcao insere um registro de Acao(log) no Banco de dados.
     *
     * @param acao
     * @return Se a operação foi bem sucedida(1) ou não (0).
     */
    public boolean inserirAcao(Acao acao) {
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
            return true;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    /**
     * O método atualizaSala tem por finalidade atualizar os atributos da sala
     * no banco de dados.
     *
     * @param nSala
     * @param temperatura
     * @param umidade
     * @param presenca
     */
    public void atualizaSala(int nSala, double temperatura, double umidade, boolean presenca) {
        try {
            String sql = "UPDATE sala SET temperatura=?, umidade=?, presenca=? WHERE nSala=?";
            PreparedStatement stmt = this.conexao.prepareStatement(sql);
            stmt.setDouble(1, temperatura);
            stmt.setDouble(2, umidade);
            stmt.setBoolean(3, presenca);
            stmt.setInt(4, nSala);
            stmt.execute();
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar sala: " + e.getMessage());
        }
    }

    public int totalSalas() {
        String sql = "SELECT COUNT(*) FROM sala";
        int totalSalas;
        try (PreparedStatement stmt = this.conexao.prepareStatement(sql); ResultSet rs = stmt.executeQuery();) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }

    /**
     * O método resgatarCodIr resgata o codigo e as configurações de sinal da
     * determinada ação solicitada.
     *
     * @param nSala Número da sala
     * @param msg Mensagem contendo Tipo e Função (ex: "ACon")
     * @return StringBuilder com a resposta formatada
     * @throws SQLException
     */
    public StringBuilder resgataCodIr(int nSala, String msg) throws SQLException {
        StringBuilder resp = new StringBuilder();
        System.out.println("Mensagem chegou: " + msg + " Sala: " + nSala);

        // Validação básica para evitar erro de índice
        if (msg == null || msg.length() < 2) {
            return new StringBuilder("ERRO.MensagemInvalida");
        }

        resp.append("DIR,");

        // Extração segura do tipo e função
        String tipo = msg.substring(0, 2);
        String funcao;

        if (msg.contains(".")) {
            funcao = msg.substring(2, msg.indexOf("."));
        } else {
            funcao = msg.substring(2);
        }

        System.out.println("Tipo: " + tipo + " Funcao: " + funcao);

        // SQL OTIMIZADO:
        // Usa INNER JOIN (JOIN) para garantir que só retorne se houver código cadastrado.
        // Se um dispositivo não tiver código para essa função, ele é ignorado automaticamente.
        String sqlBuscaCompleta = "SELECT T2.config, T3.cod "
                + "FROM conjuntodis AS T1 "
                + "JOIN dis AS T2 ON T1.dis_id = T2.id "
                + "JOIN codir AS T3 ON T2.id = T3.dispositivo_id "
                + "WHERE T1.sala_id = ? AND T2.tipo = ? AND T3.funcao = ?";

        try (PreparedStatement stmt = this.conexao.prepareStatement(sqlBuscaCompleta)) {
            // Define os parâmetros
            stmt.setInt(1, nSala);
            stmt.setString(2, tipo);
            stmt.setString(3, funcao);

            try (ResultSet rs = stmt.executeQuery()) {
                boolean encontrouAlgum = false;

                while (rs.next()) {
                    String config = rs.getString("config");
                    String cod = rs.getString("cod");

                    // Adiciona o separador '$' se já houver um dispositivo adicionado antes
                    if (encontrouAlgum) {
                        resp.append("$DIR,");
                    }

                    // Formata a resposta: config,cod.
                    resp.append(config).append(",").append(cod).append(".");

                    encontrouAlgum = true;
                    System.out.println("Dispositivo adicionado: " + config);
                }

                if (encontrouAlgum) {
                    return resp;
                } else {
                    return new StringBuilder().append("ERRO.");
                }
            }
        } catch (SQLException ex) {
            System.out.println("Erro SQL: " + ex.getMessage());
            return new StringBuilder().append("ERRO.");
        }
    }

    public boolean adicionarSala(int nSala, String ip) {
        String sql = "INSERT INTO sala (nSala, ip) VALUES (?, ?)";
        try (PreparedStatement stmt = this.conexao.prepareStatement(sql)) {

            stmt.setInt(1, nSala);
            stmt.setString(2, sql);
            stmt.execute();
            return true;

        } catch (SQLException ex) {
            return false;
        }
    }

    /**
     * Elimina um agendamento e todos os seus registros relacionados (dias da
     * semana, salas e dispositivos) a partir do ID.
     *
     * * @param idAgendamento O ID do agendamento a ser eliminado.
     * @return true se a eliminação for bem-sucedida, false caso contrário.
     */
    public boolean eliminarAgendamento(int idAgendamento) {
        // A instrução DELETE afetará apenas a tabela principal 'agendamento'
        // As tabelas filhas serão limpas automaticamente pelo ON DELETE CASCADE.
        String sql = "DELETE FROM agendamento WHERE id = ?";

        // Uso de try-with-resources para garantir o fechamento do PreparedStatement
        try (PreparedStatement stmt = this.conexao.prepareStatement(sql)) {

            // 1. Define o ID do agendamento
            stmt.setInt(1, idAgendamento);

            // 2. Executa a deleção e obtém o número de linhas afetadas
            int linhasAfetadas = stmt.executeUpdate();

            // Se linhasAfetadas > 0, significa que o agendamento foi encontrado e deletado.
            return linhasAfetadas > 0;

        } catch (SQLException ex) {
            System.err.println("Erro ao eliminar agendamento: " + ex.getMessage());
            // Logar o erro completo aqui seria útil em produção
            return false;
        }
    }

    public boolean IPexiste(String IP) {
        String sql = "SELECT * FROM sala WHERE ip = ?";
        try (PreparedStatement stmt = this.conexao.prepareStatement(sql); ResultSet res = stmt.executeQuery()) {
            stmt.setString(0, IP);

            if (res.next()) {
                return true;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public boolean NSalaexiste(int nSala) {
        String sql = "SELECT * FROM sala WHERE nSala = ?";
        try (PreparedStatement stmt = this.conexao.prepareStatement(sql); ResultSet res = stmt.executeQuery()) {
            stmt.setInt(0, nSala);

            if (res.next()) {
                return true;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    /**
     * O método consultarAgendamento retorna uma lista de todos os agendamentos.
     *
     * @return retorna uma List de Agendamento
     * @exception Uma excção SQLException será lançada se a comunicação falhar
     */
    public List<Agendamento> consultarAgendamento() {
        List<Agendamento> agendamentos = new ArrayList<>();
        String sqlAgendamento = "SELECT * FROM agendamento";

        try (PreparedStatement stmt = this.conexao.prepareStatement(sqlAgendamento); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Agendamento agendamento = new Agendamento();
                int idAgendamento = rs.getInt("id");

                // 1. Setar dados principais do Agendamento
                agendamento.setIdAgendamento(idAgendamento); // Assumindo a sugestão de adicionar idAgendamento
                agendamento.setTitulo(rs.getString("titulo"));
                agendamento.setAutor(rs.getString("autor"));
                agendamento.setStatusAgendamento(rs.getBoolean("statusAgendamento"));

                // Conversão de Date para Calendar para dataInicio
                Calendar dataIn = Calendar.getInstance();
                dataIn.setTime(rs.getDate("dataInicio"));
                agendamento.setDataIn(dataIn);

                // Conversão de Date para Calendar para dataFim
                Calendar dataF = Calendar.getInstance();
                dataF.setTime(rs.getDate("dataFim"));
                agendamento.setDataF(dataF);

                // As colunas hAtiv e hDesat estão como DATETIME no BD, o que pode incluir data e hora. 
                // Se o foco é apenas o tempo (Time), você deve usar o método getTime() do ResultSet.
                // Ajustei para usar o Time, assumindo que as partes de data são irrelevantes ou fixas.
                agendamento.sethAtv(rs.getTime("hAtiv"));
                agendamento.sethDesat(rs.getTime("hDesat"));

                // 2. Obter os dias da semana (int[])
                agendamento.setDiaSemana(consultarDiasDaSemana(idAgendamento));

                // 3. Obter os números das salas (int[])
                agendamento.setSalas(consultarNSalasAgendamento(idAgendamento));
                agendamento.setDispositivos(consultarDispositivosAgendamento(idAgendamento));

                agendamentos.add(agendamento);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao consultar agendamentos: " + e.getMessage());
        }
        return agendamentos;
    }

    /**
     * Método auxiliar para consultar os dias da semana de um agendamento.
     */
    private int[] consultarDiasDaSemana(int idAgendamento) throws SQLException {
        List<Integer> dias = new ArrayList<>();
        String sql = "SELECT dia FROM diasDaSemana WHERE agendamento_id = ?";

        try (PreparedStatement stmt = this.conexao.prepareStatement(sql)) {
            stmt.setInt(1, idAgendamento);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    dias.add(rs.getInt("dia"));
                }
            }
        }

        // Converte List<Integer> para int[]
        return dias.stream().mapToInt(i -> i).toArray();
    }

    /**
     * Método auxiliar para consultar os números das salas de um agendamento.
     */
    private int[] consultarNSalasAgendamento(int idAgendamento) throws SQLException {
        List<Integer> salas = new ArrayList<>();
        String sql = "SELECT nSala FROM nSalaAgendamento WHERE agendamento_id = ?";

        try (PreparedStatement stmt = this.conexao.prepareStatement(sql)) {
            stmt.setInt(1, idAgendamento);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    salas.add(rs.getInt("nSala"));
                }
            }
        }

        // Converte List<Integer> para int[]
        return salas.stream().mapToInt(i -> i).toArray();
    }

    private ArrayList consultarDispositivosAgendamento(int idAgendamento) throws SQLException {
        ArrayList<String> dispositivos = new ArrayList<>();
        String sql = "SELECT dispositivo FROM dispositivosAgendamento WHERE agendamento_id = ?";

        try (PreparedStatement stmt = this.conexao.prepareStatement(sql)) {
            stmt.setInt(1, idAgendamento);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    dispositivos.add(rs.getString("dispositivo"));
                }
            }
        }

        // Converte List<Integer> para int[]
        return dispositivos;
    }

    /**
     * O método adicionarAgendamento insere um novo agendamento, seus dias da
     * semana e as salas associadas no banco de dados.
     *
     * @param agendamento, instância da classe Agendamento
     * @return V se o agendamento for bem sucedido e F caso falhe
     */
    public boolean adicionarAgendamento(Agendamento agendamento) {
        // 1. Inserir o Agendamento principal
        String sqlAgendamento = "INSERT INTO agendamento "
                + "(titulo, autor, dataInicio, dataFim, hAtiv, hDesat,statusAgendamento) "
                + "VALUES (?, ?, ?, ?, ?, ?,false)";

        try {
            // PreparedStatement com RETURN_GENERATED_KEYS para obter o ID gerado
            PreparedStatement stmt = this.conexao.prepareStatement(sqlAgendamento, PreparedStatement.RETURN_GENERATED_KEYS);

            // Conversão de Calendar para java.sql.Date
            java.sql.Date dataInicio = new java.sql.Date(agendamento.getDataIn().getTimeInMillis());
            java.sql.Date dataFim = new java.sql.Date(agendamento.getDataF().getTimeInMillis());

            stmt.setString(1, agendamento.getTitulo());
            stmt.setString(2, agendamento.getAutor());
            stmt.setDate(3, dataInicio);
            stmt.setDate(4, dataFim);
            stmt.setTime(5, agendamento.gethAtv());
            stmt.setTime(6, agendamento.gethDesat());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Falha ao criar o agendamento, nenhuma linha afetada.");
            }

            // 2. Obter o ID gerado
            int idAgendamento = -1;
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    idAgendamento = generatedKeys.getInt(1);
                    agendamento.setIdAgendamento(idAgendamento); // Atualiza o objeto Agendamento com o ID
                } else {
                    throw new SQLException("Falha ao criar o agendamento, não foi possível obter o ID.");
                }
            }
            stmt.close();

            // 3. Inserir dias da semana
            String sqlDias = "INSERT INTO diasDaSemana (agendamento_id, dia) VALUES (?, ?)";
            try (PreparedStatement stmtDias = conexao.prepareStatement(sqlDias)) {
                for (int dia : agendamento.getDiaSemana()) {
                    stmtDias.setInt(1, idAgendamento);
                    stmtDias.setInt(2, dia);
                    stmtDias.addBatch(); // Adiciona o comando ao lote
                }
                stmtDias.executeBatch(); // Executa todos os comandos no lote
            }

            // 4. Inserir salas
            String sqlSalas = "INSERT INTO nSalaAgendamento (agendamento_id, nSala) VALUES (?, ?)";
            try (PreparedStatement stmtSalas = conexao.prepareStatement(sqlSalas)) {
                for (int nSala : agendamento.getSalas()) {
                    stmtSalas.setInt(1, idAgendamento);
                    stmtSalas.setInt(2, nSala);
                    stmtSalas.addBatch(); // Adiciona o comando ao lote
                }
                stmtSalas.executeBatch(); // Executa todos os comandos no lote
            }

            String sqlDispositivos = "INSERT INTO dispositivosAgendamento (agendamento_id, dispositivo) VALUES (?, ?)";
            try (PreparedStatement stmtDispositivos = conexao.prepareStatement(sqlDispositivos)) {
                for (String dispositivo : agendamento.getDispositivos()) {
                    stmtDispositivos.setInt(1, idAgendamento);
                    stmtDispositivos.setString(2, dispositivo); // A coluna é VARCHAR, então use setString
                    stmtDispositivos.addBatch();
                }
                stmtDispositivos.executeBatch();
            }

            return true;

        } catch (SQLException e) {
            System.out.println("Erro ao adicionar agendamento: " + e.getMessage());
            // Em um ambiente de produção, você consideraria um rollback aqui se não estivesse usando autocommit.
            return false;
        }
    }

    // O objeto 'Agendamento' deve ser um objeto da sua aplicação
    // com getters e setters para:
    // getIdAgendamento(), getTitulo(), getAutor(), getDataIn(), getDataF(),
    // gethAtv(), gethDesat(), getDiaSemana(), getSalas().
    // Eu estou assumindo que a sua classe Agendamento tem esses métodos.
    public boolean atualizarAgendamento(Agendamento agendamento) {
        // É crucial que o objeto Agendamento tenha um ID válido para a atualização.
        if (agendamento.getIdAgendamento() <= 0) {
            System.out.println("Erro ao atualizar agendamento: O ID do agendamento é inválido." + agendamento.getIdAgendamento());
            return false;
        }

        // O ID do agendamento que será atualizado
        int idAgendamento = agendamento.getIdAgendamento();

        // 1. Atualizar o Agendamento principal (Tabela 'agendamento')
        // Usamos UPDATE e a cláusula WHERE com o ID.
        String sqlAgendamento = "UPDATE agendamento SET "
                + "titulo = ?, autor = ?, dataInicio = ?, dataFim = ?, hAtiv = ?, hDesat = ? "
                + "WHERE id = ?"; // Chave crucial

        try {
            // Uso de try-with-resources para garantir que o PreparedStatement seja fechado.
            try (PreparedStatement stmt = this.conexao.prepareStatement(sqlAgendamento)) {

                // Conversão de Calendar para java.sql.Date
                java.sql.Date dataInicio = new java.sql.Date(agendamento.getDataIn().getTimeInMillis());
                java.sql.Date dataFim = new java.sql.Date(agendamento.getDataF().getTimeInMillis());

                // Define os parâmetros da coluna
                stmt.setString(1, agendamento.getTitulo());
                stmt.setString(2, agendamento.getAutor());
                stmt.setDate(3, dataInicio);
                stmt.setDate(4, dataFim);
                stmt.setTime(5, agendamento.gethAtv());
                stmt.setTime(6, agendamento.gethDesat());

                // Define o parâmetro do WHERE
                stmt.setInt(7, idAgendamento);

                int affectedRows = stmt.executeUpdate();

                if (affectedRows == 0) {
                    // Isso pode indicar um erro ou que o agendamento com esse ID não existe.
                    System.out.println("Aviso: Agendamento com ID " + idAgendamento + " não encontrado ou nenhum dado alterado.");
                    // Aqui você pode decidir se retorna true (se não mudar nada não é um erro fatal) ou false.
                    // Vou manter o fluxo para atualizar os detalhes, mas o aviso é importante.
                }
            } // stmt é fechado automaticamente aqui

            // As etapas 2 e 3 (dias da semana e salas) são de **1:N**. 
            // A melhor prática para atualização é **apagar os antigos e inserir os novos**.
            // 2. Excluir dias da semana antigos e Inserir novos (Tabela 'diasDaSemana')
            // a. Exclusão
            String sqlDeleteDias = "DELETE FROM diasDaSemana WHERE agendamento_id = ?";
            try (PreparedStatement stmtDeleteDias = conexao.prepareStatement(sqlDeleteDias)) {
                stmtDeleteDias.setInt(1, idAgendamento);
                stmtDeleteDias.executeUpdate();
            }

            // b. Inserção
            String sqlInsertDias = "INSERT INTO diasDaSemana (agendamento_id, dia) VALUES (?, ?)";
            try (PreparedStatement stmtInsertDias = conexao.prepareStatement(sqlInsertDias)) {
                for (int dia : agendamento.getDiaSemana()) {
                    stmtInsertDias.setInt(1, idAgendamento);
                    stmtInsertDias.setInt(2, dia);
                    stmtInsertDias.addBatch();
                }
                stmtInsertDias.executeBatch();
            }

            // 3. Excluir salas antigas e Inserir novas (Tabela 'nSalaAgendamento')
            // a. Exclusão
            String sqlDeleteSalas = "DELETE FROM nSalaAgendamento WHERE agendamento_id = ?";
            try (PreparedStatement stmtDeleteSalas = conexao.prepareStatement(sqlDeleteSalas)) {
                stmtDeleteSalas.setInt(1, idAgendamento);
                stmtDeleteSalas.executeUpdate();
            }

            // b. Inserção
            String sqlInsertSalas = "INSERT INTO nSalaAgendamento (agendamento_id, nSala) VALUES (?, ?)";
            try (PreparedStatement stmtInsertSalas = conexao.prepareStatement(sqlInsertSalas)) {
                for (int nSala : agendamento.getSalas()) {
                    stmtInsertSalas.setInt(1, idAgendamento);
                    stmtInsertSalas.setInt(2, nSala);
                    stmtInsertSalas.addBatch();
                }
                stmtInsertSalas.executeBatch();
            }

            String sqlDeletDispositivos = "DELETE FROM dispositivosAgendamento WHERE agendamento_id = ?";
            try (PreparedStatement stmtDeleteSalas = conexao.prepareStatement(sqlDeletDispositivos)) {
                stmtDeleteSalas.setInt(1, idAgendamento);
                stmtDeleteSalas.executeUpdate();
            }
            String sqlInsertDispositivos = "INSERT INTO dispositivosAgendamento (agendamento_id, dispositivo) VALUES (?, ?)";
            try (PreparedStatement stmtDispositivos = conexao.prepareStatement(sqlInsertDispositivos)) {
                for (String dispositivo : agendamento.getDispositivos()) {
                    stmtDispositivos.setInt(1, idAgendamento);
                    stmtDispositivos.setString(2, dispositivo); // A coluna é VARCHAR, então use setString
                    stmtDispositivos.addBatch();
                }
                stmtDispositivos.executeBatch();
            }

            return true;

        } catch (SQLException e) {
            System.out.println("Erro ao atualizar agendamento: " + e.getMessage());
            // Considere um rollback se estiver usando transações manuais.
            return false;
        }
    }

    // Dentro de DAOManager.java
    public int getIdUserByLogin(String login) {
        String sql = "SELECT idUser FROM user WHERE login = ?";
        try (PreparedStatement stmt = this.conexao.prepareStatement(sql)) {
            stmt.setString(1, login);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("idUser");
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar ID do usuário: " + e.getMessage());
        }
        return -1; // Retorna -1 ou lança uma exceção se o usuário não for encontrado
    }

    /**
     * Atualiza o estado de um dispositivo específico (Luzes, Ar, DataShow) na
     * Sala.
     *
     * @param nSala O número da sala.
     * @param dispositivo O código do dispositivo (ex: "LZ", "AR", "DS").
     * @param estado True para ligar, False para desligar.
     * @return True se a atualização for bem-sucedida.
     */
    public boolean atualizarEstadoDispositivo(int nSala, String dispositivo, boolean estado) {
        String campo;

        switch (dispositivo) {
            case "LZ":
                campo = "est_Luzes";
                break;
            case "AR":
                campo = "est_Ar";
                break;
            case "DS":
                campo = "est_datashow";
                break;
            default:
                System.err.println("Dispositivo desconhecido: " + dispositivo);
                return false;
        }

        String sql = "UPDATE sala SET " + campo + "=? WHERE nSala=?";

        try (PreparedStatement stmt = this.conexao.prepareStatement(sql)) {
            stmt.setBoolean(1, estado);
            stmt.setInt(2, nSala);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar o estado do dispositivo: " + e.getMessage());
            return false;
        }
    }
    

    /**
     * Insere um novo dispositivo na tabela 'dis', seus códigos IR em 'codir', e
     * suas salas relacionadas em 'conjuntodis' (antiga nsaladispositivo).
     */
    public boolean inserirDispositivo(Dispositivo dispositivo) {
        String sqlDis = "INSERT INTO dis (tipo, modelo, marca, config) VALUES (?, ?, ?, ?)";
        String sqlCodIr = "INSERT INTO codir (cod, funcao, dispositivo_id) VALUES (?, ?, ?)";
        // MUDANÇA AQUI: Tabela 'conjuntodis' com colunas 'dis_id' e 'sala_id'
        String sqlSalas = "INSERT INTO conjuntodis (dis_id, sala_id) VALUES (?, ?)";

        try {
            conexao.setAutoCommit(false);

            // 1. Inserir o Dispositivo (tabela 'dis')
            int idDispositivoGerado = -1;
            try (PreparedStatement stmtDis = conexao.prepareStatement(sqlDis, PreparedStatement.RETURN_GENERATED_KEYS)) {
                stmtDis.setString(1, dispositivo.getTipo());
                stmtDis.setString(2, dispositivo.getModelo());
                stmtDis.setString(3, dispositivo.getMarca());
                stmtDis.setString(4, dispositivo.getConfig());

                if (stmtDis.executeUpdate() == 0) {
                    throw new SQLException("Falha ao inserir dispositivo, nenhuma linha afetada.");
                }

                try (ResultSet generatedKeys = stmtDis.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        idDispositivoGerado = generatedKeys.getInt(1);
                    } else {
                        throw new SQLException("Falha ao inserir dispositivo, não foi possível obter o ID.");
                    }
                }
            }

            // 2. Inserir os Códigos IR (tabela 'codir')
            // Nota: 'codir' usa a coluna 'dispositivo_id' conforme seu SQL dump
            if (dispositivo.getListaCodigos() != null && !dispositivo.getListaCodigos().isEmpty()) {
                try (PreparedStatement stmtCodIr = conexao.prepareStatement(sqlCodIr)) {
                    for (CodIr codIr : dispositivo.getListaCodigos()) {
                        stmtCodIr.setString(1, codIr.getCod());
                        stmtCodIr.setString(2, codIr.getFuncao());
                        stmtCodIr.setInt(3, idDispositivoGerado);
                        stmtCodIr.addBatch();
                    }
                    stmtCodIr.executeBatch();
                }
            }

            // 3. INSERIR AS SALAS RELACIONADAS (tabela 'conjuntodis')
            if (dispositivo.getSalasRelacionadas() != null && dispositivo.getSalasRelacionadas().length > 0) {
                try (PreparedStatement stmtSalas = conexao.prepareStatement(sqlSalas)) {
                    for (int nSala : dispositivo.getSalasRelacionadas()) {
                        stmtSalas.setInt(1, idDispositivoGerado); // dis_id
                        stmtSalas.setInt(2, nSala);               // sala_id
                        stmtSalas.addBatch();
                    }
                    stmtSalas.executeBatch();
                }
            }

            conexao.commit();
            return true;

        } catch (SQLException e) {
            System.out.println("Erro na transação de inserção: " + e.getMessage());
            try {
                if (conexao != null) conexao.rollback();
            } catch (SQLException ex) {
                System.out.println("Erro ao rollback: " + ex.getMessage());
            }
            return false;
        } finally {
            try {
                if (conexao != null) conexao.setAutoCommit(true);
            } catch (SQLException ex) {
                // Logar erro
            }
        }
    }

    /**
     * Deleta um dispositivo. O CASCADE configurado no banco em 'codir' e 'conjuntodis'
     * fará a limpeza automática das tabelas filhas.
     */
    public boolean deletarDispositivo(int idDispositivo) {
        String sql = "DELETE FROM dis WHERE id = ?";

        try (PreparedStatement stmt = this.conexao.prepareStatement(sql)) {
            stmt.setInt(1, idDispositivo);
            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;

        } catch (SQLException ex) {
            System.err.println("Erro ao deletar dispositivo (ID: " + idDispositivo + "): " + ex.getMessage());
            return false;
        }
    }

    /**
     * Atualiza um dispositivo existente e suas relações.
     */
    public boolean atualizarDispositivo(Dispositivo dispositivo) {
        if (dispositivo.getId() <= 0) {
            System.err.println("Erro ao atualizar: ID inválido.");
            return false;
        }

        String sqlUpdateDis = "UPDATE dis SET tipo=?, modelo=?, marca=?, config=? WHERE id=?";
        
        // CodIr continua igual
        String sqlDeleteCodIr = "DELETE FROM codir WHERE dispositivo_id=?";
        String sqlInsertCodIr = "INSERT INTO codir (cod, funcao, dispositivo_id) VALUES (?, ?, ?)";

        // MUDANÇA: Usando 'conjuntodis' com 'dis_id'
        String sqlDeleteSalas = "DELETE FROM conjuntodis WHERE dis_id=?";
        String sqlInsertSalas = "INSERT INTO conjuntodis (dis_id, sala_id) VALUES (?, ?)";

        try {
            conexao.setAutoCommit(false);
            int idDispositivo = dispositivo.getId();

            // 1. Atualizar Dispositivo
            try (PreparedStatement stmtDis = conexao.prepareStatement(sqlUpdateDis)) {
                stmtDis.setString(1, dispositivo.getTipo());
                stmtDis.setString(2, dispositivo.getModelo());
                stmtDis.setString(3, dispositivo.getMarca());
                stmtDis.setString(4, dispositivo.getConfig());
                stmtDis.setInt(5, idDispositivo);
                stmtDis.executeUpdate();
            }

            // 2. Atualizar Códigos IR
            try (PreparedStatement stmtDeleteCodIr = conexao.prepareStatement(sqlDeleteCodIr)) {
                stmtDeleteCodIr.setInt(1, idDispositivo);
                stmtDeleteCodIr.executeUpdate();
            }
            if (dispositivo.getListaCodigos() != null && !dispositivo.getListaCodigos().isEmpty()) {
                try (PreparedStatement stmtInsertCodIr = conexao.prepareStatement(sqlInsertCodIr)) {
                    for (CodIr codIr : dispositivo.getListaCodigos()) {
                        stmtInsertCodIr.setString(1, codIr.getCod());
                        stmtInsertCodIr.setString(2, codIr.getFuncao());
                        stmtInsertCodIr.setInt(3, idDispositivo);
                        stmtInsertCodIr.addBatch();
                    }
                    stmtInsertCodIr.executeBatch();
                }
            }

            // 3. Atualizar Salas ('conjuntodis')
            try (PreparedStatement stmtDeleteSalas = conexao.prepareStatement(sqlDeleteSalas)) {
                stmtDeleteSalas.setInt(1, idDispositivo); // dis_id
                stmtDeleteSalas.executeUpdate();
            }
            if (dispositivo.getSalasRelacionadas() != null && dispositivo.getSalasRelacionadas().length > 0) {
                try (PreparedStatement stmtInsertSalas = conexao.prepareStatement(sqlInsertSalas)) {
                    for (int nSala : dispositivo.getSalasRelacionadas()) {
                        stmtInsertSalas.setInt(1, idDispositivo); // dis_id
                        stmtInsertSalas.setInt(2, nSala);         // sala_id
                        stmtInsertSalas.addBatch();
                    }
                    stmtInsertSalas.executeBatch();
                }
            }

            conexao.commit();
            return true;

        } catch (SQLException e) {
            System.err.println("Erro na atualização: " + e.getMessage());
            try {
                if (conexao != null) conexao.rollback();
            } catch (SQLException ex) {
                System.err.println("Erro rollback: " + ex.getMessage());
            }
            return false;
        } finally {
            try {
                if (conexao != null) conexao.setAutoCommit(true);
            } catch (SQLException ex) {}
        }
    }

    public List<Dispositivo> consultarDispositivos() {
        List<Dispositivo> dispositivos = new ArrayList<>();
        String sqlDis = "SELECT * FROM dis";

        try (PreparedStatement stmtDis = this.conexao.prepareStatement(sqlDis); ResultSet rsDis = stmtDis.executeQuery()) {

            while (rsDis.next()) {
                Dispositivo dis = new Dispositivo();
                int idDispositivo = rsDis.getInt("id");

                dis.setId(idDispositivo);
                dis.setTipo(rsDis.getString("tipo"));
                dis.setModelo(rsDis.getString("modelo"));
                dis.setMarca(rsDis.getString("marca"));
                dis.setConfig(rsDis.getString("config"));

                dis.setListaCodigos(consultarCodIrPorDispositivoId(idDispositivo));
                dis.setSalasRelacionadas(consultarSalasRelacionadas(idDispositivo));

                dispositivos.add(dis);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao consultar dispositivos: " + e.getMessage());
        }
        return dispositivos;
    }

    // --- Métodos Auxiliares ---

    /**
     * Método auxiliar corrigido para ler da tabela 'conjuntodis'
     */
    private int[] consultarSalasRelacionadas(int dispositivoId) throws SQLException {
        List<Integer> salas = new ArrayList<>();
        // MUDANÇA: Selecionar 'sala_id' de 'conjuntodis' onde 'dis_id' for igual ao parametro
        String sqlSalas = "SELECT sala_id FROM conjuntodis WHERE dis_id = ?";

        try (PreparedStatement stmtSalas = this.conexao.prepareStatement(sqlSalas)) {
            stmtSalas.setInt(1, dispositivoId);

            try (ResultSet rsSalas = stmtSalas.executeQuery()) {
                while (rsSalas.next()) {
                    salas.add(rsSalas.getInt("sala_id"));
                }
            }
        }
        return salas.stream().mapToInt(i -> i).toArray();
    }
    
    // O método consultarCodIrPorDispositivoId permanece inalterado, pois a tabela 'codir' 
    // e suas colunas (dispositivo_id) não mudaram no SQL.
    private List<CodIr> consultarCodIrPorDispositivoId(int dispositivoId) throws SQLException {
       // ... (código existente permanece válido)
       List<CodIr> codigosIr = new ArrayList<>();
       String sqlCodIr = "SELECT id, cod, funcao, dispositivo_id FROM codir WHERE dispositivo_id = ?";
       
       try (PreparedStatement stmtCodIr = this.conexao.prepareStatement(sqlCodIr)) {
           stmtCodIr.setInt(1, dispositivoId);
           try (ResultSet rsCodIr = stmtCodIr.executeQuery()) {
               while (rsCodIr.next()) {
                   CodIr codIr = new CodIr();
                   codIr.setId(rsCodIr.getInt("id"));
                   codIr.setCod(rsCodIr.getString("cod"));
                   codIr.setFuncao(rsCodIr.getString("funcao"));
                   codIr.setDispositivo_id(rsCodIr.getInt("dispositivo_id"));
                   codigosIr.add(codIr);
               }
           }
       }
       return codigosIr;
    }

    
}
