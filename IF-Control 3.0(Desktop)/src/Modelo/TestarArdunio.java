/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;
import Controle.SocketArduino;
import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;
import javax.net.ssl.SSLSocket;
import Controle.TrataServidor;
/**
 *
 * @author LENOVO
 */
public class TestarArdunio {
    private SocketArduino arduino;
    
    public static void main(String[] args) {
        try {
            /**
            TrataServidor usuario=new TrataServidor();
            usuario.conectar();
            System.out.println("Conectou");
            */
            SocketArduino arduino=new SocketArduino();
            arduino.conectarArduino(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
}
