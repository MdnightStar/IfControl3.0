/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Aplicacao;

import Controle.SocketArduino;
import java.io.IOException;

/**
 *
 * @author LENOVO
 */
public class TestarMainApp {
    public static void main(String[] args) throws IOException {
        
        SocketArduino socketArduino= new SocketArduino();
        socketArduino.enviar("LZON");
    }
}
