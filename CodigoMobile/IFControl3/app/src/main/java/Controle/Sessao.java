/*
 *
 */
package Controle;


import android.util.Log;

import com.google.gson.Gson;
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
     * Inicia a sessão e conecta ao servidor
     */
    public boolean iniciarSessao() {
        try {
            servidor = new TrataServidor();
            Log.i(TAG, "Sessão iniciando... aguardando conexão");

            // Espera alguns milissegundos até o socket conectar (em thread separada)
            Thread.sleep(500);
            if(servidor.conectado) {
                new Thread(servidor).start();
                servidor.setResposta(null);

                Log.i(TAG, "Sessão conectada com sucesso.");
                return true;
            }else{
                return false;
            }

        } catch (Exception ex) {
            Log.e(TAG, "Erro ao iniciar sessão: " + ex.getMessage());
            return false;
        }
    }

    /**
     * Encerra a sessão com o servidor
     */

    public void encerrarSessao() {
        Log.i(TAG, "Encerrando sessão...");
        if (servidor != null) {
            servidor.desconectar();
        }
    }

    /**
     * Efetua login enviando JSON com login e senha
     */
    public String login(String login, String senha) {
        servidor.enviar("{\"_login\":\"" + login
                + "\",\"_senha\":\"" + senha + "\"}");

        // Aguarda resposta
        try {
            Thread.sleep(200);
        } catch (InterruptedException ignored) {}

        return servidor.getResposta();
    }

    /**
     * Cadastra um usuário enviando JSON serializado
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
     * Envia uma ação genérica para o servidor
     */
    public void trataAcao(String acao) {
        Log.i(TAG, "Enviando ação: " + acao);
        servidor.enviar(acao);
    }

    /**
     * Solicita status de uma sala
     */
    public void statusSala(String sala) {
        Log.i(TAG, "Solicitando status da sala: " + sala);
        servidor.enviar("start;" + sala);
    }

    /**
     * Retorna última resposta recebida
     */
    public String verificarResposta() {
        return servidor.getResposta();
    }

    /**
     * Solicita atualização das salas
     */
    public void atualizar() {
        servidor.enviar("atualizar");
    }

    /**
     * Solicita logs
     *
     * @return
     */
    public String logs() {
       return servidor.enviar("logs");
    }

    /**
     * Solicita lista de salas
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

    /**
     * Adiciona uma sala
     */
    public String addSala(Sala sala) {
        Gson gson = new Gson();
        String u = gson.toJson(sala);

        servidor.enviar("sala:--addSala--" + u);

        try {
            Thread.sleep(200);
        } catch (InterruptedException ignored) {}

        return servidor.getResposta();
    }

    public String getSala(int nSala){
        servidor.enviar("--getSala--"+nSala);
        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            System.out.println("Erro ao executar o TrataCliente");
        }
        return servidor.getResposta();

    }
}
