/**
 * Descrição:
 */
package Controle;

import java.io.InputStream;
import java.util.Scanner;
import java.io.PrintStream;
import java.text.ParseException;
import Modelo.Acao;
import Modelo.DAOManager;
import Modelo.Sala;
import Modelo.User;
import com.google.gson.Gson;

/**
 * @author Jeison
 */
public class TrataCliente implements Runnable {

    private InputStream clienteIn;
    private PrintStream clienteOut;
    private Servidor servidor;
    private TratarAcao sessao;
    private User user;
    private Sala sala;
    private Acao acao;
    private Gson gson;
    private DAOManager manager;

    /**
     * Construtor da classe TrataCliente
     *
     * @param clienteIn fluxo de saida do cliente
     * @param clienteOut fluxo de entrada do cliente, para enviar informaçõe
     * @param servidor
     * @param manager banco de dados do cliente
     */
    public TrataCliente(InputStream clienteIn, PrintStream clienteOut, Servidor servidor, DAOManager manager) {
        this.clienteIn = clienteIn;
        this.servidor = servidor;
        this.clienteOut = clienteOut;
        this.manager = manager;
        sessao=new TratarAcao();
        clienteOut.println("CONNECTOK");
        gson = new Gson();
    }

    /**
     * Construtor da classe TrataCliente
     *
     * @param clienteIn fluxo de saida do cliente
     * @param clienteOut fluxo de entrada do cliente, para enviar informaçõe
     * @param servidor
     * @param manager banco de dados do cliente
     */
    
    @Override
    public void run() {
        try (Scanner s = new Scanner(this.clienteIn)) { //Declaração do fluxo de saída do cliente
            
            while (s.hasNextLine()) { //Leitura de dados do cliente
                
                String msg = s.nextLine();
                System.out.println("Mensagem recebida pelo servidor: "+msg);
                servidor.setMessage(msg); //Print da mensagem do cliente no servidor
                if (msg.contains("_login")) {
                    String campo[] = msg.split(",");
                    String dado[] = campo[0].split(":");
                    String dado1[] = campo[1].split(":");

                    sessao = new TratarAcao();
                    sessao.setLogin(dado[1].substring(1, dado[1].length() - 1));
                    sessao.setSenha(dado1[1].substring(1, dado1[1].length() - 2));

                    sessao.setManager(manager);  //Insere o banco de dados na sessao
                    clienteOut.println(sessao.login());  //Envia para o usúario uma mensagem do tipo "LOGONOK ou LOGONOT" referente ao login
                    System.out.println(sessao.login());

                } else if (msg.contains("siap")) {   //Cadastro
                    user = gson.fromJson(msg, User.class); //Deserializa a mensagem e transforma em usúario
                    sessao = new TratarAcao();
                    sessao.setManager(manager); //Insere o banco de dados na sessao
                    clienteOut.println(sessao.cadastrarUser(user.getSiap(), user.getNome(),
                            user.getLogin(), user.getSenha())); //Envia para o cliente se o cadastro foi executado
                } else if (msg.contains("start")) {
                    //Derifica se a sala está ocupada ou não
                    String texto = msg;
                    String quebra[] = texto.split(";");
                    int nSala = Integer.parseInt(quebra[1]);
                    clienteOut.println(sessao.estadoSala(nSala));//Envia para o cliente se a sala está ocupada ou não
                } else if (msg.contains("tipoAcao")) {
                    System.out.println(msg); //Printa os atributos;
                    acao = gson.fromJson(msg, Acao.class); //Deserializa a msg em uma Acao
                    String r = sessao.tratarAcao(acao.getTipoAcao(), acao.getnSala()); //Efetua a ação e retorna se deu falho ou não
                    servidor.distribuiMsg(sessao.pegarSalas()); //Envia todas as salas serializadas para os clientes
                    //metodo para agendamento   servidor.iniciarAgendamentos();
                } else if (msg.contains("logs")) {
                    clienteOut.println(sessao.pegarLogs());//Imprime todas as ações
                } else if (msg.contains("atualizar")) {
                    servidor.distribuiMsg(sessao.pegarSalas()); //Envia todas as salas serializadas para os clientes, para poder atualizar os atributos
                }else if(msg.contains("salas")){
                    clienteOut.println(sessao.pegarSalas());//Imprime todas as salas
                }else if(msg.contains("addSala")){
                    String texto=msg;
                    String quebra[] = texto.split("--addSala--");
                    sala=gson.fromJson(quebra[1], Sala.class);
                    sessao=new TratarAcao();
                    sessao.setManager(manager);
                    clienteOut.println(sessao.cadastrarSala(sala.getnSala(), sala.getIP()));
                }
            }
        } catch (ParseException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
