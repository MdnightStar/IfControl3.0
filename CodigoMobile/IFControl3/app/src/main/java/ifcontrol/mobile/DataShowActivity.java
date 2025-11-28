package ifcontrol.mobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.button.MaterialButton;

public class DataShowActivity extends AppCompatActivity {

    // Componentes Visuais
    private ImageView imageViewOk;
    private ImageView imageViewCima, imageViewBaixo, imageViewEsquerda, imageViewDireita;
    private ImageView imageViewEsc, imageViewFreeze, imageViewMenu;
    private MaterialButton buttonVoltar;

    // Variável da Sala
    private int nsala;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datashow);

        // Configura Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // 1. Recupera o número da sala vindo da Intent
        nsala = getIntent().getIntExtra("NUMERO_SALA", 0);
        if (nsala == 0) nsala = getIntent().getIntExtra("nSala", 0);

        // 2. Vincula os IDs do XML
        inicializarComponentes();

        // 3. Configura os Cliques (Enviando os comandos do protocolo)
        configurarCliques();
    }

    private void inicializarComponentes() {
        imageViewOk = findViewById(R.id.imageViewOK);
        imageViewCima = findViewById(R.id.imageViewCima);
        imageViewBaixo = findViewById(R.id.imageViewBaixo);
        imageViewEsquerda = findViewById(R.id.imageViewEsquerda);
        imageViewDireita = findViewById(R.id.imageViewDireita);

        imageViewFreeze = findViewById(R.id.imageViewFreeze);
        imageViewEsc = findViewById(R.id.imageViewEsc);
        imageViewMenu = findViewById(R.id.imageViewMenu);

        buttonVoltar = findViewById(R.id.buttonVoltar);
    }

    private void configurarCliques() {
        // Navegação (D-PAD)
        imageViewOk.setOnClickListener(v -> enviarComando("DSOK."));
        imageViewCima.setOnClickListener(v -> enviarComando("DSCIMA."));
        imageViewBaixo.setOnClickListener(v -> enviarComando("DSBAIXO."));
        imageViewEsquerda.setOnClickListener(v -> enviarComando("DSESQ."));
        imageViewDireita.setOnClickListener(v -> enviarComando("DSDIR."));

        // Funções
        imageViewFreeze.setOnClickListener(v -> enviarComando("DSFREEZE."));
        imageViewEsc.setOnClickListener(v -> enviarComando("DSESC."));
        imageViewMenu.setOnClickListener(v -> enviarComando("DSMENU."));

        // Botão Voltar (Fecha a tela e retorna à anterior)
        buttonVoltar.setOnClickListener(v -> finish());
    }

    /**
     * Método auxiliar para enviar comandos em background.
     * Evita repetir o código da Thread várias vezes.
     */
    private void enviarComando(String comando) {
        new Thread(() -> {
            if (MainApp.sessaoInstance != null) {
                try {
                    // Envia o comando para o servidor
                    MainApp.sessaoInstance.trataAcao(comando, nsala);

                    // Feedback opcional (Toast)
                    // runOnUiThread(() -> Toast.makeText(DataShowActivity.this, "Enviado: " + comando, Toast.LENGTH_SHORT).show());

                } catch (Exception e) {
                    runOnUiThread(() ->
                            Toast.makeText(DataShowActivity.this, "Erro: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                    );
                }
            } else {
                runOnUiThread(() ->
                        Toast.makeText(DataShowActivity.this, "Sem conexão", Toast.LENGTH_SHORT).show()
                );
            }
        }).start();
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
