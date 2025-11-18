package Controle;

import Modelo.Agendamento;
import java.sql.Time;

/**
 * Classe utilitária para gerar expressões Cron a partir de um objeto Agendamento.
 */
public class CronExpressionGenerator {

    /**
     * Gera a parte de Dias da Semana (ex: "2,3,4,5,6") para a expressão Cron.
     * @param diasSemana Array de int com os dias (1=Dom, 7=Sab).
     * @return String no formato Cron (ex: "1,2,3,4,5,6,7").
     */
    private static String getDiasDaSemanaCron(int[] diasSemana) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < diasSemana.length; i++) {
            sb.append(diasSemana[i]);
            if (i < diasSemana.length - 1) {
                sb.append(",");
            }
        }
        return sb.toString();
    }

    /**
     * Gera a expressão Cron para o evento de LIGAR (hAtv).
     * @param agendamento O objeto Agendamento.
     * @return Expressão Cron completa (ex: "0 30 7 ? * 2,3,4,5,6").
     */
    public static String generateLigarCron(Agendamento agendamento) {
        Time hAtv = agendamento.gethAtv(); // Hora Ativação
        
        // Time retorna a hora em milissegundos desde 01/01/1970.
        // Convertemos para Calendar para extrair minutos e horas.
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(hAtv);

        int hora = cal.get(java.util.Calendar.HOUR_OF_DAY);
        int minuto = cal.get(java.util.Calendar.MINUTE);
        
        String dias = getDiasDaSemanaCron(agendamento.getDiaSemana());

        // Formato: segundos minutos horas dia_do_mês mês dia_da_semana
        // Exemplo: "0 30 7 ? * 2,3,4,5,6" (às 07:30, de segunda a sexta)
        return String.format("0 %d %d ? * %s", minuto, hora, dias);
    }

    /**
     * Gera a expressão Cron para o evento de DESLIGAR (hDesat).
     * @param agendamento O objeto Agendamento.
     * @return Expressão Cron completa (ex: "0 0 18 ? * 2,3,4,5,6").
     */
    public static String generateDesligarCron(Agendamento agendamento) {
        Time hDesat = agendamento.gethDesat(); // Hora Desativação

        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(hDesat);

        int hora = cal.get(java.util.Calendar.HOUR_OF_DAY);
        int minuto = cal.get(java.util.Calendar.MINUTE);
        
        String dias = getDiasDaSemanaCron(agendamento.getDiaSemana());

        // Formato: segundos minutos horas dia_do_mês mês dia_da_semana
        return String.format("0 %d %d ? * %s", minuto, hora, dias);
    }
}