/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author LENOVO
 */
public class CodIr {
    private String cod;
    private String funcao;
    private int id,dispositivo_id;

    public void setId(int id) {
        this.id = id;
    }

    public void setDispositivo_id(int dispositivo_id) {
        this.dispositivo_id = dispositivo_id;
    }

    public int getId() {
        return id;
    }

    public int getDispositivo_id() {
        return dispositivo_id;
    }
    
    public void setCod(String cod) {
        this.cod = cod;
    }

    public void setFuncao(String funcao) {
        this.funcao = funcao;
    }

    public String getCod() {
        return cod;
    }

    public String getFuncao() {
        return funcao;
    }
    
}
