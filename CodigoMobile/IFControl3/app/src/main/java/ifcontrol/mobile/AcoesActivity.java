package ifcontrol.mobile;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import Modelo.Acao;

public class AcoesActivity extends AppCompatActivity {

    // --- Views ---
    private RecyclerView recyclerViewAcoes;
    private AcoesAdapter acoesAdapter;

    // --- Lógica de Atualização ---
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private Thread backgroundUpdateThread;
    private volatile boolean isActivityRunning = false;

    // --- Processamento de Dados ---
    private List<Acao> acoes;
    private final Gson gs = new Gson();
    private final Type tipoAcao = new TypeToken<ArrayList<Acao>>() {}.getType();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acoes);

        // --- Configuração da Toolbar ---
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // --- Configuração do RecyclerView ---
        recyclerViewAcoes = findViewById(R.id.recyclerViewAcoes); // Use o ID do seu RecyclerView
        recyclerViewAcoes.setLayoutManager(new LinearLayoutManager(this));
        acoesAdapter = new AcoesAdapter();
        recyclerViewAcoes.setAdapter(acoesAdapter);

        // --- Configuração da Navegação Inferior ---
        setupBottomNavigation();
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bnv_botton);
        bottomNavigationView.setSelectedItemId(R.id.tab_acoes);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.tab_salas) {
                startActivity(new Intent(getApplicationContext(), MenuActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            } else if (itemId == R.id.tab_acoes) {
                return true;
            }
            return false;
        });
    }

    // --- Lógica do Loop de Atualização em Background ---

    private void startBackgroundUpdates() {
        isActivityRunning = true;
        backgroundUpdateThread = new Thread(() -> {
            while (isActivityRunning) {
                // 1. Pega os dados da rede na thread de background
                final String resp = MainApp.sessaoInstance.logs();

                if (resp != null && resp.contains("dataAcao")) {
                    // 2. Processa os dados (ainda na thread de background)
                    acoes = gs.fromJson(resp, tipoAcao);
                    ordenarAcoes(acoes);

                    // 3. Posta o resultado para a UI thread para atualizar a tela
                    mainHandler.post(() -> {
                        // Este código roda na thread principal, é seguro atualizar a UI aqui
                        acoesAdapter.updateAcoes(acoes);
                    });
                }

                try {
                    // 4. Espera 5 segundos antes da próxima atualização
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    // A thread foi interrompida (pelo onPause), então saia do loop
                    break;
                }
            }
        });
        backgroundUpdateThread.start();
    }

    private void stopBackgroundUpdates() {
        isActivityRunning = false;
        if (backgroundUpdateThread != null) {
            backgroundUpdateThread.interrupt();
        }
    }

    // Ordena a lista de ações, da mais nova para a mais antiga, usando o ID
    private void ordenarAcoes(List<Acao> lista) {
        // Usando a forma compatível com API 23
        Collections.sort(lista, new Comparator<Acao>() {
            @Override
            public int compare(Acao o1, Acao o2) {
                return Integer.compare(o2.getIdAcao(), o1.getIdAcao());
            }
        });
    }

    // --- Ciclo de Vida da Activity ---

    @Override
    protected void onResume() {
        super.onResume();
        // Inicia o loop de atualizações quando a tela fica visível
        startBackgroundUpdates();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Para o loop de atualizações quando a tela não está mais em primeiro plano
        stopBackgroundUpdates();
    }


    // --- Métodos do Menu da Toolbar ---
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
        } else if (id == R.id.sobre) {
            Toast.makeText(this, "Sobre o app", Toast.LENGTH_SHORT).show();
        }
        return true;
    }
}
