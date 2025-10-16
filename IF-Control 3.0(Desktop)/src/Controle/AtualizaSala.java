/**
 * Descrição: Esta classe serve para recolher as informações do arduino
 */
package Controle;

import java.io.IOException;
import java.util.Arrays;
import Modelo.DAOManager;
import Modelo.Sala;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 * @author Jeison
 * @version 3.0
 */
public class AtualizaSala extends SocketArduino implements Runnable {

    private DAOManager manager;
    private Servidor servidor;
    private Sala sala;
    private boolean[] salasDisponiveis;

    /**
     * Construtor do AtualizaSala, inializa os atributos
     *
     * @param manager Controlador do banco de dados
     * @param servidor
     */
    public AtualizaSala(DAOManager manager, Servidor servidor) {
        this.manager = manager;
        this.servidor = servidor;
        sala = new Sala();

    }
    
    /**
     * O método run é implementado de Runnable, permitindo que esta classe execute 
     * threads (que nada mais permite que o pagrama trabalha em varias tarefas) para 
     * permitir a comunicação constante com o arduino, recolhendo as principais 
     * informações
     *
     */
    @Override
    public void run() {
        double temp;
        double umi;
        boolean presenca;

        new Thread(new ConexaoSalas(manager)).start();

        while (true) {
            int cont=0;
            try {
                salasDisponiveis = manager.statusConexao();//Procura no banco de dados todas as salas, e retorna um array dos seus estatus
                for (int ns = 0; ns < salasDisponiveis.length; ns++) { //Percorre o array
                    if (salasDisponiveis[ns]) {//Verifica as salas que estão com status V
                        conectarArduino(ns + 1);
                        enviar("TEMP.");//Pede para o arduino a temperatura
                        temp = Double.parseDouble(ler());//Recebe a temperatura do Arduino
                        desconectarArduino();

                        conectarArduino(ns + 1);
                        enviar("UMIDADE.");//Pede para o arduino a umidade
                        umi = Double.parseDouble(ler());//Recebe a umidade do Arduino
                        desconectarArduino();

                        conectarArduino(ns + 1);
                        enviar("PRESENCA.");//Pede para o arduino a presença
                        presenca = Boolean.parseBoolean(ler());//Recebe a presença do Arduino
                        desconectarArduino();
                      

                        servidor.setMessage("sala: " + (ns + 1) + " " + temp + " " + umi + " " + presenca); //Mensagem com as informações recebidas

                        manager.atualizaSala((ns + 1), temp, umi, presenca); //atualiza o banco de dados com as respesctivas salas
                        //caso precisa adicionar o metodo conexao arduino com o servido  para atualizar no bd aqui !!!

                    } else {
                        if(cont>50){
                            System.out.println("Sala "+ns+" não conectada");
                            cont=1;
                        }else{
                            cont++;
                        }
                    }
                }
                Thread.sleep(3000);//Faz thread descansar por 3 segundos
            } catch (IOException e) {
                System.out.println(": " + e.getMessage());
                e.printStackTrace();
            } catch (InterruptedException ex) {
                JOptionPane.showMessageDialog(null, "A atualização foi interropida", "Erro", JOptionPane.ERROR_MESSAGE);
            }

        }

    }
}
