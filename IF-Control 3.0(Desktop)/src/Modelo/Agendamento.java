/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.util.Calendar;
import java.sql.Time;


/**
 *
 * @author axelm
 */
public class Agendamento {
    String autor,titulo;
    Calendar dataIn,dataF;
    Time hAtv,hDesat;
    int[] diaSemana,salas;

    public String getAutor() {
        return autor;
    }

    public String getTitulo() {
        return titulo;
    }

    public Calendar getDataIn() {
        return dataIn;
    }

    public Calendar getDataF() {
        return dataF;
    }

    public Time gethAtv() {
        return hAtv;
    }

    public Time gethDesat() {
        return hDesat;
    }

    public int[] getDiaSemana() {
        return diaSemana;
    }

    public int[] getSalas() {
        return salas;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setDataIn(Calendar dataIn) {
        this.dataIn = dataIn;
    }

    public void setDataF(Calendar dataF) {
        this.dataF = dataF;
    }

    public void sethAtv(Time hAtv) {
        this.hAtv = hAtv;
    }

    public void sethDesat(Time hDesat) {
        this.hDesat = hDesat;
    }

    public void setDiaSemana(int[] diaSemana) {
        this.diaSemana = diaSemana;
    }

    public void setSalas(int[] salas) {
        this.salas = salas;
    }
}
