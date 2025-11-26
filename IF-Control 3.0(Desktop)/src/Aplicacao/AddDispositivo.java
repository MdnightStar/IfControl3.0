/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Aplicacao;

import Modelo.Dispositivo;
import com.google.gson.Gson;

import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import javax.crypto.AEADBadTagException;

import javax.swing.JRadioButton;
import javax.swing.JSpinner;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author cauaa
 */
public class AddDispositivo extends javax.swing.JFrame {

    private AddCodigosAr codAr;
    private AddCodigosDs codDs;
    private Gson gson;

    /**
     * Creates new form g
     */
    public AddDispositivo() {
        initComponents();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        codAr = new AddCodigosAr();
        codDs = new AddCodigosDs();
        gson=new Gson();

    }

    public boolean isPreenchido() {
        // Lista de todos os campos que precisam ser verificados
        JTextField[] campos = {
            jTextFieldBitMark,
            jTextFieldHeaderMark,
            jTextFieldHeaderSpace,
            jTextFieldOneSpace,
            jTextFieldPauseSpace,
            jTextFieldZeroSpace
        };

        for (JTextField campo : campos) {
            // Verifica se o campo é nulo ou se o texto (após trim) está vazio
            if (campo == null || campo.getText().trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public boolean podeSerConvertidoEmInt() {
        // Primeira verificação: se não estiver preenchido, já falha
        if (!isPreenchido()) {
            return false;
        }

        // Lista de todos os campos para teste
        JTextField[] campos = {
            jTextFieldBitMark,
            jTextFieldHeaderMark,
            jTextFieldHeaderSpace,
            jTextFieldOneSpace,
            jTextFieldPauseSpace,
            jTextFieldZeroSpace
        };

        for (JTextField campo : campos) {
            try {
                // Tenta converter o texto (limpo de espaços) em um inteiro
                Integer.parseInt(campo.getText().trim());
            } catch (NumberFormatException e) {
                // Se a conversão falhar para qualquer campo, retorna falso imediatamente
                return false;
            }
        }
        return true;
    }

    /**
     * Junta os valores dos campos em uma string formatada. Requer que os campos
     * estejam preenchidos e sejam inteiros válidos.
     *
     * * @return String formatada ou null se houver campos vazios/inválidos.
     */
    public String formatarConfiguracao() {
        // Se a validação não for feita antes, chame-a aqui:
        if (!podeSerConvertidoEmInt()) {
            // Retorna null ou uma string de erro, dependendo do requisito da aplicação.
            return null;
        }

        // Concatena os valores na ordem solicitada: 
        // [ONE_SPACE, ZERO_SPACE, BIT_MARK, PAUSE_SPACE, HEADER_MARK, HEADER_SPACE]
        StringBuilder sb = new StringBuilder();
        sb.append("[");

        // ONE_SPACE
        sb.append(jTextFieldOneSpace.getText().trim()).append(",");

        // ZERO_SPACE
        sb.append(jTextFieldZeroSpace.getText().trim()).append(",");

        // BIT_MARK
        sb.append(jTextFieldBitMark.getText().trim()).append(",");

        // PAUSE_SPACE
        sb.append(jTextFieldPauseSpace.getText().trim()).append(",");

        // HEADER_MARK
        sb.append(jTextFieldHeaderMark.getText().trim()).append(",");

        // HEADER_SPACE (Último elemento, sem vírgula no final)
        sb.append(jTextFieldHeaderSpace.getText().trim());

        sb.append("]");

        return sb.toString();
    }

    /**
     * Analisa a string de salas ("1, 3, 6" ou "1-3") e retorna um array de
     * inteiros. (Esta lógica não precisa de alteração, pois é independente do
     * tipo de componente.)
     */
    private int[] analisarStringSalas(String salasStr) throws NumberFormatException {
        if (salasStr == null || salasStr.trim().isEmpty()) {
            return new int[0];
        }

        salasStr = salasStr.replaceAll("\\s+", ""); // Remove espaços em branco
        ArrayList<Integer> salas = new ArrayList<>();

        String[] partes = salasStr.split(",");

        for (String parte : partes) {
            if (parte.contains("-")) {
                // Caso de range: "1-3"
                String[] range = parte.split("-");
                if (range.length == 2) {
                    try {
                        int inicio = Integer.parseInt(range[0]);
                        int fim = Integer.parseInt(range[1]);
                        for (int i = inicio; i <= fim; i++) {
                            salas.add(i);
                        }
                    } catch (NumberFormatException e) {
                        throw new NumberFormatException("Formato de range inválido na parte: " + parte);
                    }
                } else {
                    throw new NumberFormatException("Formato de range incompleto: " + parte);
                }
            } else {
                // Caso de número único: "3"
                try {
                    salas.add(Integer.parseInt(parte));
                } catch (NumberFormatException e) {
                    throw new NumberFormatException("Número de sala inválido: " + parte);
                }
            }
        }

        // Converte a ArrayList para int[] e retorna
        return salas.stream().mapToInt(i -> i).toArray();
    }
    
    public void terminar(Dispositivo dis){
        String envio=gson.toJson(dis);
        String resp=MainApp.sessao.trataAcao("--addDispositivo--"+envio);
        if(resp.contains("OK")){
            JOptionPane.showMessageDialog(null, "Cadastro do dispositivo reaalizado com sucesso", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            codAr.dispose();
            codDs.dispose();
        }else{
            JOptionPane.showMessageDialog(null, "Erro no banco de dados", "Erro", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jTextFieldMarca = new javax.swing.JTextField();
        jButtonAdd = new javax.swing.JButton();
        jLabelIF = new javax.swing.JLabel();
        jLabelIfamLogo = new javax.swing.JLabel();
        jLabelAdicionarDispositivo = new javax.swing.JLabel();
        jLabelMarca = new javax.swing.JLabel();
        jLabelModelo = new javax.swing.JLabel();
        jButtonCancelar1 = new javax.swing.JButton();
        jTextFieldSalasRe = new javax.swing.JTextField();
        jLabelModelo1 = new javax.swing.JLabel();
        jCheckBoxAR = new javax.swing.JCheckBox();
        jCheckBoxDS = new javax.swing.JCheckBox();
        jLabelSalaRe = new javax.swing.JLabel();
        jTextFieldModelo1 = new javax.swing.JTextField();
        jLabelCodigos = new javax.swing.JLabel();
        jButtonAddCod = new javax.swing.JButton();
        jLabelOneSpace = new javax.swing.JLabel();
        jLabelZeroSpace = new javax.swing.JLabel();
        jLabelHeaderMark = new javax.swing.JLabel();
        jLabelHeaderSpace = new javax.swing.JLabel();
        jLabelBitMark = new javax.swing.JLabel();
        jLabelPauseSpace = new javax.swing.JLabel();
        jTextFieldZeroSpace = new javax.swing.JTextField();
        jTextFieldPauseSpace = new javax.swing.JTextField();
        jTextFieldOneSpace = new javax.swing.JTextField();
        jTextFieldHeaderMark = new javax.swing.JTextField();
        jTextFieldHeaderSpace = new javax.swing.JTextField();
        jTextFieldBitMark = new javax.swing.JTextField();
        jLabelDesc1 = new javax.swing.JLabel();
        jLabelDesc2 = new javax.swing.JLabel();
        jLabelAdicionarDispositivo1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new java.awt.GridLayout(1, 0));

        jPanel1.setBackground(new java.awt.Color(0, 51, 102));

        jTextFieldMarca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldMarcaActionPerformed(evt);
            }
        });

        jButtonAdd.setBackground(new java.awt.Color(0, 153, 51));
        jButtonAdd.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 12)); // NOI18N
        jButtonAdd.setForeground(new java.awt.Color(255, 255, 255));
        jButtonAdd.setText("Adicionar");
        jButtonAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddActionPerformed(evt);
            }
        });

        jLabelIF.setBackground(new java.awt.Color(187, 187, 187));
        jLabelIF.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 18)); // NOI18N
        jLabelIF.setForeground(new java.awt.Color(255, 255, 255));
        jLabelIF.setText("IFControl 3.0");

        jLabelIfamLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagens/IFAM_logo.png"))); // NOI18N
        jLabelIfamLogo.setText("jLabel1");

        jLabelAdicionarDispositivo.setBackground(new java.awt.Color(187, 187, 187));
        jLabelAdicionarDispositivo.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 18)); // NOI18N
        jLabelAdicionarDispositivo.setForeground(new java.awt.Color(255, 255, 255));
        jLabelAdicionarDispositivo.setText("Configuração do sinal");

        jLabelMarca.setBackground(new java.awt.Color(255, 255, 255));
        jLabelMarca.setFont(new java.awt.Font("Microsoft JhengHei UI", 1, 14)); // NOI18N
        jLabelMarca.setForeground(new java.awt.Color(255, 255, 255));
        jLabelMarca.setText("Marca:");

        jLabelModelo.setBackground(new java.awt.Color(255, 255, 255));
        jLabelModelo.setFont(new java.awt.Font("Microsoft JhengHei UI", 1, 14)); // NOI18N
        jLabelModelo.setForeground(new java.awt.Color(255, 255, 255));
        jLabelModelo.setText("Modelo:");

        jButtonCancelar1.setBackground(new java.awt.Color(0, 51, 102));
        jButtonCancelar1.setFont(new java.awt.Font("Microsoft YaHei UI", 0, 12)); // NOI18N
        jButtonCancelar1.setForeground(new java.awt.Color(255, 255, 255));
        jButtonCancelar1.setText("Cancelar");
        jButtonCancelar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelar1ActionPerformed(evt);
            }
        });

        jTextFieldSalasRe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldSalasReActionPerformed(evt);
            }
        });

        jLabelModelo1.setBackground(new java.awt.Color(255, 255, 255));
        jLabelModelo1.setFont(new java.awt.Font("Microsoft JhengHei UI", 1, 14)); // NOI18N
        jLabelModelo1.setForeground(new java.awt.Color(255, 255, 255));
        jLabelModelo1.setText("Tipo:");

        jCheckBoxAR.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jCheckBoxAR.setText("Ar-condicionado");

        jCheckBoxDS.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jCheckBoxDS.setText("DataShow");

        jLabelSalaRe.setBackground(new java.awt.Color(255, 255, 255));
        jLabelSalaRe.setFont(new java.awt.Font("Microsoft JhengHei UI", 1, 14)); // NOI18N
        jLabelSalaRe.setForeground(new java.awt.Color(255, 255, 255));
        jLabelSalaRe.setText("Salas relacionadas:");

        jTextFieldModelo1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldModelo1ActionPerformed(evt);
            }
        });

        jLabelCodigos.setBackground(new java.awt.Color(255, 255, 255));
        jLabelCodigos.setFont(new java.awt.Font("Microsoft JhengHei UI", 1, 14)); // NOI18N
        jLabelCodigos.setForeground(new java.awt.Color(255, 255, 255));
        jLabelCodigos.setText("Codigos:");

        jButtonAddCod.setBackground(new java.awt.Color(255, 102, 51));
        jButtonAddCod.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 12)); // NOI18N
        jButtonAddCod.setForeground(new java.awt.Color(255, 255, 255));
        jButtonAddCod.setText("Adiconar os códigos");
        jButtonAddCod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddCodActionPerformed(evt);
            }
        });

        jLabelOneSpace.setBackground(new java.awt.Color(255, 255, 255));
        jLabelOneSpace.setFont(new java.awt.Font("Microsoft JhengHei UI", 1, 14)); // NOI18N
        jLabelOneSpace.setForeground(new java.awt.Color(255, 255, 255));
        jLabelOneSpace.setText("One space:");

        jLabelZeroSpace.setBackground(new java.awt.Color(255, 255, 255));
        jLabelZeroSpace.setFont(new java.awt.Font("Microsoft JhengHei UI", 1, 14)); // NOI18N
        jLabelZeroSpace.setForeground(new java.awt.Color(255, 255, 255));
        jLabelZeroSpace.setText("Zero space:");

        jLabelHeaderMark.setBackground(new java.awt.Color(255, 255, 255));
        jLabelHeaderMark.setFont(new java.awt.Font("Microsoft JhengHei UI", 1, 14)); // NOI18N
        jLabelHeaderMark.setForeground(new java.awt.Color(255, 255, 255));
        jLabelHeaderMark.setText("Header mark:");

        jLabelHeaderSpace.setBackground(new java.awt.Color(255, 255, 255));
        jLabelHeaderSpace.setFont(new java.awt.Font("Microsoft JhengHei UI", 1, 14)); // NOI18N
        jLabelHeaderSpace.setForeground(new java.awt.Color(255, 255, 255));
        jLabelHeaderSpace.setText("Header space:");

        jLabelBitMark.setBackground(new java.awt.Color(255, 255, 255));
        jLabelBitMark.setFont(new java.awt.Font("Microsoft JhengHei UI", 1, 14)); // NOI18N
        jLabelBitMark.setForeground(new java.awt.Color(255, 255, 255));
        jLabelBitMark.setText("Bit mark:");

        jLabelPauseSpace.setBackground(new java.awt.Color(255, 255, 255));
        jLabelPauseSpace.setFont(new java.awt.Font("Microsoft JhengHei UI", 1, 14)); // NOI18N
        jLabelPauseSpace.setForeground(new java.awt.Color(255, 255, 255));
        jLabelPauseSpace.setText("Pause space:");

        jTextFieldZeroSpace.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldZeroSpaceActionPerformed(evt);
            }
        });

        jTextFieldPauseSpace.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldPauseSpaceActionPerformed(evt);
            }
        });

        jTextFieldOneSpace.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldOneSpaceActionPerformed(evt);
            }
        });

        jTextFieldHeaderMark.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldHeaderMarkActionPerformed(evt);
            }
        });

        jTextFieldHeaderSpace.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldHeaderSpaceActionPerformed(evt);
            }
        });

        jTextFieldBitMark.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldBitMarkActionPerformed(evt);
            }
        });

        jLabelDesc1.setBackground(new java.awt.Color(187, 187, 187));
        jLabelDesc1.setText("Em salas diferentes separe com \",\" Exemplo: \"1, 3, 6\"");

        jLabelDesc2.setBackground(new java.awt.Color(187, 187, 187));
        jLabelDesc2.setText("Em salas sequenciais utilizar \"-\" Exemplo: \"1-3\"");

        jLabelAdicionarDispositivo1.setBackground(new java.awt.Color(187, 187, 187));
        jLabelAdicionarDispositivo1.setFont(new java.awt.Font("Microsoft YaHei UI", 1, 24)); // NOI18N
        jLabelAdicionarDispositivo1.setForeground(new java.awt.Color(255, 255, 255));
        jLabelAdicionarDispositivo1.setText("Adicionar Dispositivo");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabelCodigos)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonAddCod)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jButtonCancelar1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabelIfamLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabelSalaRe)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabelMarca)
                                .addGap(32, 32, 32)
                                .addComponent(jTextFieldMarca))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabelModelo)
                                    .addComponent(jLabelModelo1))
                                .addGap(20, 20, 20)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jCheckBoxAR)
                                        .addGap(18, 18, 18)
                                        .addComponent(jCheckBoxDS)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addComponent(jTextFieldModelo1))))
                        .addGap(44, 44, 44))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabelIF))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabelOneSpace)
                                    .addComponent(jLabelZeroSpace))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jTextFieldZeroSpace, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextFieldOneSpace, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(jLabelBitMark)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jTextFieldBitMark, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(jLabelPauseSpace)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jTextFieldPauseSpace, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGap(6, 6, 6)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(jLabelHeaderSpace)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jTextFieldHeaderSpace, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(jLabelHeaderMark)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jTextFieldHeaderMark, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(78, 78, 78)
                                        .addComponent(jLabelAdicionarDispositivo))))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(150, 150, 150)
                                .addComponent(jTextFieldSalasRe, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabelDesc1, javax.swing.GroupLayout.PREFERRED_SIZE, 371, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabelDesc2, javax.swing.GroupLayout.PREFERRED_SIZE, 371, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addContainerGap())))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(331, 331, 331)
                .addComponent(jButtonAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(385, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                    .addContainerGap(286, Short.MAX_VALUE)
                    .addComponent(jLabelAdicionarDispositivo1)
                    .addGap(279, 279, 279)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelIF)
                .addGap(87, 87, 87)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldMarca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelMarca))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelModelo)
                    .addComponent(jTextFieldModelo1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelModelo1)
                    .addComponent(jCheckBoxAR)
                    .addComponent(jCheckBoxDS))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelSalaRe)
                            .addComponent(jTextFieldSalasRe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelCodigos)
                            .addComponent(jButtonAddCod)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(jLabelDesc1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelDesc2)))
                .addGap(24, 24, 24)
                .addComponent(jLabelAdicionarDispositivo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelBitMark)
                    .addComponent(jLabelOneSpace)
                    .addComponent(jLabelHeaderMark)
                    .addComponent(jTextFieldOneSpace, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldHeaderMark, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldBitMark, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelPauseSpace)
                    .addComponent(jLabelZeroSpace)
                    .addComponent(jLabelHeaderSpace)
                    .addComponent(jTextFieldZeroSpace, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldPauseSpace, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldHeaderSpace, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 79, Short.MAX_VALUE)
                .addComponent(jButtonAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabelIfamLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jButtonCancelar1)
                        .addGap(41, 41, 41))))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(77, 77, 77)
                    .addComponent(jLabelAdicionarDispositivo1)
                    .addContainerGap(522, Short.MAX_VALUE)))
        );

        getContentPane().add(jPanel1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonCancelar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelar1ActionPerformed
        dispose();
    }//GEN-LAST:event_jButtonCancelar1ActionPerformed

    private void jButtonAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddActionPerformed
        String marca = jTextFieldMarca.getText();
        String modelo = jTextFieldModelo1.getText();
        String salas = jTextFieldSalasRe.getText();
        Dispositivo dis = new Dispositivo();
        int[] salasR;

        if (marca.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Campo marca vazio", "Erro de Validação", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (modelo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Campo modelo vazio", "Erro de Validação", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (salas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Campo salas relaciondas vazio", "Erro de Validação", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            salasR = analisarStringSalas(salas);
        } catch (NumberFormatException e) {
            // Exibir erro específico de formato de salas (que é um erro diferente de campo vazio)
            JOptionPane.showMessageDialog(this, "Erro no formato das Salas: " + e.getMessage(), "Erro de Formato", JOptionPane.ERROR_MESSAGE);
            System.err.println("Erro de formato de salas: " + e.getMessage());
            return;
        }
        if (!(jCheckBoxAR.isSelected() || jCheckBoxDS.isSelected())) {
            JOptionPane.showMessageDialog(this, "Selecione um tipo", "Erro de Validação", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!isPreenchido()) {
            JOptionPane.showMessageDialog(this, "Configurações do sinal imcompletas", "Erro de Validação", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!podeSerConvertidoEmInt()) {
            JOptionPane.showMessageDialog(this, "Somente números nas configurações no sinal", "Erro de Validação", JOptionPane.WARNING_MESSAGE);
            return;
        }

        dis.setMarca(marca);
        dis.setModelo(modelo);
        dis.setSalasRelacionadas(salasR);
        dis.setConfig(formatarConfiguracao());

        if (jCheckBoxAR.isSelected()) {
            dis.setTipo("AR");
            if (codAr.isCompletou()) {
                dis.setListaCodigos(codAr.getListaCod());
                terminar(dis);
            } else {
                int resposta = JOptionPane.showConfirmDialog(
                        null, // Componente pai (null para centrar na tela)
                        "Nem um cod ir adicionado; Deseja procesguir?", // A mensagem a ser exibida
                        "Deseja?", // O título da janela
                        JOptionPane.YES_NO_OPTION, // As opções de botões (Sim e Não)
                        JOptionPane.QUESTION_MESSAGE // O ícone da caixa de diálogo

                );
                if (resposta == JOptionPane.YES_OPTION) {
                    dis.setListaCodigos(new ArrayList<>());
                    terminar(dis);
                }
            }
        }else{
            dis.setTipo("DS");
            if (codDs.isCompletou()) {
                dis.setListaCodigos(codDs.getListaCod());
                terminar(dis);
            } else {
                int resposta = JOptionPane.showConfirmDialog(
                        null, // Componente pai (null para centrar na tela)
                        "Nem um cod ir adicionado; Deseja procesguir?", // A mensagem a ser exibida
                        "Deseja?", // O título da janela
                        JOptionPane.YES_NO_OPTION, // As opções de botões (Sim e Não)
                        JOptionPane.QUESTION_MESSAGE // O ícone da caixa de diálogo

                );
                if (resposta == JOptionPane.YES_OPTION) {
                    dis.setListaCodigos(new ArrayList<>());
                    terminar(dis);
                }
            }
        }
    }//GEN-LAST:event_jButtonAddActionPerformed

    private void jTextFieldMarcaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldMarcaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldMarcaActionPerformed

    private void jTextFieldSalasReActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldSalasReActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldSalasReActionPerformed

    private void jTextFieldModelo1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldModelo1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldModelo1ActionPerformed

    private void jButtonAddCodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddCodActionPerformed
        // TODO add your handling code here:
        if ((jCheckBoxAR.isSelected() || jCheckBoxDS.isSelected())) {
            if (jCheckBoxAR.isSelected()) {
                codAr.setVisible(true);
            }else{
                codDs.setVisible(true);
            }
        }else{
            JOptionPane.showMessageDialog(this, "Selecione um tipo primeiro", "Erro de Validação", JOptionPane.WARNING_MESSAGE);
            
        }
    }//GEN-LAST:event_jButtonAddCodActionPerformed

    private void jTextFieldZeroSpaceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldZeroSpaceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldZeroSpaceActionPerformed

    private void jTextFieldPauseSpaceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldPauseSpaceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldPauseSpaceActionPerformed

    private void jTextFieldOneSpaceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldOneSpaceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldOneSpaceActionPerformed

    private void jTextFieldHeaderMarkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldHeaderMarkActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldHeaderMarkActionPerformed

    private void jTextFieldHeaderSpaceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldHeaderSpaceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldHeaderSpaceActionPerformed

    private void jTextFieldBitMarkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldBitMarkActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldBitMarkActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AddDispositivo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AddDispositivo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AddDispositivo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AddDispositivo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AddDispositivo().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAdd;
    private javax.swing.JButton jButtonAddCod;
    private javax.swing.JButton jButtonCancelar1;
    private javax.swing.JCheckBox jCheckBoxAR;
    private javax.swing.JCheckBox jCheckBoxDS;
    private javax.swing.JLabel jLabelAdicionarDispositivo;
    private javax.swing.JLabel jLabelAdicionarDispositivo1;
    private javax.swing.JLabel jLabelBitMark;
    private javax.swing.JLabel jLabelCodigos;
    private javax.swing.JLabel jLabelDesc1;
    private javax.swing.JLabel jLabelDesc2;
    private javax.swing.JLabel jLabelHeaderMark;
    private javax.swing.JLabel jLabelHeaderSpace;
    private javax.swing.JLabel jLabelIF;
    private javax.swing.JLabel jLabelIfamLogo;
    private javax.swing.JLabel jLabelMarca;
    private javax.swing.JLabel jLabelModelo;
    private javax.swing.JLabel jLabelModelo1;
    private javax.swing.JLabel jLabelOneSpace;
    private javax.swing.JLabel jLabelPauseSpace;
    private javax.swing.JLabel jLabelSalaRe;
    private javax.swing.JLabel jLabelZeroSpace;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField jTextFieldBitMark;
    private javax.swing.JTextField jTextFieldHeaderMark;
    private javax.swing.JTextField jTextFieldHeaderSpace;
    private javax.swing.JTextField jTextFieldMarca;
    private javax.swing.JTextField jTextFieldModelo1;
    private javax.swing.JTextField jTextFieldOneSpace;
    private javax.swing.JTextField jTextFieldPauseSpace;
    private javax.swing.JTextField jTextFieldSalasRe;
    private javax.swing.JTextField jTextFieldZeroSpace;
    // End of variables declaration//GEN-END:variables
}
