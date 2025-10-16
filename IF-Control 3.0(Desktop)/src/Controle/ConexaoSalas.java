/**
 * Descrição
 */
package Controle;

import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import Modelo.DAOManager;

/**
 * @author Jeison
 */
public class ConexaoSalas extends SocketArduino implements Runnable {
    private DAOManager manager;
    
    public ConexaoSalas(DAOManager manager){
        this.manager= manager;
    }
    
    @Override
    public void run(){
        int lim=manager.totalSalas();
        for(int i=1;i==lim;i++){
            try{
                if(conectarArduino(i)){
                    manager.efetivarConexao(true, i);
                    desconectarArduino();
                }else{
                    System.out.println("Sala não conectada: "+i);
                    manager.efetivarConexao(false, i);
                }
            }catch(IOException ex){
                System.out.println("Erro na conexão com Arduino");;
            }
        }
        
        try{
            Thread.sleep(3000);
        }catch(InterruptedException ex){
            Logger.getLogger(ConexaoSalas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}
