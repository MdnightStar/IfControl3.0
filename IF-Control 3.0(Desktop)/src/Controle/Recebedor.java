/**
 * Descrição:
 */
package Controle;

import java.io.InputStream;
import java.util.Scanner;

/**
 * @author Jeison
 * @version 3.0
 */
public class Recebedor implements Runnable {

    private InputStream servidor;

    /**
     * Construtor inicial, estabelece um fluxo de saida do servidor
     */
    public Recebedor(InputStream servidor) {
        this.servidor = servidor;
    }

    @Override
    public void run() {
        Scanner s = new Scanner(this.servidor);
        while (s.hasNextLine()) {
            System.out.println(s.nextLine());
        }
    }
}
