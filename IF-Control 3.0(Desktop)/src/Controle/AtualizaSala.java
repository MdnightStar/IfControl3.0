/**
 * Descrição: Esta classe serve para recolher as informações do arduino
 */
package Controle;

import java.io.IOException;
import Modelo.DAOManager;
import Modelo.Sala;
import javax.swing.JOptionPane;

public class AtualizaSala extends SocketArduino implements Runnable {

    private DAOManager manager;
    private Servidor servidor;
    private Sala sala;
    private boolean[] salasDisponiveis;

    public AtualizaSala(DAOManager manager, Servidor servidor) {
        this.manager = manager;
        this.servidor = servidor;
        sala = new Sala();
    }

    @Override
    public void run() {

        new Thread(new ConexaoSalas(manager)).start();

        while (true) {
            try {
                salasDisponiveis = manager.statusConexao();
                int cont = 0;

                for (int ns = 0; ns < salasDisponiveis.length; ns++) {

                    if (salasDisponiveis[ns]) {

                        double temp = solicitarDouble(ns + 1, "TEMP.");
                        double umi  = solicitarDouble(ns + 1, "UMIDADE.");
                        boolean presenca = solicitarBoolean(ns + 1, "PRESENCA.");

                        servidor.setMessage("sala: " + (ns + 1) + " " + temp + " " + umi + " " + presenca);

                        manager.atualizaSala((ns + 1), temp, umi, presenca);

                    } else {

                        if (cont > 50) {
                            System.out.println("Sala " + ns + " não conectada");
                            cont = 0;
                        } else {
                            cont++;
                        }
                    }
                }

                Thread.sleep(3000);

            } catch (IOException e) {
                System.out.println("Erro de comunicação: " + e.getMessage());
            } catch (InterruptedException ex) {
                JOptionPane.showMessageDialog(null, "A atualização foi interrompida", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    // ======================================================================
    //  MÉTODOS SEGUROS
    // ======================================================================

    private double solicitarDouble(int sala, String comando) throws IOException {
        conectarArduino(sala);
        enviar(comando);
        String resposta = ler();
        desconectarArduino();
        return parseSeguroDouble(resposta);
    }

    private boolean solicitarBoolean(int sala, String comando) throws IOException {
        conectarArduino(sala);
        enviar(comando);
        String resposta = ler();
        desconectarArduino();
        return parseSeguroBoolean(resposta);
    }


    // ======================================================================
    //  PARSES SEGUROS
    // ======================================================================

    /**
     * Evita NumberFormatException em valores inválidos vindos do Arduino
     */
    private double parseSeguroDouble(String valor) {

        if (valor == null) return 0;

        valor = valor.trim().replace("\n", "").replace("\r", "");

        if (valor.isEmpty()) return 0;

        // Arduino envia "nan" quando o sensor falha
        if (valor.equalsIgnoreCase("nan")) return 0;

        // Se tiver qualquer lixo, ignora e retorna 0
        if (!valor.matches("[-+]?[0-9]*\\.?[0-9]+")) return 0;

        try {
            return Double.parseDouble(valor);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * Tratamento seguro de boolean
     */
    private boolean parseSeguroBoolean(String valor) {
        if (valor == null) return false;

        valor = valor.trim().toUpperCase();

        return (valor.equals("TRUE") || valor.equals("1"));
    }
}

