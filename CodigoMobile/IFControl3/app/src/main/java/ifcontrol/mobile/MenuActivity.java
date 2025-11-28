package ifcontrol.mobile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import Modelo.Sala;

public class MenuActivity extends AppCompatActivity implements SalasAdapter.OnSalaClickListener {

    private RecyclerView recyclerViewSalas;
    private SalasAdapter salasAdapter;
    private List<Sala> salas = new ArrayList<>();

    // Controle da Thread
    private volatile boolean runningUpdate = true;
    private Thread updateThread;

    // Objetos JSON
    private Gson gs;
    private java.lang.reflect.Type tipoSala;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Salas");
        }

        // Inicializa Gson
        gs = new Gson();
        tipoSala = new TypeToken<List<Sala>>() {}.getType();

        // Configura RecyclerView
        recyclerViewSalas = findViewById(R.id.recyclerViewSalas);
        salasAdapter = new SalasAdapter(new ArrayList<>(), this);

        // Decoração (Espaçamento)
        int spacingInPixels = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12, getResources().getDisplayMetrics());
        recyclerViewSalas.addItemDecoration(new GridSpacingItemDecoration(2, spacingInPixels, true));

        recyclerViewSalas.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerViewSalas.setAdapter(salasAdapter);

        // Menu Inferior (Navegação)
        setupBottomNavigation();

        // Inicia a Thread
        updateThread = new Thread(new AtulizaDadosSala());
        updateThread.start();
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bnv_botton);
        bottomNavigationView.setSelectedItemId(R.id.tab_salas); // Marca item atual

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.tab_acoes) {
                // 1. Para a thread desta tela
                stopUpdateThread();

                // 2. Vai para a tela de Ações
                Intent intent = new Intent(getApplicationContext(), AcoesActivity.class);
                startActivity(intent);

                // 3. Limpa e fecha a atual
                overridePendingTransition(0, 0);
                finish();
                return true;

            } else if (itemId == R.id.tab_salas) {
                // Já estamos aqui
                return true;
            }
            return false;
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopUpdateThread();
    }

    public void stopUpdateThread() {
        runningUpdate = false;
        if (updateThread != null && updateThread.isAlive()) {
            updateThread.interrupt();
        }
    }

    @Override
    public void onSalaClick(Sala sala) {
        Intent intent = new Intent(MenuActivity.this, SalaActivity.class);
        intent.putExtra("NUMERO_SALA", sala.getnSala());
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.sobre) {
            Intent intent = new Intent(this, SobreActivity.class);
            startActivity(intent);
            return true;
        }
        return true;
    }

    // --- Inner Class Runnable ---
    private class AtulizaDadosSala implements Runnable {
        @Override
        public void run() {
            while (runningUpdate) {
                // Pausa atualização se uma sala estiver aberta (lógica específica de Sala)
                while (runningUpdate && (!SalaView.salaAberta)) {
                    try {
                        String resposta = "";
                        if (MainApp.sessaoInstance != null) {
                            resposta = MainApp.sessaoInstance.salas();
                        }

                        if (resposta != null && resposta.contains("nSala")) {
                            List<Sala> todasSalas = gs.fromJson(resposta, tipoSala);
                            List<Sala> salasConectadas = new ArrayList<>();

                            for (Sala s : todasSalas) {
                                if (s.isEstadoDaConexao()) {
                                    salasConectadas.add(s);
                                }
                            }

                            // Ordenação
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                Collections.sort(salasConectadas, Comparator.comparingInt(Sala::getnSala));
                            } else {
                                Collections.sort(salasConectadas, (o1, o2) -> Integer.compare(o1.getnSala(), o2.getnSala()));
                            }

                            final List<Sala> finalSalas = salasConectadas;
                            runOnUiThread(() -> salasAdapter.atualizarDados(finalSalas));
                        }
                    } catch (Exception e) {
                        Log.e("MenuActivity", "Erro: " + e.getMessage());
                    }

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        Log.e("MenuActivity", "Thread interrompida");
                        break; // Sai do loop interno
                    }
                }
                // Pequeno sleep caso o loop interno (salaAberta) esteja bloqueando
                try { Thread.sleep(500); } catch (Exception e) {}
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SalaView.salaAberta = false;
    }
}
