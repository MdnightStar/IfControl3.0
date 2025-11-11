/**
 * Descrição:
 */
package Controle;

import Modelo.*;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
/**
 * @author Jeison
 * @version 3.0
 */
public class SocketArduino {
    protected Socket arduino;
    protected DataOutputStream out;
    protected BufferedReader input;
    private String pack;
    
    /**
    *O método conectarArduino estebelece a conexão entre o computador e o arduino
    * tanto out como input
    * 
    * @param nSala este parametro serve para identificar o IP da sala no banco de dados 
    * por meio do número
    * @throws IOException 
    * @return retorna V se a conexão for estabelecida ou F se não houver conexão
    */
    public boolean conectarArduino(int nSala) throws IOException{
        packconection(nSala);
        
        try{
            System.out.println("Começando conexão com o arduino, IP: "+pack);
            arduino=new Socket(pack,808);
            System.out.println(arduino.isConnected());
            out = new DataOutputStream(arduino.getOutputStream());
            input = new BufferedReader(new InputStreamReader(arduino.getInputStream()));
            return true;
        }catch (IOException e) {
            return false;

        }
    }
    
    public boolean conectarArduinoIP(String ip) throws IOException{
        
        try{
            System.out.println("Começando conexão com o arduino, IP: "+pack);
            arduino=new Socket(ip,808);
            System.out.println(arduino.isConnected());
            out = new DataOutputStream(arduino.getOutputStream());
            input = new BufferedReader(new InputStreamReader(arduino.getInputStream()));
            return true;
        }catch (IOException e) {
            return false;

        }
    }
    
    /**
    *O método enviar serve para enviar uma String para o arduino
    * 
    * @param req String que se passa ao arduino
    * @throws IOException 
    */
    public void enviar(String req) throws IOException{
        out.writeBytes(req);
        out.flush();
    }
    
    /**
    *O método ler recebe uma mensagem do arduino
    * 
    * @return Retorna a String que bem do arduino
    * @throws IOException 
    */
    public String ler() throws IOException {
        String str = input.readLine();
        return str;
    }
    
    /**
    *O método enviar serve para enviar 2 String, com a mensagem e o cod IR para 
    * enviar o sinal infravermelho, e uma array de int com as configurações dos 
    * sinais
    * 
    * @param str String que se passa ao arduino
    * @param cod codIR
    * @param conf configurações do sinal
    * @throws IOException 
    
    public void enviarDs(String str, String cod, int[] conf ) throws IOException{
        StringBuilder pacote= new StringBuilder();
        pacote.append(str).append("|").append(cod).append("|");
        for(int i =0;i<conf.length;i++){
            pacote.append(conf[i]);
            if(i<conf.length-1) pacote.append(",");
        }
        pacote.append("#"); //Fim do pacote
        
        out.writeBytes(pacote.toString());
        out.flush();
    }
    */

    /**
    *O método desconectarArduino fecha a conecção com o arduino
    * 
    * @throws IOException 
    */
    public void desconectarArduino() throws IOException {
        out.close();
        input.close();
        arduino.close();
    }
    
    /**
    *O método packconection retorna o IP da sala por meio do Nº da sala
    * 
    * @param nSala Nº da sala
    */
    public void packconection(int nSala){
        DAOManager dao= new DAOManager();
        Sala sala=dao.procuraSala(nSala);
        pack=sala.getIP();
    }
}
