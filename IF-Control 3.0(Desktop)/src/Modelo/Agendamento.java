package Modelo;

import java.util.Calendar;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays; // Importação adicionada para uso do método toString

/**
 *
 * @author axelm e cauaa
 */
public class Agendamento {
    // Adicionado o ID do agendamento
    int idAgendamento; 
    
    String autor,titulo;
    Calendar dataIn,dataF;
    Time hAtv,hDesat;
    int[] diaSemana,salas;
    ArrayList <String> dispositivos;
    
    public Agendamento(){
    }

    public Agendamento(String autor, String titulo, Calendar dataIn, Calendar dataF, Time hAtv, Time hDesat, int[] diaSemana, int[] salas, ArrayList<String> dispositivos) {
        this.autor = autor;
        this.titulo = titulo;
        this.dataIn = dataIn;
        this.dataF = dataF;
        this.hAtv = hAtv;
        this.hDesat = hDesat;
        this.diaSemana = diaSemana;
        this.salas = salas;
        this.dispositivos = dispositivos;
    }
    
    

    // Getters
    public int getIdAgendamento() {
        return idAgendamento;
    }

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

    // Setters
    public void setIdAgendamento(int idAgendamento) {
        this.idAgendamento = idAgendamento;
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
    
    public ArrayList<String> getDispositivos() {
        return dispositivos;
    }

    public void setDispositivos(ArrayList<String> dispositivos) {
        this.dispositivos = dispositivos;
    }
    
    // Opcional: Para ajudar na visualização/depuração
    @Override
    public String toString() {
        return "Agendamento{" + "idAgendamento=" + idAgendamento + ", autor=" + autor + ", titulo=" + titulo + ", dataIn=" + (dataIn != null ? dataIn.getTime() : "null") + ", dataF=" + (dataF != null ? dataF.getTime() : "null") + ", hAtv=" + hAtv + ", hDesat=" + hDesat + ", diaSemana=" + Arrays.toString(diaSemana) + ", salas=" + Arrays.toString(salas) + '}';
    }
}