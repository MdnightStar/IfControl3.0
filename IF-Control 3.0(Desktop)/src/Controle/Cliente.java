/**
 * Descrição:
 */
package Controle;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * @author Jeison
 * @version 3.0
 */
public class Cliente {

    private String host;
    private int porta;
    
    /**
     * Metodo main da classe que permite intanciar um objeto cliente sem fazer 
     * declaração da classe
     */
    public static void main(String[] args) throws IOException {
        new Cliente("0.0.0.0", 808).executa();
    }
    
    /**
     * Construtor de Cliente
     *
     * @param host Endereço asignado ao cliente, pode haver conflito se o número 
     * de clientes?
     * @param porta É a porta de entrada de dados do cliente
     */
    public Cliente(String host, int porta) {
        this.host = host;
        this.porta = porta;
    }
    
    /**
     * O método executa inicializa a comunicação com o cliente, usando a ferramenta
     * try-with-resours que fecha automaticamente recursos ao finla do bloco
     * 
     * @throws IOException
     */
    public void executa() throws IOException {
        //Cria um socket para o cliente, espera que o cliente se conecta para continuar
        try (Socket cliente = new Socket(this.host, this.porta)) {
            //Confirma a conexão
            System.out.println("O Cliente se conectou ao servidor");
            
            //Cria um fluxo de entrada do cliente, enviando informações para ele
            new Thread(new Recebedor(cliente.getInputStream())).start();
            
            //Cria um fluxo de saida com o cliente, lê mensagens do usuário pelo
            //teclado e envia ao servidor
            try (Scanner in = new Scanner(System.in); PrintStream out = new PrintStream(cliente.getOutputStream())) {
                while (in.hasNextLine()) {
                    out.println(in.nextLine());
                }
            }

        }
    }

}
