/*
 * 
 */
package Controle;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author Jeison
 */
public class TrataServidor implements Runnable {

    private final String host = "0.0.0.0";
    private final int porta = 808;
    private Socket cliente;
    private PrintStream out;
    private Scanner in;
    private String resposta = null;

    /**
     * Construtor da classe, instancia um socket que vai ser o cliente
     * 
     * @throws IOException
     */
    public TrataServidor() throws IOException {
        cliente = new Socket(host, porta);
    }
    
    /**
     * Estabelece a conexão com o cliente
     * 
     * @throws IOException
     */
    public void conectar() throws IOException {
        out = new PrintStream(cliente.getOutputStream());
        in = new Scanner(cliente.getInputStream());
    }
    
    /**
     * Fecha a conexão
     * 
     * @throws IOException
     */
    public void desconectar() throws IOException {
        cliente.close();
        in.close();
        out.close();
    }
    
    /**
     * Envia uma mensagem para o servidor
     * 
     * @param message
     */
    public void enviar(String message){
        System.out.println("Enviando para o servidor: "+message);
        out.println(message);
        try {
            Thread.sleep(100);
        } catch (Exception e) {
        }
    }
    
    /**
     * Recolhe a mensagem do cliente
     * 
     */
    @Override
    public void run() {
        while (in.hasNextLine()) {
            resposta = in.nextLine();
            System.out.println("TrataServidor recebeu: "+resposta);
        }
    }
    
    public String getResposta() {
        return resposta;
    }

    public void setResposta(String resposta) {
        this.resposta = resposta;
    }
}
