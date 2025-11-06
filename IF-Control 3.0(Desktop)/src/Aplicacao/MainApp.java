/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Aplicacao;
import Controle.Sessao;

/**
 *
 * @author Jeison
 */
public class MainApp {
    protected static Sessao sessao;
    protected static boolean running;
    protected boolean sessaoAberta = true;

    
    public MainApp(){
        sessao= new Sessao();
        if(sessao.iniciarSessao()==false){
            System.exit(0);
        }else{
        System.out.println("Sessao iniciada");
        running=true;
        showLogin();
        }
    }
    
    public static void showLogin(){
        try{
            Login.main(null);
        }catch(Exception ex){
            System.err.println("Erro ao carregar login" + ex.getMessage());
        }
    }
    
    public static void showCadastro(){
        try{
            Cadastro.main(null);
        }catch(Exception ex){
            System.err.println("Erro ao carregar cadastro" + ex.getMessage());
        }
    }
    
    public static void showPAAcoes(){
        try{
            PAAcoes.main(null);
        }catch(Exception ex){
            System.err.println("Erro ao carregar ações" + ex.getMessage());
        }
    }
    
    public static void showPAngendamento(){
        try{
           PAgendamento.main(null);
        }catch(Exception ex){
            System.err.println("Erro ao carregar agendamentos" + ex.getMessage());
        }
    }
    
    public static void showPDispositivos(){
        try{
            PDispositivos.main(null);
        }catch(Exception ex){
            System.err.println("Erro ao carregar dispositivos" + ex.getMessage());
        }
    }
    
    public static void showPSalas(){
        try{
            PSala.main(null);
        }catch(Exception ex){
            System.err.println("Erro ao carregar salas " + ex.getMessage());
        }
    }
    
    public static void showAddSala(){
        try {
            AddSala.main(null);
        } catch (Exception e) {
            System.err.println("Erro ao carregar cadastrar sala"+ e.getMessage());
        }
    }
    
    public static void showSala(String nSala, boolean estadoAr, boolean estadoDS, boolean estadoLuzes,
    boolean estadoSala, boolean presenca, int nsala){
        try {
            FrameSala.main(null);
        } catch (Exception e) {
            System.err.println("Erro ao carregar sala"+ e.getMessage());
        }
    }
    
    public static void showPesquisarAcao(){
        try {
            PesquisarAcao.main(null);
        } catch (Exception e) {
            System.err.println("Erro ao carregar pesquisar ação"+e.getMessage());
        }
    }
    
    public static void showFrameSala(){
        try {
            FrameSala.main(null);
        } catch (Exception e) {
            System.err.println("Erro ao carregar frame sala"+e.getMessage());
        }
    }
    
    public static void showAddAgendamento(){
        try {
            AddAgendamento.main(null);
        } catch (Exception e) {
            System.err.println("Erro ao carregar adicionar agendamento"+e.getMessage());
        }
    }
    
    public static void main(String[]args){
        MainApp aplicacao=new MainApp();
    }
    
    
}
