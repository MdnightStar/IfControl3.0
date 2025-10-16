/**
 *
 */
package Controle;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import Modelo.*;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 * @author Jeison
 */
public class TratarAcao extends SocketArduino {

    private String _login;
    private String _senha;
    private DAOManager manager;
    private User user;
    private Acao acao;
    private Sala sala;
    private Gson gson;
    

    /**
     * Construtor da clase, somente instância o gson
     */
    public TratarAcao() {
        gson = new Gson();
    }

    /**
     * O método modifica o DAO manager
     *
     * @param manager
     */
    public void setManager(DAOManager manager) {
        this.manager = manager;
    }

    /**
     * Esse método retona V ou F se um determinado login estiver no banco de
     * dados
     *
     * @return Retorna uma mensagens, com um padrã determinado
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
     * Esse método retona uma mensagens que informa alguma erro ou sucesso ao
     * cadastrar um usúario, verficando o siap, o login, e se foi possivel
     * inserir no BD
     *
     * @param siap
     * @param nome
     * @param login
     * @param senha
     * @return Retorna uma mensagens, com um formato determinado
     */
    public String cadastrarUser(Long siap, String nome, String login, String senha) {
        if (!manager.siapExiste(siap)) {
            return "INVALID_SIAP";
        } else if (manager.loginExiste(login)) {
            return "INVALID_LOGIN";
        } else {
            user = new User();
            user.setLogin(login);
            user.setNome(nome);
            user.setSiap(siap);
            user.setSenha(senha);
            if (manager.inserirUser(user)) {
                return "CAD_OK";
            } else {
                return "ERROR_BD_INSERT";
            }
        }
    }
    
    public String cadastrarSala(int nSala, String IP){
        if(manager.NSalaexiste(nSala)){
            return "INVALID_NSALA";
        }else if(manager.IPexiste(IP)){
            return "INVALID_IP";
        }else{
            sala = new Sala(nSala, IP);
            if(manager.adicionarSala(nSala, IP)){
                return "CAD_SALA_OK";
            }else{
                return "ERROR_BD_INSERT";
            }
        }
    }

    /**
     * Esse método retorna a lista de ações no formato JSON, ou seja, serializa
     * as ações para poder colocar na interface
     *
     * @return Retorna uma mensagens, com um formato determinado
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
     * Esse método retorna a lista de salas no formato JSON, ou seja, serializa
     * as ações para poder colocar na interface
     *
     * @return Retorna uma mensagens, com um formato determinado
     */
    public String pegarSalas() {
        List<Sala> salas = manager.consultarSalas();
        if (salas != null) {
            return gson.toJson(salas);
        } else {
            return "ERROR_SELECT_LOGS";
        }
    }

    /**
     * Esse método verifica se a sala esta ocupada ou não;
     *
     * @param nSala
     * @return Retorna uma mensagens, com um formato determinado
     */
    public String estadoSala(int nSala) {
        sala = new Sala();
        sala = manager.procuraSala(nSala); //pega a sala do BD
        if (sala.isEstadoSala()) {
            return ("OCUPADA : SALA:" + nSala);
        } else {
            return ("LIVRE: SALA:" + nSala);
        }
    }

    /**
     * Esse método verifica se a sala esta ocupada ou não;
     *
     * @param nSala
     * @return Retorna uma mensagens, com um formato determinado
     */
    public String tratarAcao(String acao, int nSala) throws ParseException {
        this.acao = new Acao(); // zera a ação do método
        sala = new Sala();
        String resposta = "";

        //Preenche dados da ação do metodo
        this.acao.setTipoAcao(acao);
        this.acao.setIdUser(user.getIdUser()); //seta id do usuario da classe user
        this.acao.setnSala(nSala); //seta o numero da sala do metodo
        this.acao.setLogin(_login); //seta o login desta classe sessao

        sala = manager.procuraSala(nSala); // pega a sala do BD

        //Muda o estado da sala e da ação a partir da mensagem enviada
        if ((acao.contains("OCP")) || (acao.contains("DSC"))) {
            if (acao.contains("OCP")) {
                sala.setEstadoSala(true);
            } else {
                sala.setEstadoSala(false);
            }
        } else {
            //Caso seja outra tipo de ação, que não contem HD nem HA
            if (!acao.contains("HD") && !acao.contains("HA")) {
                if (acao.contains("DS") || acao.contains("AR")) {
                    //Recolhe a o codIr da determinada ação
                    try {
                        StringBuilder newAcao = manager.resgataCodIr(nSala, acao);
                        //Solicita a ação no arduino
                        conectarArduino(nSala);
                        enviar(newAcao.toString());
                        resposta = ler();//recebe um "Ok" do arduino se recebeu
                        desconectarArduino();
                    } catch (SQLException e) {
                        JOptionPane.showMessageDialog(null, "Não foi possivel recoletar o cod IR do Banco de Dados",
                                "ERRO", JOptionPane.ERROR_MESSAGE);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null, "Não foi se conectar com o arduino",
                                "ERRO", JOptionPane.ERROR_MESSAGE);
                    }

                    if (resposta.equals("OK")) {  //confirmaçao da açao
                        this.acao.setStatus(true);
                    } else {
                        this.acao.setStatus(false);
                    }
                    //Até essa parte do código, se envia uma ação para o arduino e 
                    //verifica o recebimento
                }

                switch (acao) {
                    case "DSON.":
                        sala.setEstadoDataShow(true);
                        break;
                    case "DSOFF.":
                        sala.setEstadoDataShow(false);
                        break;
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
            //Ajusta o horario de ativação e desativação da sala
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
        }
        //Insere novo registro de acao e atualiza dados da sala no BD sao tudo
        if (manager.inserirAcao(this.acao) && manager.alteraSala(sala)) {
            return "ACAO_OK";
        } else if (manager.inserirAcao(this.acao)) {
            return "ERROR_BD_INSERT";
        } else {
            return "ERROR_BD_UPDATE";
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
