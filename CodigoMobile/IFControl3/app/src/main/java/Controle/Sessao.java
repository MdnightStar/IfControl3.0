/*
 * Versão unificada e aprimorada da classe Sessao.
 * Combina a robustez da lógica de negócio da versão de referência
 * com a compatibilidade e boas práticas do ambiente Android.
 */
package Controle;

import android.util.Log;
import com.google.gson.Gson;
import com.google.type.Date;

import java.sql.Time;
import java.time.LocalTime;
import java.util.Calendar;
import Modelo.Acao;
import Modelo.Sala;
import Modelo.User;

public class Sessao {

    private static final String TAG = "Sessao";

    private String _login;
    private String _senha;
    private TrataServidor servidor;

    public Sessao() {}

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
     * Inicia a sessão e conecta ao servidor de forma assíncrona.
     * Retorna true se a conexão inicial for estabelecida.
     */
    public boolean iniciarSessao() {
        try {
            servidor = new TrataServidor();
            Log.i(TAG, "Sessão iniciando... aguardando conexão.");

            // Aguarda um tempo para o socket conectar em sua thread.
            Thread.sleep(500);
            if (servidor.conectado) {
                new Thread(servidor).start();
                servidor.setResposta(null);
                Log.i(TAG, "Sessão conectada com sucesso.");
                return true;
            } else {
                Log.e(TAG, "Falha ao conectar com o servidor.");
                return false;
            }
        } catch (Exception ex) {
            Log.e(TAG, "Erro ao iniciar sessão: " + ex.getMessage());
            return false;
        }
    }

    /**
     * Encerra a conexão com o servidor.
     */
    public void encerrarSessao() {
        Log.i(TAG, "Encerrando sessão...");
        if (servidor != null) {
            servidor.desconectar();
        }
    }

    /**
     * Efetua o login do cliente, enviando JSON para o servidor.
     */
    public String login(String login, String senha) {
        this._login = login; // Armazena o login para uso futuro em 'trataAcao'
        this._senha = senha;
        servidor.enviar("{\"_login\":\"" + login + "\",\"_senha\":\"" + senha + "\"}");

        try {
            Thread.sleep(200); // Aguarda resposta do servidor
        } catch (InterruptedException ex) {
            Log.w(TAG, "Thread.sleep interrompida durante o login.", ex);
        }
        return servidor.getResposta();
    }

    /**
     * Envia o usuário serializado para cadastro.
     */
    public String cadastrar(User user) {
        Gson gson = new Gson();
        String u = gson.toJson(user);
        servidor.enviar(u);

        try {
            Thread.sleep(200);
        } catch (InterruptedException ignored) {}

        return servidor.getResposta();
    }

    /**
     * Envia uma Acao genérica para o servidor, com detalhes de log.
     */
    public String trataAcao(String acao) {
        Acao a = new Acao();
        a.setLogin(this._login);
        a.setDataAcao(Calendar.getInstance());
        a.setHoraAcao(new Time(System.currentTimeMillis()));
        a.setTipoAcao(acao);

        Gson gson = new Gson();
        String jsonAcao = gson.toJson(a);
        String mensagem = "--acao--" + jsonAcao;

        Log.i(TAG, "Enviando ação: " + mensagem);
        servidor.enviar(mensagem);

        try {
            Thread.sleep(200);
        } catch (InterruptedException ex) {
            Log.w(TAG, "Thread.sleep interrompida em trataAcao.", ex);
        }
        return servidor.getResposta();
    }

    /**
     * Envia uma Acao específica de uma sala para o servidor.
     */
    public String trataAcao(String acao, int nSala) {
        Acao a = new Acao();
        a.setLogin(this._login);
        a.setDataAcao(Calendar.getInstance());
        a.setHoraAcao(new Time(System.currentTimeMillis()));
        a.setTipoAcao(acao);
        a.setnSala(nSala);

        Gson gson = new Gson();
        String jsonAcao = gson.toJson(a);
        String mensagem = "--acaoSala--" + jsonAcao;

        Log.i(TAG, "Enviando ação de sala: " + mensagem);
        servidor.enviar(mensagem);
        try {
            Thread.sleep(200);
        } catch (InterruptedException ex) {
            Log.w(TAG, "Thread.sleep interrompida em trataAcao de sala.", ex);
        }
        return servidor.getResposta();
    }

    /**
     * Solicita status de uma sala específica.
     */
    public void statusSala(String sala) {
        Log.i(TAG, "Solicitando status da sala: " + sala);
        servidor.enviar("start;" + sala);
    }

    /**
     * Retorna a última resposta recebida do servidor.
     */
    public String verificarResposta() {
        return servidor.getResposta();
    }

    /**
     * Solicita ao servidor para atualizar as salas.
     */
    public void atualizar() {
        Log.i(TAG, "Solicitando atualização das salas.");
        servidor.enviar("atualizar");
    }

    /**
     * Solicita os logs (ações) do servidor.
     */
    public String logs() {
        Log.i(TAG, "Solicitando logs.");
        servidor.enviar("logs");
        try {
            Thread.sleep(200);
        } catch (InterruptedException ex) {
            Log.w(TAG, "Thread.sleep interrompida em logs.", ex);
        }
        return servidor.getResposta();
    }

    /**
     * Solicita a lista de todas as salas.
     */
    public String salas() {
        Log.i(TAG, "Solicitando lista de salas.");
        servidor.enviar("salas");
        try {
            Thread.sleep(200);
        } catch (InterruptedException ex) {
            Log.w(TAG, "Thread.sleep interrompida em salas.", ex);
        }
        return servidor.getResposta();
    }

    /**
     * Solicita dados de uma sala específica.
     */
    public String getSala(int nSala) {
        Log.i(TAG, "Solicitando dados da sala: " + nSala);
        servidor.enviar("--getSala--" + nSala);
        try {
            Thread.sleep(200);
        } catch (InterruptedException ex) {
            Log.w(TAG, "Thread.sleep interrompida em getSala.", ex);
        }
        return servidor.getResposta();
    }

    /**
     * Adiciona uma nova sala.
     */
    public String addSala(Sala sala) {
        Gson gson = new Gson();
        String jsonSala = gson.toJson(sala);
        servidor.enviar("sala:--addSala--" + jsonSala);

        try {
            Thread.sleep(200);
        } catch (InterruptedException ignored) {}

        return servidor.getResposta();
    }

    /**
     * Solicita a lista de todos os agendamentos.
     */
    public String agendamentos() {
        Log.i(TAG, "Solicitando agendamentos.");
        servidor.enviar("--agendamentos--");
        try {
            Thread.sleep(200);
        } catch (InterruptedException ex) {
            Log.w(TAG, "Thread.sleep interrompida em agendamentos.", ex);
        }
        return servidor.getResposta();
    }

    /**
     * Solicita a lista de todos os dispositivos.
     */
    public String dispositivos() {
        Log.i(TAG, "Solicitando dispositivos.");
        servidor.enviar("--dispositivos--");
        try {
            Thread.sleep(200);
        } catch (InterruptedException ex) {
            Log.w(TAG, "Thread.sleep interrompida em dispositivos.", ex);
        }
        return servidor.getResposta();
    }
}

