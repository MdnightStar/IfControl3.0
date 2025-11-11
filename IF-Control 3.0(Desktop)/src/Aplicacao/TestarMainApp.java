/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Aplicacao;

import Controle.ConexaoSalas;
import Controle.SocketArduino;
import Modelo.DAOManager;
import java.io.IOException;

/**
 *
 * @author LENOVO
 */
public class TestarMainApp {

    public static void main(String[] args) throws IOException {

        SocketArduino socketArduino = new SocketArduino();

        socketArduino.conectarArduino( 1);
        
        socketArduino.enviar("LZON.");
        String temp = socketArduino.ler();//Recebe a temperatura do Arduino
        socketArduino.desconectarArduino();

        
    }
}
