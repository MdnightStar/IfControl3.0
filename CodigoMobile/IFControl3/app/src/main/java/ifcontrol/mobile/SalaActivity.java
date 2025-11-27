package ifcontrol.mobile;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import Modelo.Sala;

public class SalaActivity extends AppCompatActivity {

    // Componentes Visuais (Mantidos do XML existente)
    private TextView textViewTituloSala;
    private TextView textViewSalaTemp, textViewSalaUmi, textViewSalaPres;
    private MaterialCardView cardArCond, cardLuz, cardDS;
    private TextView statusAr, statusLuz, statusDS;
    private ImageView iconAr, iconLuz, iconDS;
    private MaterialButton buttonVoltarSala;

    // Variáveis Lógicas (Iguais ao FrameSala.java)
    private Sala sala;
    private int nsala;
    private boolean estadoAr, estadoDS, estadoLuzes;
    private Gson gs;
    private java.lang.reflect.Type tipoSala;

    // Cores (Auxiliar para UI Android)
    private int colorOn = Color.parseColor("#4CAF50");
    private int colorOff = Color.parseColor("#D32F2F");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sala);

        // 1. Inicialização de Variáveis e Gson
        gs = new Gson();
        this.tipoSala = new TypeToken<Sala>() {}.getType();

        // Recupera o número da sala (Segurança para pegar "nSala" ou "NUMERO_SALA")
        nsala = getIntent().getIntExtra("NUMERO_SALA", 0);
        if (nsala == 0) nsala = getIntent().getIntExtra("nSala", 0);

        // Configura Toolbar e Título
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        vincularViews(); // Linka os IDs do XML

        if (nsala != 0) textViewTituloSala.setText("Sala " + nsala);

        // 2. Lógica de Rede (Igual ao Construtor do FrameSala)
        // No Android, rede deve ser feita em Thread separada para não travar a tela
        new Thread(() -> {
            if (MainApp.sessaoInstance != null) {
                // Envia comando de ocupação (OCP)
                MainApp.sessaoInstance.trataAcao("OCP", nsala);

                // Busca dados iniciais da sala
                String resposta = MainApp.sessaoInstance.getSala(nsala);

                if (resposta != null && resposta.contains("nSala")) {
                    sala = gs.fromJson(resposta, tipoSala);

                    // Atualiza estados locais
                    estadoAr = sala.isEstadoAr();
                    estadoDS = sala.isEstadoDataShow();
                    estadoLuzes = sala.isEstadoLuzes();

                    // Atualiza UI inicial
                    runOnUiThread(this::atualizarInterface);
                }

                // Inicia a thread de atualização constante (Loop)
                new Thread(new AtualizaDadosSala()).start();
            }
        }).start();

        // 3. Configura os Cliques (Botões enviando os comandos exatos do Swing)
        configurarCliques();
    }

    private void vincularViews() {
        textViewTituloSala = findViewById(R.id.textViewTituloSala);
        textViewSalaTemp = findViewById(R.id.textViewSalaTemp);
        textViewSalaUmi = findViewById(R.id.textViewSalaUmi);
        textViewSalaPres = findViewById(R.id.textViewSalaPres);
        cardArCond = findViewById(R.id.cardArCond);
        statusAr = findViewById(R.id.statusAr);
        iconAr = findViewById(R.id.iconAr);
        cardLuz = findViewById(R.id.cardLuz);
        statusLuz = findViewById(R.id.statusLuz);
        iconLuz = findViewById(R.id.iconLuz);
        cardDS = findViewById(R.id.cardDS);
        statusDS = findViewById(R.id.statusDS);
        iconDS = findViewById(R.id.iconDS);
        buttonVoltarSala = findViewById(R.id.buttonVoltarSala);
    }

    private void configurarCliques() {
        // AR CONDICIONADO: Abre tela de controle (Igual sua lógica anterior, mas passando o ID)
        cardArCond.setOnClickListener(v -> {
            // Lógica de ligar/desligar rápido (Toggle) igual ao Swing jLabelArMouseClicked
            new Thread(() -> {
                if (estadoAr) {
                    MainApp.sessaoInstance.trataAcao("AROFF.", nsala);
                } else {
                    MainApp.sessaoInstance.trataAcao("ARON.", nsala);
                }
                // Opcional: Abrir a activity de controle detalhado se segurar o botão
                // ou manter como clique simples para toggle e criar um botão "Configurar"
            }).start();

            // Se quiser manter a ida para a tela de controle detalhado:
            // Intent intent = new Intent(SalaActivity.this, ArCondActivity.class);
            // intent.putExtra("NUMERO_SALA", nsala);
            // startActivity(intent);
        });

        // LUZ: Toggle (Igual ao Swing jLabelLuzMouseClicked)
        cardLuz.setOnClickListener(v -> {
            new Thread(() -> {
                if (estadoLuzes) {
                    MainApp.sessaoInstance.trataAcao("LZOFF.", nsala);
                } else {
                    MainApp.sessaoInstance.trataAcao("LZON.", nsala);
                }
            }).start();
        });

        // DATASHOW: Toggle (Igual ao Swing jLabelDSMouseClicked)
        cardDS.setOnClickListener(v -> {
            new Thread(() -> {
                if (estadoDS) {
                    MainApp.sessaoInstance.trataAcao("DSOFF.", nsala);
                } else {
                    MainApp.sessaoInstance.trataAcao("DSON.", nsala);
                }
                // Se quiser ir para a tela de controle do DS:
                // Intent intent = new Intent(SalaActivity.this, DataShowActivity.class);
                // intent.putExtra("NUMERO_SALA", nsala);
                // startActivity(intent);
            }).start();
        });

        buttonVoltarSala.setOnClickListener(v -> finish());
    }

    // Método auxiliar para atualizar visual (Icons e Textos)
    private void atualizarInterface() {
        if (sala == null) return;

        textViewSalaTemp.setText(sala.getTemperatura() + "ºC");
        textViewSalaUmi.setText(sala.getUmidade() + "%");

        if (sala.isPresenca()) {
            textViewSalaPres.setText("Com movimento");
            textViewSalaPres.setTextColor(colorOn);
        } else {
            textViewSalaPres.setText("Sem movimento");
            textViewSalaPres.setTextColor(Color.GRAY);
        }

        // Atualiza Estado Ar
        if (sala.isEstadoAr()) {
            statusAr.setText("Ligado");
            statusAr.setTextColor(colorOn);
            iconAr.setColorFilter(null);
        } else {
            statusAr.setText("Desligado");
            statusAr.setTextColor(colorOff);
            iconAr.setColorFilter(Color.GRAY);
        }

        // Atualiza Estado Luz
        if (sala.isEstadoLuzes()) {
            statusLuz.setText("Ligada");
            statusLuz.setTextColor(colorOn);
            iconLuz.setColorFilter(null);
        } else {
            statusLuz.setText("Desligada");
            statusLuz.setTextColor(colorOff);
            iconLuz.setColorFilter(Color.GRAY);
        }

        // Atualiza Estado DataShow
        if (sala.isEstadoDataShow()) {
            statusDS.setText("Ligado");
            statusDS.setTextColor(colorOn);
            iconDS.setColorFilter(null);
        } else {
            statusDS.setText("Desligado");
            statusDS.setTextColor(colorOff);
            iconDS.setColorFilter(Color.GRAY);
        }
    }

    /**
     * Classe Interna para Atualização de Dados (Baseada no FrameSala.java)
     */
    private class AtualizaDadosSala implements Runnable {

        @Override
        public void run() {
            // isDisplayable() do Swing vira !isFinishing() no Android
            while (!isFinishing()) {
                try {
                    String resposta = MainApp.sessaoInstance.getSala(nsala);

                    if (resposta != null && resposta.contains("nSala")) {
                        sala = gs.fromJson(resposta, tipoSala);

                        // SwingUtilities.invokeLater vira runOnUiThread
                        runOnUiThread(() -> {
                            // Atualiza estados internos
                            estadoAr = sala.isEstadoAr();
                            estadoDS = sala.isEstadoDataShow();
                            estadoLuzes = sala.isEstadoLuzes();

                            // Atualiza Tela
                            atualizarInterface();
                        });
                    }

                    Thread.sleep(3000); // 3 Segundos

                } catch (InterruptedException ex) {
                    Log.e("SalaActivity", "Thread interrompida");
                    break; // Sai do loop se interrompido
                } catch (Exception e) {
                    Log.e("SalaActivity", "Erro na atualização: " + e.getMessage());
                }
            }

            // Lógica pós-loop do Swing: Envia DSC e libera sala
            // Nota: No Android, é garantido chamar isso no onDestroy para evitar que não rode se o app fechar rápido
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Garante que o comando de desocupar (DSC) seja enviado ao sair da tela
        new Thread(() -> {
            if (MainApp.sessaoInstance != null) {
                // Formato DSC usado no FrameSala: trataAcao("DSC", nsala)
                MainApp.sessaoInstance.trataAcao("DSC", nsala);
            }
        }).start();

        // Reseta flag estática
        SalaView.salaAberta = false;
    }

    // Menus da Toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.config) Toast.makeText(this, "Configurações", Toast.LENGTH_SHORT).show();
        if (id == R.id.sobre) Toast.makeText(this, "Sobre o app", Toast.LENGTH_SHORT).show();
        return true;
    }
}