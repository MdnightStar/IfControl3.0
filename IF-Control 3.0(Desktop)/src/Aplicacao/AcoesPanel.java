/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package Aplicacao;

import java.util.Calendar;
import java.sql.Time;
import java.util.HashMap;
import java.util.Map;
/**
 *
 * @author LENOVO
 */
public class AcoesPanel extends javax.swing.JPanel {
    private int id;
    /**
     * Creates new form AgendamentoPanel
     */
    public AcoesPanel(String autor, String tipoAcao, String data, String hora, int id, int nSala) {
        initComponents();
        this.id=id;
        jLabelAutorEd.setText(autor);
        jLabelDataEd.setText(data);
        jLabelHoraEd.setText(hora);
        jLabelIDEd.setText(String.valueOf(id));
        jLabelSalaEd.setText(String.valueOf(nSala));
        
        if(tipoAcao.contains("ARON")){
            jLabelTipoAcaoEd.setText("Ligou o Ar");
        } else if(tipoAcao.contains("AROFF")){
            jLabelTipoAcaoEd.setText("Desligou o Ar");
        }else if(tipoAcao.contains("DSON")){
            jLabelTipoAcaoEd.setText("Ligou o DataShow");
        }else if(tipoAcao.contains("DSOFF")){
            jLabelTipoAcaoEd.setText("Desligou o DataShow");
        }else if(tipoAcao.contains("LZON")){
            jLabelTipoAcaoEd.setText("Ligou a luz");
        }else if(tipoAcao.contains("LZOFF")){
            jLabelTipoAcaoEd.setText("Desligou a luz");
        }else if(tipoAcao.contains("HA")||tipoAcao.contains("OCP")){
            jLabelTipoAcaoEd.setText("Ocupou a sala");
        }else if(tipoAcao.contains("HD")||tipoAcao.contains("DSC")){
            jLabelTipoAcaoEd.setText("Desocupou a sala");
        }else 
        {
           String resp=decodificar(tipoAcao);
           jLabelTipoAcaoEd.setText(resp);
        }
        
        
       
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
    
    public AcoesPanel(String autor, String tipoAcao, String data, String hora, int id) {
        initComponents();
        jLabelAutorEd.setText(autor);
        jLabelDataEd.setText(data);
        jLabelHoraEd.setText(hora);
        jLabelIDEd.setText(String.valueOf(id));
        jLabelSalaEd.setText("Sem sala");
        
        
        if(tipoAcao.contains("--addSala--")){
            jLabelTipoAcaoEd.setText("Adicionou uma sala");
        } else if(tipoAcao.contains("--addAgendamento--")){
            jLabelTipoAcaoEd.setText("Adicionou um agendamento");
        }else if(tipoAcao.contains("--deletAgendamento--")){
            jLabelTipoAcaoEd.setText("Apagou um agendamento");
        }else if(tipoAcao.contains("--editAgendamento--")){
            jLabelTipoAcaoEd.setText("Deletou um agendamento");
        }
    }
    
    /**
     * Decodifica uma string de comando formatada (ex: ARCOOL18, DSOK) em uma
     * descrição amigável.
     * * @param comando A string de comando a ser decodificada.
     * @return Uma string de descrição formatada ou uma mensagem de erro.
     */
    public static String decodificar(String comando) {
        if (comando == null || comando.isEmpty()) {
            return "Erro: Comando vazio.";
        }

        // Garante que o comando esteja em maiúsculas para facilitar a comparação
        String cmd = comando.toUpperCase().trim();

        if (cmd.startsWith("AR")) {
            return decodificarArCondicionado(cmd);
        } else if (cmd.startsWith("DS")) {
            return decodificarDataShow(cmd);
        } else {
            return "Erro: Comando desconhecido. Deve começar com 'AR' ou 'DS'.";
        }
    }

    private static String decodificarArCondicionado(String cmd) {
        // Mapeamento dos modos possíveis do Ar-condicionado
        Map<String, String> modos = new HashMap<>();
        modos.put("COOL", "Cool");
        modos.put("FAN", "Fan");
        modos.put("AUTO", "Auto");
        modos.put("DRY", "Dry");
        
        // 1. Tenta identificar o modo
        for (Map.Entry<String, String> entry : modos.entrySet()) {
            if (cmd.contains(entry.getKey())) {
                String modo = entry.getValue();
                
                // 2. Tenta extrair a temperatura (o que sobrar após o modo)
                String tempStr = cmd.substring(cmd.indexOf(entry.getKey()) + entry.getKey().length());

                try {
                    int temperatura = Integer.parseInt(tempStr);
                    
                    // 3. Valida a temperatura (16 a 25)
                    if (temperatura >= 16 && temperatura <= 25) {
                        return String.format("Ar-condicionado - Modo: %s e Temperatura: %d°C", modo, temperatura);
                    } else {
                        return String.format("Erro: Temperatura inválida (%d). Deve ser entre 16 e 25.", temperatura);
                    }
                } catch (NumberFormatException e) {
                    return "Erro: Formato de temperatura inválido após o modo.";
                }
            }
        }
        
        return "Erro: Modo de Ar-condicionado não reconhecido (Deve ser Cool, Fan, Auto ou Dry).";
    }

    private static String decodificarDataShow(String cmd) {
        // Lista de comandos possíveis do Datashow
        String[] comandosValidos = {"OK", "ESQ", "CIMA", "BAIXO", "DIR", "ESC", "FREEZER", "MENU"};

        // 1. Remove o prefixo "DS" e verifica o comando restante
        String comandoDS = cmd.substring(2);

        for (String valido : comandosValidos) {
            if (comandoDS.equals(valido)) {
                return String.format("Datashow: Comando '%s' executado.", valido);
            }
        }

        return "Erro: Comando de Datashow não reconhecido. Comandos válidos: " + String.join(", ", comandosValidos) + ".";
    }
    
    
    

    /**
     * This method is called from within the constructor to initialize t                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            
     * ++e form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSeparator1 = new javax.swing.JSeparator();
        jLabelIDEd = new javax.swing.JLabel();
        jLabelAutor = new javax.swing.JLabel();
        jLabelData = new javax.swing.JLabel();
        jLabelHora = new javax.swing.JLabel();
        jLabelTipoAcao = new javax.swing.JLabel();
        jLabelAutorEd = new javax.swing.JLabel();
        jLabelTipoAcaoEd = new javax.swing.JLabel();
        jLabelDataEd = new javax.swing.JLabel();
        jLabelHoraEd = new javax.swing.JLabel();
        jLabelID = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jLabelSala = new javax.swing.JLabel();
        jLabelSalaEd = new javax.swing.JLabel();

        setBackground(new java.awt.Color(153, 153, 153));
        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        setMaximumSize(new java.awt.Dimension(1600, 81));
        setMinimumSize(new java.awt.Dimension(1343, 81));
        setPreferredSize(new java.awt.Dimension(1343, 81));
        setRequestFocusEnabled(false);

        jSeparator1.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jLabelIDEd.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        jLabelIDEd.setForeground(new java.awt.Color(0, 0, 0));
        jLabelIDEd.setText("XXXX");

        jLabelAutor.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        jLabelAutor.setForeground(new java.awt.Color(0, 0, 0));
        jLabelAutor.setText("Usúario:");

        jLabelData.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        jLabelData.setForeground(new java.awt.Color(0, 0, 0));
        jLabelData.setText("Data: ");

        jLabelHora.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        jLabelHora.setForeground(new java.awt.Color(0, 0, 0));
        jLabelHora.setText("Hora:");

        jLabelTipoAcao.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        jLabelTipoAcao.setForeground(new java.awt.Color(0, 0, 0));
        jLabelTipoAcao.setText("Tipo ação:");

        jLabelAutorEd.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        jLabelAutorEd.setForeground(new java.awt.Color(0, 0, 0));
        jLabelAutorEd.setText("XXXXXXXXX");

        jLabelTipoAcaoEd.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        jLabelTipoAcaoEd.setForeground(new java.awt.Color(0, 0, 0));
        jLabelTipoAcaoEd.setText("XXXXXXXXX");

        jLabelDataEd.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        jLabelDataEd.setForeground(new java.awt.Color(0, 0, 0));
        jLabelDataEd.setText("XXXXXXXXX");

        jLabelHoraEd.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        jLabelHoraEd.setForeground(new java.awt.Color(0, 0, 0));
        jLabelHoraEd.setText("XXXXXXXXX");

        jLabelID.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        jLabelID.setForeground(new java.awt.Color(0, 0, 0));
        jLabelID.setText("ID:");

        jSeparator2.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jLabelSala.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        jLabelSala.setForeground(new java.awt.Color(0, 0, 0));
        jLabelSala.setText("Sala:");

        jLabelSalaEd.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 14)); // NOI18N
        jLabelSalaEd.setForeground(new java.awt.Color(0, 0, 0));
        jLabelSalaEd.setText("XXXXXXXXX");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabelTipoAcao)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelTipoAcaoEd))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabelAutor)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelAutorEd)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelHora)
                    .addComponent(jLabelData))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelDataEd)
                    .addComponent(jLabelHoraEd))
                .addGap(18, 18, 18)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabelID)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelIDEd)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabelSala)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabelSalaEd)
                        .addContainerGap(838, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1)
            .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabelData, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabelDataEd))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabelAutor)
                                .addComponent(jLabelAutorEd)))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabelHora)
                                .addComponent(jLabelHoraEd))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabelTipoAcao)
                                .addComponent(jLabelTipoAcaoEd)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelSala, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelSalaEd))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelIDEd)
                            .addComponent(jLabelID))))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabelAutor;
    private javax.swing.JLabel jLabelAutorEd;
    private javax.swing.JLabel jLabelData;
    private javax.swing.JLabel jLabelDataEd;
    private javax.swing.JLabel jLabelHora;
    private javax.swing.JLabel jLabelHoraEd;
    private javax.swing.JLabel jLabelID;
    private javax.swing.JLabel jLabelIDEd;
    private javax.swing.JLabel jLabelSala;
    private javax.swing.JLabel jLabelSalaEd;
    private javax.swing.JLabel jLabelTipoAcao;
    private javax.swing.JLabel jLabelTipoAcaoEd;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    // End of variables declaration//GEN-END:variables
}
