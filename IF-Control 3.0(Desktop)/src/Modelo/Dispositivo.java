/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.util.List;

/**
 *
 * @author LENOVO
 */
public class Dispositivo {
    private String marca, modelo, config, tipo;
    private List<CodIr> listaCodigos;
    private int[] salasRelacionadas;

    public void setSalasRelacionadas(int[] salasRelacionadas) {
        this.salasRelacionadas = salasRelacionadas;
    }

    public int[] getSalasRelacionadas() {
        return salasRelacionadas;
    }
    private int id;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setListaCodigos(List<CodIr> listaCodigos) {
        this.listaCodigos = listaCodigos;
    }

    public String getMarca() {
        return marca;
    }

    public String getModelo() {
        return modelo;
    }

    public String getConfig() {
        return config;
    }

    public String getTipo() {
        return tipo;
    }

    public List<CodIr> getListaCodigos() {
        return listaCodigos;
    }
}
