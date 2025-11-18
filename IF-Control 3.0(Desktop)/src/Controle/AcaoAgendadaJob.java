package Controle;

import Modelo.Acao;
import Modelo.DAOManager;
import Modelo.Sala; // Importar Sala para poder atualizar o estado dos dispositivos
import org.quartz.*;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Time;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Job principal que executa a ação de ligar ou desligar.
 */
public class AcaoAgendadaJob extends SocketArduino implements Job {

    // Chaves para o JobDataMap (MANTIDAS)
    public static final String KEY_TIPO_ACAO = "tipoAcao";
    public static final String KEY_ID_AGENDAMENTO = "idAgendamento";
    public static final String KEY_SALAS = "salas";
    public static final String KEY_DISPOSITIVOS = "dispositivos";
    public static final String KEY_AUTOR_LOGIN = "autorLogin";

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();

        // 1. Obter dados do contexto
        String tipoAcao = dataMap.getString(KEY_TIPO_ACAO); // "LIGAR" ou "DESLIGAR"
        int idAgendamento = dataMap.getInt(KEY_ID_AGENDAMENTO);
        int[] salas = (int[]) dataMap.get(KEY_SALAS);
        String[] dispositivos = (String[]) dataMap.get(KEY_DISPOSITIVOS);
        String autorLogin = dataMap.getString(KEY_AUTOR_LOGIN);

        DAOManager dao = new DAOManager();
        int idUser = dao.getIdUserByLogin(autorLogin); // Obtém o ID do usuário para o log

        System.out.println("--- JOB EXECUTADO ---");
        
        // 2. Executar a ação para cada sala
        for (int nSala : salas) {
            
            // Assume sucesso no início de cada sala
            boolean statusAcaoDaSala = true; 
            
            // Loop de Dispositivos (Lógica de Comunicação e Status)
            for(String dis : dispositivos){
                String comando =  dis+ tipoAcao + "."; // Adaptação do seu padrão: ACAO+DISPOSITIVO+'.' (ex: LIGARLZ.)
                String respostaArduino = "";
                
                try {
                    conectarArduino(nSala);
                    enviar(comando);
                    respostaArduino = ler();
                    desconectarArduino();
                    
                    // Verifica a resposta do Arduino
                    if (!respostaArduino.contains("OK")) {
                        statusAcaoDaSala = false; // Falha se não receber OK
                        System.err.println("Comando " + comando + " na Sala " + nSala + " falhou. Resposta: " + respostaArduino);
                    } else {
                        dao.atualizarEstadoDispositivo(nSala, dis, tipoAcao.equals("ON"));
                    }
                    
                } catch (IOException ex) {
                    // Falha na conexão/envio/leitura é uma falha de ação
                    statusAcaoDaSala = false;
                    Logger.getLogger(AcaoAgendadaJob.class.getName()).log(Level.SEVERE, "Erro de comunicação com Arduino na Sala " + nSala, ex);
                }
            }
            
            // 3. Registrar a ação no Log (Tabela ACÃO)
            Acao logAcao = new Acao();
            logAcao.setLogin(autorLogin); 
            logAcao.setIdUser(idUser); // USANDO O ID DO USUÁRIO OBTIDO
            logAcao.setnSala(nSala);
            logAcao.setTipoAcao(tipoAcao + "-" + String.join(",", dispositivos)); 
            logAcao.setStatus(statusAcaoDaSala); // Status final da execução na sala
            logAcao.setDataAcao(Calendar.getInstance()); 
            logAcao.setHoraAcao(new Time(System.currentTimeMillis()));

            dao.inserirAcao(logAcao);
            System.out.println("Ação de " + tipoAcao + " registrada no log para Sala " + nSala + ". Status: " + (statusAcaoDaSala ? "SUCESSO" : "FALHA"));
        }
    }
}
