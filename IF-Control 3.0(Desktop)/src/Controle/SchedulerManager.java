package Controle;

import Modelo.Agendamento;
import Modelo.DAOManager;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Date;
import java.util.List;

public class SchedulerManager {

    private static Scheduler scheduler;
    private final DAOManager daoManager;

    public SchedulerManager(DAOManager manager) { // NOVO CONSTRUTOR
        this.daoManager = manager;
    }

    public void startScheduler() throws SchedulerException {
        // 1. Inicializar o Scheduler
        SchedulerFactory sf = new StdSchedulerFactory();
        scheduler = sf.getScheduler();
        scheduler.start();

        System.out.println("Quartz Scheduler iniciado.");

        // 2. Carregar e agendar todas as tarefas existentes no BD
        scheduleAllActiveAgendamentos();
    }

    public void shutdownScheduler() throws SchedulerException {
        if (scheduler != null && scheduler.isStarted()) {
            scheduler.shutdown(true); // true para esperar a conclusão dos jobs em execução
            System.out.println("Quartz Scheduler desligado.");
        }
    }

    /**
     * Carrega todos os agendamentos ativos do banco de dados e os agenda.
     */
    public void scheduleAllActiveAgendamentos() {
        System.out.println("Carregando agendamentos ativos do BD...");

        // O DAOManager já tem consultarAgendamento()
        List<Agendamento> agendamentos = daoManager.consultarAgendamento();

        for (Agendamento agendamento : agendamentos) {
            if (agendamento.isStatusAgendamento()) {
                try {
                    scheduleAgendamento(agendamento);
                } catch (SchedulerException e) {
                    System.err.println("Erro ao agendar ID " + agendamento.getIdAgendamento() + ": " + e.getMessage());
                }
            }
        }
        System.out.println("Carregamento de agendamentos concluído.");
    }

    /**
     * Agenda um único objeto Agendamento (cria 2 jobs: LIGAR e DESLIGAR).
     */
    public void scheduleAgendamento(Agendamento agendamento) throws SchedulerException {

        // 1. Definir os dados que serão passados para o Job
        JobDataMap jobData = new JobDataMap();
        jobData.put(AcaoAgendadaJob.KEY_ID_AGENDAMENTO, agendamento.getIdAgendamento());
        jobData.put(AcaoAgendadaJob.KEY_SALAS, agendamento.getSalas());
        jobData.put(AcaoAgendadaJob.KEY_AUTOR_LOGIN, agendamento.getAutor()); // Usamos 'Autor' como login do usuário
        jobData.put(AcaoAgendadaJob.KEY_DISPOSITIVOS, agendamento.getDispositivos().toArray(new String[0]));

        // 2. JOB DE LIGAR
        scheduleSingleAction(agendamento, jobData, "ON");

        // 3. JOB DE DESLIGAR
        scheduleSingleAction(agendamento, jobData, "OFF");
    }

    /**
     * Função auxiliar para criar e agendar um Job de Ligar ou Desligar.
     */
    private void scheduleSingleAction(Agendamento agendamento, JobDataMap baseDataMap, String acao) throws SchedulerException {

        String jobName = acao + "_" + agendamento.getIdAgendamento();
        String cronExpression;
        Date startDate = agendamento.getDataIn().getTime();
        Date endDate = agendamento.getDataF().getTime();

        // Copia e ajusta o JobDataMap para a ação específica
        JobDataMap dataMap = (JobDataMap) baseDataMap.clone();
        dataMap.put(AcaoAgendadaJob.KEY_TIPO_ACAO, acao);

        // Define a expressão Cron
        if ("ON".equals(acao)) {
            cronExpression = CronExpressionGenerator.generateLigarCron(agendamento);
        } else if ("OFF".equals(acao)) {
            cronExpression = CronExpressionGenerator.generateDesligarCron(agendamento);
        } else {
            return; // Deve ser LIGAR ou DESLIGAR
        }

        // Criar o JobDetail (define a tarefa)
        JobDetail job = JobBuilder.newJob(AcaoAgendadaJob.class)
                .withIdentity(jobName, "AGENDAMENTO_GROUP")
                .usingJobData(dataMap)
                .build();

        // Criar o Trigger (define quando a tarefa será executada)
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(jobName + "_TRIGGER", "AGENDAMENTO_GROUP")
                .startAt(startDate) // Data de início do período
                .endAt(endDate) // Data de fim do período
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression)
                        .withMisfireHandlingInstructionFireAndProceed())
                .build();

        // Agendar no Quartz
        if (scheduler.checkExists(job.getKey())) {
            // Se o job já existe (em caso de update), remove o antigo e agenda o novo.
            scheduler.deleteJob(job.getKey());
        }
        scheduler.scheduleJob(job, trigger);

        System.out.println("Agendado: " + jobName + " com Cron: " + cronExpression);
    }

    /**
     * Remove o agendamento do Quartz (usado após a exclusão no BD).
     */
    public void unscheduleAgendamento(int idAgendamento) throws SchedulerException {
        String jobLigarName = "ON_" + idAgendamento;
        String jobDesligarName = "OFF_" + idAgendamento;

        scheduler.deleteJob(new JobKey(jobLigarName, "AGENDAMENTO_GROUP"));
        scheduler.deleteJob(new JobKey(jobDesligarName, "AGENDAMENTO_GROUP"));

        System.out.println("Agendamento " + idAgendamento + " removido do Quartz.");
    }
}
