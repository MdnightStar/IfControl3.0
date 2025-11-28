/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Aplicacao;

import Controle.ConexaoSalas;
import Controle.SocketArduino;
import Modelo.DAOManager;
import java.io.IOException;
import java.sql.SQLException;

/**
 *
 * @author LENOVO
 */
public class TestarMainApp {

    public static void main(String[] args) throws IOException, SQLException {

        DAOManager dao = new DAOManager();
        dao.resgataCodIr(1, "ARCool17");
        
    }
}
