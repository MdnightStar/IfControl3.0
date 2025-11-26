package Controle;

import android.util.Log;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * Cliente socket para comunicação com o servidor via TCP
 * Adaptado para Android
 */
public class TrataServidor implements Runnable {

    private static final String TAG = "TrataServidor";

    private final String host = "10.0.0.92"; // IP do servidor
    private final int porta = 808;

    private Socket cliente;
    private PrintStream out;
    private Scanner in;
    private volatile String resposta = null; // 'volatile' para leitura segura entre threads
    public boolean conectado = false;

    public TrataServidor() {
        new Thread(() -> {
            try {
                cliente = new Socket(host, porta);
                out = new PrintStream(cliente.getOutputStream());
                in = new Scanner(cliente.getInputStream());
                conectado = true;
                Log.i(TAG, "Conectado ao servidor em " + host + ":" + porta);
            } catch (IOException e) {
                Log.e(TAG, "Erro ao conectar: " + e.getMessage());
                conectado = false;
            }
        }).start();
    }

    /**
     * Envia uma mensagem para o servidor
     *
     * @return
     */
    public String enviar(String message) {
        new Thread(() -> {
            try {
                if (out != null && conectado) {
                    Log.i(TAG, "Enviando: " + message);
                    out.println(message);
                } else {
                    Log.e(TAG, "Conexão não estabelecida!");
                }
            } catch (Exception e) {
                Log.e(TAG, "Erro ao enviar: " + e.getMessage());
            }
        }).start();
        return message;
    }

    /**
     * Fecha a conexão
     */
    public void desconectar() {
        try {
            if (cliente != null) cliente.close();
            if (in != null) in.close();
            if (out != null) out.close();
            conectado = false;
            Log.i(TAG, "Conexão encerrada.");
        } catch (IOException e) {
            Log.e(TAG, "Erro ao desconectar: " + e.getMessage());
        }
    }

    /**
     * Thread que escuta as respostas do servidor
     */
    @Override
    public void run() {
        while (conectado && in != null && in.hasNextLine()) {
            resposta = in.nextLine();
            Log.i(TAG, "Recebido do servidor: " + resposta);
        }
    }

    public String getResposta() {
        return resposta;
    }

    public void setResposta(String resposta) {
        this.resposta = resposta;
    }
}
