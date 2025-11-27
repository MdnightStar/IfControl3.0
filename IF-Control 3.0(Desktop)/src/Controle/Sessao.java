/*
 *
 */
package Controle;

import Modelo.Acao;
import com.google.gson.Gson;
import java.io.IOException;
import Modelo.User;
import java.sql.Time;
import java.time.LocalTime;
import java.util.Calendar;

/**
 * @author Jeison
 */
public class Sessao {

    private String _login;
    private String _senha;
    private TrataServidor servidor;

    public Sessao() {

    }

    public String getLogin() {
        return _login;
    }

    public String getSenha() {
        return _senha;
    }

    public void setLogin(String _login) {
        this._login = _login;
    }

    public void setSenha(String _senha) {
        this._senha = _senha;
    }

    /**
     * Instancia o socket que representa o usúario e espera ele enviar uma
     * mensagem, logo ápos zera a String resposta para esperar uma nova mensagem
     *
     * @return
     */
    public boolean iniciarSessao() {
        try {
            servidor = new TrataServidor();
            servidor.conectar();
            System.out.println("Sessao conectada");
            new Thread(servidor).start();
            servidor.setResposta(null);
            return true;
        } catch (IOException ex) {
            System.out.println("Não foi possivel se conectar com o cliente: " + ex);
            return false;
        }
    }

    /**
     * Encerra a conexao do cliente com o servidor
     *
     */
    public void encerrarSessao() {
        System.out.println("ENCERRANDO SESSAO");
        try {
            servidor.desconectar();
        } catch (IOException ex) {
            System.out.println("Erro ao se desconectar: " + ex);
        }
    }

    /**
     * Efetua o login do cliente, enviando para o TrataCliente
     *
     * @param login
     * @param senha
     * @return
     */
    public String login(String login, String senha) {
        servidor.enviar("{\"_login\":\"" + login
                + "\",\"_senha\":\"" + senha
                + "\"}");
        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            System.out.println("Erro ao executar o TrataCliente");
        }
        this._login = login;
        this._senha = senha;
        return servidor.getResposta();
    }

    /**
     * Envia o user serializado para o TrataCliente, assim efetuando o cadastro
     *
     * @param user
     * @return
     */
    public String cadastrar(User user) {

        Gson gson = new Gson();
        String u = gson.toJson(user);

        servidor.enviar(u);
        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            System.out.println("Erro ao executar o TrataCliente");
        }
        return servidor.getResposta();
    }

    /**
     * Envia uma Acao já serializada para o TrataCliente, assim cadastrando a
     * ação no BD
     *
     * @param acao
     */
    public String trataAcao(String acao) {
        Acao a = new Acao();
        a.setLogin(_login);
        a.setDataAcao(Calendar.getInstance());
        a.setHoraAcao(Time.valueOf(LocalTime.now()));
        a.setTipoAcao(acao);
        Gson gson = new Gson();
        String u = "--acao--";
        u += gson.toJson(a);

        System.out.println(u);
        servidor.enviar(u);
        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            System.out.println("Erro ao executar o TrataCliente");
        }
        return servidor.getResposta();

    }

    /**
     * Envia uma Acao já serializada para o TrataCliente, assim cadastrando a
     * ação no BD
     *
     * @param acao
     */
    public String trataAcao(String acao, int nSala) {
        Acao a = new Acao();
        a.setLogin(_login);
        a.setDataAcao(Calendar.getInstance());
        a.setHoraAcao(Time.valueOf(LocalTime.now()));
        a.setTipoAcao(acao);
        a.setnSala(nSala);
        Gson gson = new Gson();
        String u = "--acaoSala--";
        u += gson.toJson(a);

        System.out.println(u);
        servidor.enviar(u);
        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            System.out.println("Erro ao executar o TrataCliente");
        }
        return servidor.getResposta();
    }

    /**
     * Envia um pedido de verificação se a sala esta ocupada ou não, para o
     * TrataCliente
     *
     * @param sala
     */
    public void statusSala(String sala) {
        System.out.println(sala);
        servidor.enviar("start;" + sala);

    }

    /**
     * Verifica o que o servidor respondeu refernte a ação executada
     *
     * @return
     */
    public String verificarResposta() {
        return servidor.getResposta();
    }

    /**
     * Pede para o TrataCliente atualizar as salas
     *
     */
    public void atualizar() {
        servidor.enviar("atualizar");
    }

    /**
     * Pede para o TrataCliente enviar todas as ações serializadas
     *
     */
    public String logs() {
        servidor.enviar("logs");
        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            System.out.println("Erro ao executar o TrataCliente");
        }
        return servidor.getResposta();
    }

    /**
     * Pede para o TrataCliente enviar todas as salas serializadas
     *
     */
    public String salas() {
        servidor.enviar("salas");
        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            System.out.println("Erro ao executar o TrataCliente");
        }
        return servidor.getResposta();
    }

    public String getSala(int nSala) {
        servidor.enviar("--getSala--" + nSala);
        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            System.out.println("Erro ao executar o TrataCliente");
        }
        return servidor.getResposta();

    }

    public String agendamentos() {
        servidor.enviar("--agendamentos--");
        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            System.out.println("Erro ao executar o TrataCliente");
        }
        return servidor.getResposta();
    }

    public String dispositivos() {
        servidor.enviar("--dispositivos--");
        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            System.out.println("Erro ao executar o TrataCliente");
        }
        return servidor.getResposta();
    }
}
