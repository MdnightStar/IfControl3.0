/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.util.ArrayList;
import Controle.Sessao;
import Modelo.Sala;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 *
 * @author LENOVO
 */
public class TesteBD {
    public static void main(String args[]){
        Gson g=new Gson();
        Sessao sessao=new Sessao();
        DAOManager dao= new DAOManager();
        
        List<Sala> salas=dao.consultarSalas();
        for(Sala sala:salas){
            System.out.println("Sala: "+sala.getnSala());
            System.out.println("IP: "+sala.getIP());
        }
        
        
        /*salas = g.fromJson(resp, tipoSala);
        for(Sala sala: salas){
            System.out.println("Sala "+sala.getnSala());
        }
        **/
        /**
        int n=dao.totalSalas();
        System.out.println("Total salas:"+n);
        
        User pes = dao.validaLogin("jdoe", "senha123");
        if(pes==null){
            System.out.println("Não tem nada");
        }else{
            System.out.println("Tem algo");
        }
        System.out.println("Login: "+pes.getLogin()+"\nSenha: "+pes.getSenha());
        
        if(dao.siapExiste((long)1)){
            System.out.println("O siap existe");
        }else{
            System.out.println("O siap não existe");
        }
        
        boolean[] conexoes =dao.statusConexao();
      
        for(int i=0; i<conexoes.length;i++){
            if(conexoes[i]){
                System.out.println("TRUE");
            }else{
                System.out.println("FALSE");
            }
        }
        
       
        
        Sala sala=dao.procuraSala(1);
        System.out.println(sala.getIP());**/
        
        
    }
}
