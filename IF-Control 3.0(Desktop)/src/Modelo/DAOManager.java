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
     * determinada ação solicitada, no formato:
     * "msg.config([000,000,000,000,000, 000],cod(HHHh11011101))"
     *
     * @param nSala
     * @param msg
     * @return Retorna uma StringBuilder com a msg modificada
     * @throws SQLException
     */
    public StringBuilder resgataCodIr(int nSala, String msg) throws SQLException {
        StringBuilder resp = new StringBuilder();

        resp.append("DIR,");

        // Extrair tipo (2 primeiros caracteres) e função (até o ponto final, se houver)
        String tipo = msg.substring(0, 2);
        String funcao = msg.contains(".") ? msg.substring(2, msg.indexOf(".")) : msg.substring(2);

        // 1. Buscar dispositivos da sala com o tipo especificado
        String sqlDis = "SELECT id, config FROM dis WHERE sala_id = ? AND tipo = ?";
        try (PreparedStatement stmt = this.conexao.prepareStatement(sqlDis); ResultSet rs = stmt.executeQuery()) {
            stmt.setInt(1, nSala);
            stmt.setString(2, tipo);

            while (rs.next()) {
                int disId = rs.getInt("id");
                String config = rs.getString("config");

                // 3.1 Adicionar config ao StringBuilder
                resp.append(config).append(",");

                // 4. Buscar código IR correspondente à função
                String sqlCodIr = "SELECT cod FROM codIR WHERE dispositivo_id = ? AND funcao = ?";
                try (PreparedStatement stmt2 = this.conexao.prepareStatement(sqlCodIr); ResultSet rs2 = stmt2.executeQuery()) {
                    stmt2.setInt(1, disId);
                    stmt2.setString(2, funcao);

                    if (rs2.next()) {
                        String cod = rs2.getString("cod");
                        // 5. Adicionar cod ao StringBuilder
                        resp.append(cod).append(".");
                    }
                } catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        if(resp.toString().contains(".")){
            return resp;
        }else {
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
            System.out.println("Erro ao atualizar agendamento: O ID do agendamento é inválido."+agendamento.getIdAgendamento());
            return false;
        }

        // O ID do agendamento que será atualizado
        int idAgendamento = agendamento.getIdAgendamento();

        // 1. Atualizar o Agendamento principal (Tabela 'agendamento')
        // Usamos UPDATE e a cláusula WHERE com o ID.
        String sqlAgendamento = "UPDATE agendamento SET "
                + "titulo = ?, autor = ?, dataInicio = ?, dataFim = ?, hAtiv = ?, hDesat = ? , statusAgendamento=false"
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
}
