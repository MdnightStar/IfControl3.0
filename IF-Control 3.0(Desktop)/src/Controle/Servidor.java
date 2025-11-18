/**
 *Descrição:
 */
package Controle;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import Modelo.*;
import java.util.HashMap;

/**
 * @author Jeison
 * @version 3.0
 */
public class Servidor {

    // Colocar o agendamento, pra isso é recomendado criar um List<Agendamento>
    private final int porta;
    private boolean encerrar=false;
    private HashMap<String, PrintStream> clientes;
    private String message;
    private List<Sala> salas;
    protected static DAOManager manager;
    private ServerSocket servidor;
    private SchedulerManager schedulerManager;

    /**
     * Método main principal, executa o servidor, também ja defini o valor da
     * variavel final porta, sendo essa a porta de conexão para a comunicação
     * com os clientes
     *
     * @param args[]
     */
    public static void main(String args[]) {

        try {
            new Servidor(808).executa();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }

    /**
     * Construtor do Servidor, instância os principais atributos
     *
     * @param porta Porata na qual vai se estabelecer o servidor
     */
    public Servidor(int porta) {
        this.porta = porta;
        this.clientes = new HashMap<>();
        this.salas = new ArrayList();
        this.message = "IFControl";

    }

    /**
     * O método executa tem por finalidade iniciar o sercidor, ou seja, se conectar
     * na rede (porta). Pegar informações basica do arduino e colocar no banco de dados
     * (temperatura, humidade, presença) como também establecer o fluxo de saida e entrada
     * em todos os clientes
     * 
     * @throws IOException
     *
     */
    public void executa() throws IOException {

        servidor = new ServerSocket(porta);
        manager = new DAOManager();

        JOptionPane.showMessageDialog(null, "Conectado com sucesso \n Porta: "+porta, "Sucesso", JOptionPane.INFORMATION_MESSAGE);

        //Para confirmar se a conexão com o banco de dados foi bem sucedida
        if (manager.connect() == false) {
            System.exit(1);
            JOptionPane.showMessageDialog(null, "Erro ao conectar no banco de dados","Erro",JOptionPane.ERROR_MESSAGE);
        } else {
            JFrame frame = new JFrame("IFCONTROL");
            JOptionPane.showMessageDialog(frame, "Servidor Inicializado","Sucesso", JOptionPane.INFORMATION_MESSAGE);
            manager.resetSalas(); //Zera as conexões no banco de dados
        }
        
        try {
            this.schedulerManager = new SchedulerManager(manager); // Passa o DAOManager
            this.schedulerManager.startScheduler();
            System.out.println("Scheduler Quartz iniciado e agendamentos carregados.");
        } catch (Exception e) {
            System.err.println("Erro ao iniciar o Quartz Scheduler: " + e.getMessage());
            // Decida se o servidor deve parar ou continuar sem agendamentos
        }

        //Inicia as threads que vão pedir a informações para o arduino e armazenar no banco de dados     
        new Thread(new AtualizaSala(manager, this)).start();

        //Inicia a comunicação com os clientes, gera um lop inifito.
        while (!encerrar) {
            Socket cliente = null;
            System.out.println("Esperando conexão.");
            cliente = servidor.accept(); //Aguarda a conexão de um cliente, literalemnte
            System.out.println("Nova conexao com o cliente "+ cliente.getInetAddress().getHostAddress());
         
            /**Esse techo ter por finalidade adicionar uma comunicação de saida do cliente em um ArrayList 
            *de comunicações de saida de clientes, ou seja, o trecho new PrintStream(cliente.getOutputStream()) 
            * estabelece um fluxo de saida em clientes, onde o cliente pode enviar mensagens
            */
            int count=clientes.size();
            this.clientes.put((String)cliente.getInetAddress().getHostAddress(),new PrintStream(cliente.getOutputStream()));
            System.out.println("Clientes conectados: "+clientes.size());
            
                new Thread(new TrataCliente(cliente.getInputStream(), new PrintStream(cliente.getOutputStream()), this, manager)).start();
            
        }

    }

    public SchedulerManager getSchedulerManager() {
        return schedulerManager;
    }
    
    /**
     * Envia determinada mensagem para todos os atuais clientes Socket.
     *
     * @param msg
     */
    public void distribuiMsg(String msg) {
        for (PrintStream cliente : this.clientes.values()) {
            cliente.println(msg);
        }
    }

    public void setMessage(String str) {
        this.message = str;
        System.out.println("No Set message do Servidor "+str);
    }

    /**
     * Percorre vetor de salas e verifica os "horarioAtivacao" e
     * "horarioDesativacao" delas, e, se o valor for diferente de nulo, adiciona
     * a instarncia da classe que vai cuidar do TimerTask referente aquela sala.
     */
    
    //teria esss metodo public void iniciarAgendamentos() {
}
