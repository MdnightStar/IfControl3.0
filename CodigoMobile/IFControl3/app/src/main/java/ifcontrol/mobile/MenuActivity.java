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

        // Inicializa Gson
        gs = new Gson();
        tipoSala = new TypeToken<List<Sala>>() {}.getType();

        // Configura RecyclerView
        recyclerViewSalas = findViewById(R.id.recyclerViewSalas);

        // Inicializa Adapter com lista vazia inicialmente
        salasAdapter = new SalasAdapter(new ArrayList<>(), this);

        // Decoração (Espaçamento)
        int spacingInPixels = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12, getResources().getDisplayMetrics());
        recyclerViewSalas.addItemDecoration(new GridSpacingItemDecoration(2, spacingInPixels, true));

        recyclerViewSalas.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerViewSalas.setAdapter(salasAdapter);

        // Menu Inferior
        BottomNavigationView bottomNavigationView = findViewById(R.id.bnv_botton);
        bottomNavigationView.setSelectedItemId(R.id.tab_salas);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.tab_acoes) {
                stopUpdateThread(); // Parar thread ao sair
                startActivity(new Intent(getApplicationContext(), AcoesActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            } else if (itemId == R.id.tab_salas) {
                return true;
            }
            return false;
        });

        // Inicia a Thread de atualização (Mesma lógica do PSala)
        updateThread = new Thread(new AtulizaDadosSala());
        updateThread.start();
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

    // Clique na sala (Vindo do Adapter -> SalaView -> Button)
    @Override
    public void onSalaClick(Sala sala) {
        // A flag estática salaAberta já foi setada como TRUE dentro da SalaView antes de chegar aqui
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
        if (id == R.id.config) {
            Toast.makeText(this, "Configurações", Toast.LENGTH_SHORT).show();
        }
        if (id == R.id.sobre) {
            Toast.makeText(this, "Sobre o app", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    /**
     * Classe Runnable idêntica à lógica do PSala.java
     */
    private class AtulizaDadosSala implements Runnable {

        @Override
        public void run() {
            while (runningUpdate) {
                // Verifica se NÃO há sala aberta (Pausa atualização se estiver em uma sala)
                // Nota: SalaView.salaAberta deve ser resetado para false quando voltar para esta tela (onResume)
                while (runningUpdate && (!SalaView.salaAberta)) {
                    try {
                        // Chama o servidor (Assumindo que MainApp e sessaoInstance existem e são estáticos)
                        // Caso não tenha MainApp, substitua pela sua lógica de chamada de socket
                        String resposta = "";

                        if (MainApp.sessaoInstance != null) {
                            resposta = MainApp.sessaoInstance.salas();
                        }

                        // Verifica se a resposta é válida
                        if (resposta != null && resposta.contains("nSala")) {

                            List<Sala> todasSalas = gs.fromJson(resposta, tipoSala);

                            // Filtra apenas salas conectadas (conforme lógica do PSala)
                            List<Sala> salasConectadas = new ArrayList<>();
                            for (Sala s : todasSalas) {
                                if (s.isEstadoDaConexao()) {
                                    salasConectadas.add(s);
                                }
                            }

                            // Ordena
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                Collections.sort(salasConectadas, Comparator.comparingInt(Sala::getnSala));
                            } else {
                                // Fallback para Android antigo
                                Collections.sort(salasConectadas, new Comparator<Sala>() {
                                    @Override
                                    public int compare(Sala o1, Sala o2) {
                                        return Integer.compare(o1.getnSala(), o2.getnSala());
                                    }
                                });
                            }

                            // Atualiza a UI na Thread Principal
                            final List<Sala> finalSalas = salasConectadas;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // Atualiza o adapter com a nova lista
                                    salasAdapter.atualizarDados(finalSalas);
                                }
                            });
                        }
                    } catch (Exception e) {
                        Log.e("MenuActivity", "Erro ao atualizar salas: " + e.getMessage());
                    }

                    try {
                        Thread.sleep(1000); // 1 segundo de intervalo
                    } catch (InterruptedException ex) {
                        Log.e("MenuActivity", "Thread interrompida");
                    }
                }

                // Pequeno sleep caso o loop interno (salaAberta) pare, para não fritar a CPU
                try { Thread.sleep(500); } catch (Exception e) {}
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Quando volta para o menu, libera a atualização novamente
        SalaView.salaAberta = false;
    }
}
