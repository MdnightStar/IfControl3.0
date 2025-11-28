package ifcontrol.mobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.button.MaterialButton;

public class ArCondActivity extends AppCompatActivity {

    // Componentes da UI
    private Spinner spinnerTemperaturaAr;
    private Spinner spinnerModoAr;
    private MaterialButton buttonEnviar;
    private MaterialButton buttonVoltar;

    // Variável para armazenar o número da sala
    private int nsala;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Configurações de Tela Cheia
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arcond);

        // Configura Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // 1. RECUPERAR O NÚMERO DA SALA
        // Verifica se veio com a chave "NUMERO_SALA" ou "nSala" para garantir compatibilidade
        nsala = getIntent().getIntExtra("NUMERO_SALA", 0);
        if (nsala == 0) nsala = getIntent().getIntExtra("nSala", 0);

        // Vincular Componentes
        spinnerTemperaturaAr = findViewById(R.id.spinnerTemperaturaAr);
        spinnerModoAr = findViewById(R.id.spinnerModoAr);
        buttonEnviar = findViewById(R.id.buttonEnviar);
        buttonVoltar = findViewById(R.id.buttonVoltar);

        configurarSpinners();
        configurarBotoes();
    }

    private void configurarSpinners() {
        // Adapter para o Spinner de Temperatura
        ArrayAdapter<CharSequence> adapterTemp = ArrayAdapter.createFromResource(this,
                R.array.temperaturas_array, R.layout.spinner_item);
        adapterTemp.setDropDownViewResource(R.layout.spinner_item);
        spinnerTemperaturaAr.setAdapter(adapterTemp);

        // Adapter para o Spinner de Modo
        ArrayAdapter<CharSequence> adapterModo = ArrayAdapter.createFromResource(this,
                R.array.modos_ar_array, R.layout.spinner_item);
        adapterModo.setDropDownViewResource(R.layout.spinner_item );
        spinnerModoAr.setAdapter(adapterModo);
    }

    private void configurarBotoes() {
        // 2. LÓGICA DO BOTÃO ENVIAR (Idêntica ao FrameSala.java)
        buttonEnviar.setOnClickListener(v -> {
            // Pega os valores selecionados
            String modoSelecionado = spinnerModoAr.getSelectedItem().toString();
            String tempSelecionada = spinnerTemperaturaAr.getSelectedItem().toString();

            // Monta o comando base
            StringBuilder funcao = new StringBuilder("AR");

            // Lógica do Swing adaptada:
            // Se for Auto ou Fan, envia apenas o modo. Se for Cool ou Dry, envia modo + temperatura.
            if (modoSelecionado.equals("Auto") || modoSelecionado.equals("Fan")) {
                funcao.append(modoSelecionado);
            } else {
                funcao.append(modoSelecionado);
                funcao.append(tempSelecionada);
            }

            // Adiciona o ponto final obrigatório do protocolo
            funcao.append(".");

            String comandoFinal = funcao.toString();

            // 3. ENVIO PELA REDE (Em Thread separada)
            new Thread(() -> {
                if (MainApp.sessaoInstance != null) {
                    try {
                        // Envia o comando
                        MainApp.sessaoInstance.trataAcao(comandoFinal, nsala);

                        // Feedback visual na Thread principal
                        runOnUiThread(() ->
                                Toast.makeText(ArCondActivity.this,
                                        "Comando enviado: " + comandoFinal,
                                        Toast.LENGTH_SHORT).show()
                        );
                    } catch (Exception e) {
                        runOnUiThread(() ->
                                Toast.makeText(ArCondActivity.this,
                                        "Erro ao enviar: " + e.getMessage(),
                                        Toast.LENGTH_SHORT).show()
                        );
                    }
                }
            }).start();
        });

        // Configura botão voltar
        buttonVoltar.setOnClickListener(v -> {
            // finish() fecha essa tela e volta automaticamente para a anterior (SalaActivity)
            // Isso é melhor que abrir uma nova Intent, pois mantém o estado da conexão
            finish();
        });
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
        if (id == R.id.config) {
            Toast.makeText(this, "Configurações", Toast.LENGTH_SHORT).show();
        }
        if (id == R.id.sobre) {
            Toast.makeText(this, "Sobre o app", Toast.LENGTH_SHORT).show();
        }
        return true;
    }
}
