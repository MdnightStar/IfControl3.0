/**
 * Descrição: Entidade da ação executada no sistema dektop, para gerenciamento 
 * das ações feitas pelo usúario.
 */
package Modelo;

import java.sql.Time;
import java.util.Calendar;
import java.text.SimpleDateFormat;


/**
 * @author Jeison, Cauã, Axel.
 */
public class Acao {
    
    private Calendar dataAcao; //Data da ação
    private Time horaAcao; //Hora da ação
    private String tipoAcao, login; //Tipo da ação, Login pelo qual a ação foi executada
    private int idUser, nSala, idAcao; //ID do Usúario, Nº da sala, ID da ação
    private boolean status; //Status da ação

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
    

    public int getIdAcao() {
        return idAcao;
    }

    public void setIdAcao(int idAcao) {
        this.idAcao = idAcao;
    }

    public Calendar getDataAcao() {
        return dataAcao;
    }

    public void setDataAcao(Calendar dataAcao) {
        this.dataAcao = dataAcao;
    }

    public Time getHoraAcao() {
        return horaAcao;
    }

    public void setHoraAcao(Time horaAcao) {
        this.horaAcao = horaAcao;
    }

    public String getTipoAcao() {
        return tipoAcao;
    }

    public void setTipoAcao(String tipoAcao) {
        this.tipoAcao = tipoAcao;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public int getnSala() {
        return nSala;
    }

    public void setnSala(int nSala) {
        this.nSala = nSala;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
    
    public String dataFormatada(){
        SimpleDateFormat sdf= new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(dataAcao);
    }
    
    public String horaFormatada(){
        SimpleDateFormat sdf= new SimpleDateFormat("HH:mm");
        return sdf.format(horaAcao);
    }
    
    
}