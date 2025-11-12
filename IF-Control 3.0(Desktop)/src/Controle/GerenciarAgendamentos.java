package Controle;

import Modelo.Agendamento;
import Modelo.DAOManager;
import java.sql.Time;
import java.util.Calendar;
import java.util.List;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GerenciarAgendamentos implements Runnable {

    private final DAOManager manager;
    // Você precisará de um controlador para enviar comandos aos dispositivos,
    // semelhante ao SocketArduino, mas para comandos de ativação/desativação.
    // O `Controle` é apenas um placeholder.
    // private final ControleDispositivos controladorDispositivos; 

    public GerenciarAgendamentos(DAOManager manager) {
        this.manager = manager;
        // this.controladorDispositivos = new ControleDispositivos(); 
    }
    
    /**
     * Verifica se um agendamento é válido para ser executado na data/dia da semana atual.
     * * @param agendamento O objeto Agendamento a ser verificado.
     * @param agora O objeto Calendar representando a data e hora atual.
     * @param diaDaSemana O dia da semana atual (Calendar.SUNDAY=1, Calendar.MONDAY=2, etc.).
     * @param horaAtualMillis A hora atual em milissegundos (dentro do dia).
     * @return true se o agendamento deve ser considerado para execução de ação, false caso contrário.
     */
    private boolean deveSerExecutado(Agendamento agendamento, Calendar agora, int diaDaSemana, long horaAtualMillis) {
        // 1. Verificar se a data atual está dentro do período de validade (Data Início e Data Fim).
        // Para a comparação de data, zeramos o tempo para comparar apenas o dia.
        
        Calendar dataAtual = (Calendar) agora.clone();
        dataAtual.set(Calendar.HOUR_OF_DAY, 0);
        dataAtual.set(Calendar.MINUTE, 0);
        dataAtual.set(Calendar.SECOND, 0);
        dataAtual.set(Calendar.MILLISECOND, 0);

        Calendar dataInicioAgendamento = agendamento.getDataIn();
        // É importante que os objetos Calendar de dataIn e dataF estejam formatados apenas com a data, 
        // ou que a comparação seja feita apenas no campo de data.
        
        // Verifica se a data atual é igual ou posterior à data de início.
        boolean aposDataInicio = !dataAtual.before(dataInicioAgendamento);

        // Verifica se a data atual é igual ou anterior à data de fim.
        boolean antesDataFim = !dataAtual.after(agendamento.getDataF());
        
        if (!aposDataInicio || !antesDataFim) {
            // Se a data atual estiver fora do intervalo de validade (DataIn/DataF), ignora o agendamento.
            return false;
        }

        // 2. Verificar se o dia da semana atual está incluído nos dias agendados.
        int[] diasAgendados = agendamento.getDiaSemana();
        
        for (int dia : diasAgendados) {
            if (dia == diaDaSemana) {
                // O agendamento é válido para este dia da semana e está dentro do período de datas.
                return true;
            }
        }
        
        // O agendamento está no período de datas, mas não é para ser executado neste dia da semana.
        return false;
    }

    @Override
    public void run() {
        while (true) {
            try {
                // 1. Obter a hora e dia atuais
                Calendar agora = Calendar.getInstance();
                int diaDaSemana = agora.get(Calendar.DAY_OF_WEEK); // 1 = Domingo, 2 = Segunda, ..., 7 = Sábado
                long horaAtualMillis = agora.getTimeInMillis();

                // 2. Buscar todos os agendamentos ativos no BD
                List<Agendamento> agendamentos = manager.consultarAgendamento(); // Método já existente no DAOManager

                for (Agendamento agendamento : agendamentos) {
                    // 3. Verificar as condições de execução
                    if (deveSerExecutado(agendamento, agora, diaDaSemana, horaAtualMillis)) {

                        // 4. Executar ativação ou desativação, se necessário
                       
                    }
                }

                // 5. Esperar um breve período antes de verificar novamente
                Thread.sleep(60000); // Exemplo: verifica a cada 1 minuto (60000 ms)

            } catch (InterruptedException e) {
                Logger.getLogger(GerenciarAgendamentos.class.getName()).log(Level.WARNING, "Thread de agendamento interrompida", e);
                break;
            } catch (Exception e) {
                Logger.getLogger(GerenciarAgendamentos.class.getName()).log(Level.SEVERE, "Erro no loop do GerenciadorAgendamentos", e);
            }
        }
    }
}
