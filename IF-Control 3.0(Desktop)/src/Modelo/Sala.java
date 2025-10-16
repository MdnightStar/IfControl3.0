/**
 * Descrição: Entidade da sala, que vai gerenciar todas as informações da sala.
 */
package Modelo;

import Aplicacao.SalaPanel;
import java.sql.Time;

/**
 * @author Jeison
 */
public class Sala {
    
    private int nSala; //Nº da sala
    private boolean estadoSala; //Estado da sala (disponivel ou indisponivel)
    private boolean estadoDataShow; //Estado do data-show(ligado, desligado, desconectado)
    private boolean conexao; //Estado da conexão(usando, em uso, desconectada )
    private boolean estadoLuzes; //Estado das luzes(ligado, desligado, desconectado)
    private boolean estadoAr; //Estado do ar-condicionado(ligado, desligado, desconectado)
    private int tempAr; //Temperatura do ar-condicionado (lista de temperaturas)
    private double temperatura; //Temperatura da sala
    private double umidade; //Humidade da sala
    private boolean presenca; //V ou F, se a detecção de movimento na sala (Existem pessoas na sala?)
    private Time horaAtivacao; //Hora que a sala foi acessada por um usúario
    private Time horaDesativacao; //Hora que o mesmo usúario se desconectou da sala
    private String IP;
    
    public Sala(int nSala, String IP){
        this.IP=IP;
        this.nSala=nSala;
    }

    public Sala() {
        
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public String getIP() {
        return IP;
    }

    public void setnSala(int nSala) {
        this.nSala = nSala;
    }

    public int getnSala() {
        return nSala;
    }
    
    
    public boolean isEstadoLuzes() {
        return estadoLuzes;
    }

    public void setEstadoLuzes(boolean estadoLuzes) {
        this.estadoLuzes = estadoLuzes;
    }
    public void setEstadoSala(boolean estadoSala) {   //insere estado sala
        this.estadoSala = estadoSala;
    }
    public void setEstadoDataShow(boolean estadoDataShow) {   //insere estado do datashow
        this.estadoDataShow = estadoDataShow;
    }
    public void setConexao(boolean estadoConexao) {   //insere estado da conexao com o arduino
        this.conexao = estadoConexao;
    }

    public boolean isEstadoAr() {
        return estadoAr;
    }
     public boolean isEstadoSala() {        //get estado sala
        return estadoSala;
    }
     public boolean isEstadoDataShow() {        //get estado datashow
        return estadoDataShow;
    }
    public boolean isEstadoDaConexao() {        //get estado da conexao do arduino
        return conexao;
    }

    public void setEstadoAr(boolean estadoAr) {
        this.estadoAr = estadoAr;
    }

    public double getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(double temperatura) {
        this.temperatura = temperatura;
    }

    public double getUmidade() {
        return umidade;
    }

    public void setUmidade(double umidade) {
        this.umidade = umidade;
    }

    public boolean isPresenca() {
        return presenca;
    }

    public void setPresenca(boolean presenca) {
        this.presenca = presenca;
    }
    

    public int getTempAr() {
        return tempAr;
    }

    public void setTempAr(int tempAr) {
        this.tempAr = tempAr;
    }

    public Time getHoraAtivacao() {
        return horaAtivacao;
    }

    public void setHoraAtivacao(Time horaAtivacao) {
        this.horaAtivacao = horaAtivacao;
    }

    public Time getHoraDesativacao() {
        return horaDesativacao;
    }

    public void setHoraDesativacao(Time horaDesativacao) {
        this.horaDesativacao = horaDesativacao;
    }
    
    public void atualizaSala( double temperatura, double umidade, boolean presenca ){
        this.temperatura = temperatura;
        this.umidade = umidade;
        this.presenca = presenca;
    }
    
    @Override
    public boolean equals(Object o){
        SalaPanel panel=(SalaPanel) o;
        if(nSala==panel.getN()){
            return true;
        }else{
            return false;
        }
    }
    
}

