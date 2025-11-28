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

    // Componentes Visuais
    private TextView textViewTituloSala;
    private TextView textViewSalaTemp, textViewSalaUmi, textViewSalaPres;
    private MaterialCardView cardArCond, cardLuz, cardDS;
    private TextView statusAr, statusLuz, statusDS;
    private ImageView iconAr, iconLuz, iconDS;
    private MaterialButton buttonVoltarSala;
    private boolean navegandoParaOutraTela = false;

    // Variáveis Lógicas
    private Sala sala;
    private int nsala;
    private boolean estadoAr, estadoDS, estadoLuzes;
    private Gson gs;
    private java.lang.reflect.Type tipoSala;

    // Cores
    private int colorOn = Color.parseColor("#4CAF50");
    private int colorOff = Color.parseColor("#D32F2F");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sala);

        gs = new Gson();
        this.tipoSala = new TypeToken<Sala>() {}.getType();

        // Recupera o número da sala
        nsala = getIntent().getIntExtra("NUMERO_SALA", 0);
        if (nsala == 0) nsala = getIntent().getIntExtra("nSala", 0);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        vincularViews();

        if (nsala != 0) textViewTituloSala.setText("Sala " + nsala);

        // Lógica de Rede Inicial
        new Thread(() -> {
            if (MainApp.sessaoInstance != null) {
                MainApp.sessaoInstance.trataAcao("OCP", nsala);
                String resposta = MainApp.sessaoInstance.getSala(nsala);

                if (resposta != null && resposta.contains("nSala")) {
                    sala = gs.fromJson(resposta, tipoSala);
                    estadoAr = sala.isEstadoAr();
                    estadoDS = sala.isEstadoDataShow();
                    estadoLuzes = sala.isEstadoLuzes();
                    runOnUiThread(this::atualizarInterface);
                }
                new Thread(new AtualizaDadosSala()).start();
            }
        }).start();

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
        /*
         * ==============================================
         * AR CONDICIONADO
         * ==============================================
         */

        // Clique Simples: Liga/Desliga (Toggle)
        cardArCond.setOnClickListener(v -> {
            new Thread(() -> {
                if (estadoAr) {
                    MainApp.sessaoInstance.trataAcao("AROFF.", nsala);
                } else {
                    MainApp.sessaoInstance.trataAcao("ARON.", nsala);
                }
            }).start();
        });

        // Clique Longo (Segurar): Abre a Tela de Configuração
        cardArCond.setOnLongClickListener(v -> {
            navegandoParaOutraTela = true;
            Intent intent = new Intent(SalaActivity.this, ArCondActivity.class);
            // É crucial passar o ID da sala para a próxima tela saber o que controlar
            intent.putExtra("NUMERO_SALA", nsala);
            startActivity(intent);

            // Retornar true indica que o evento foi consumido e NÃO deve acionar o click simples depois
            return true;
        });

        /*
         * ==============================================
         * LUZ (Apenas Toggle)
         * ==============================================
         */
        cardLuz.setOnClickListener(v -> {
            new Thread(() -> {
                if (estadoLuzes) {
                    MainApp.sessaoInstance.trataAcao("LZOFF.", nsala);
                } else {
                    MainApp.sessaoInstance.trataAcao("LZON.", nsala);
                }
            }).start();
        });

        /*
         * ==============================================
         * DATASHOW
         * ==============================================
         */

        // Clique Simples: Liga/Desliga
        cardDS.setOnClickListener(v -> {
            new Thread(() -> {
                if (estadoDS) {
                    MainApp.sessaoInstance.trataAcao("DSOFF.", nsala);
                } else {
                    MainApp.sessaoInstance.trataAcao("DSON.", nsala);
                }
            }).start();
        });

        // Clique Longo (Segurar): Abre o Controle Remoto
        cardDS.setOnLongClickListener(v -> {
            navegandoParaOutraTela = true;
            Intent intent = new Intent(SalaActivity.this, DataShowActivity.class);
            intent.putExtra("NUMERO_SALA", nsala);
            startActivity(intent);
            return true;
        });

        buttonVoltarSala.setOnClickListener(v -> finish());
    }

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

        // Atualiza Ar
        if (sala.isEstadoAr()) {
            statusAr.setText("Ligado");
            statusAr.setTextColor(colorOn);
            iconAr.setColorFilter(null);
        } else {
            statusAr.setText("Desligado");
            statusAr.setTextColor(colorOff);
            iconAr.setColorFilter(Color.GRAY);
        }

        // Atualiza Luz
        if (sala.isEstadoLuzes()) {
            statusLuz.setText("Ligada");
            statusLuz.setTextColor(colorOn);
            iconLuz.setColorFilter(null);
        } else {
            statusLuz.setText("Desligada");
            statusLuz.setTextColor(colorOff);
            iconLuz.setColorFilter(Color.GRAY);
        }

        // Atualiza DataShow
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

    private class AtualizaDadosSala implements Runnable {
        @Override
        public void run() {
            while (!isFinishing()) {
                try {
                    String resposta = MainApp.sessaoInstance.getSala(nsala);
                    if (resposta != null && resposta.contains("nSala")) {
                        sala = gs.fromJson(resposta, tipoSala);
                        runOnUiThread(() -> {
                            estadoAr = sala.isEstadoAr();
                            estadoDS = sala.isEstadoDataShow();
                            estadoLuzes = sala.isEstadoLuzes();
                            atualizarInterface();
                        });
                    }
                    Thread.sleep(3000);
                } catch (Exception e) {
                    Log.e("SalaActivity", "Erro atualização: " + e.getMessage());
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy(); // Sempre chame o super primeiro

        // Envia o comando em uma thread separada
        new Thread(() -> {
            if (MainApp.sessaoInstance != null) {
                try {
                    MainApp.sessaoInstance.trataAcao("DSC", nsala);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        SalaView.salaAberta = false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_simple, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
       // if (id == R.id.config) Toast.makeText(this, "Configurações", Toast.LENGTH_SHORT).show();
        //if (id == R.id.sobre) Toast.makeText(this, "Sobre o app", Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Resetamos a variável. Se o usuário sair agora, é para valer.
        navegandoParaOutraTela = false;
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Se a tela sumiu e NÃO estamos indo para um sub-menu (Ar ou DS),
        // significa que o usuário minimizou o app ou apertou Home.
        if (!navegandoParaOutraTela) {
            // O finish() vai forçar o encerramento da Activity,
            // o que fará o onDestroy() ser chamado logo em seguida.
            finish();
        }
    }
}