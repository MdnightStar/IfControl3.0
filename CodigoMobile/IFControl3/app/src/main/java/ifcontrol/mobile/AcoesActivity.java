package ifcontrol.mobile;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Modelo.Acao;

public class AcoesActivity extends AppCompatActivity {

    private RecyclerView recyclerViewAcoes;
    private AcoesAdapter acoesAdapter;

    // Controle da Thread
    private volatile boolean runningUpdate = true;
    // Flag para saber se estamos vendo uma pesquisa ou dados em tempo real
    private boolean isFiltering = false;
    private Thread updateThread;

    // Processamento de Dados
    private final Gson gs = new Gson();
    private final Type tipoAcao = new TypeToken<ArrayList<Acao>>() {}.getType();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acoes);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Ações");
        }

        // RecyclerView
        recyclerViewAcoes = findViewById(R.id.recyclerViewAcoes);
        recyclerViewAcoes.setLayoutManager(new LinearLayoutManager(this));
        acoesAdapter = new AcoesAdapter();
        recyclerViewAcoes.setAdapter(acoesAdapter);

        setupBottomNavigation();

        // Inicia a Thread de atualização
        updateThread = new Thread(new AtualizaDadosAcao());
        updateThread.start();
    }

    // --- MENU DA TOOLBAR (PESQUISAR) ---
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Certifique-se que o seu toolbar_menu.xml tem um item com id "action_search" (lupa)
        // e um item "action_refresh" ou "limpar" seria bom também.
        getMenuInflater().inflate(R.menu.toolbar_menuacao, menu);

        // Se quiser adicionar a lupa via código caso não tenha no XML:
        // menu.add(0, R.id.action_search, 0, "Pesquisar").setIcon(android.R.drawable.ic_menu_search).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        // Botão de Pesquisa (Lupa)
        if (id == R.id.action_search) { // Certifique-se de criar esse ID no seu menu XML
            showSearchDialog();
            return true;
        }

        // Botão para limpar filtro e voltar ao "Ao Vivo"
        if (id == R.id.action_refresh || id == R.id.action_clear) { // Sugestão de ID
            isFiltering = false;
            Toast.makeText(this, "Voltando para atualização em tempo real...", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (id == R.id.sobre) {
            // Abre a tela de Sobre
            Intent intent = new Intent(this, SobreActivity.class);
            startActivity(intent);
            return true;
        }

        return true;
    }

    // --- LÓGICA DO DIÁLOGO DE PESQUISA ---
    private void showSearchDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_pesquisar, null);
        builder.setView(dialogView);

        // Referências aos campos do layout
        final EditText etAutor = dialogView.findViewById(R.id.etAutor);
        final EditText etSala = dialogView.findViewById(R.id.etSala);
        final CheckBox cbSemSala = dialogView.findViewById(R.id.cbSemSala);
        final Spinner spinnerTipoAcao = dialogView.findViewById(R.id.spinnerTipoAcao);
        final Button btnData = dialogView.findViewById(R.id.btnSelecionarData);
        final TextView tvData = dialogView.findViewById(R.id.tvDataSelecionada);

        // Configurar Checkbox Sala
        cbSemSala.setOnCheckedChangeListener((buttonView, isChecked) -> etSala.setEnabled(!isChecked));

        // Configurar Spinner (Copiado do seu Swing)
        String[] tipos = { "Todos", "Ocupou a sala (OCP)", "Desocupou a sala (DSC)", "Ligou o ar (ARON)",
                "Desligou o ar (AROFF)", "Ligou a luz (LZON)", "Desligou a luz (LZOFF)",
                "Ligou a o datashow (DSON)", "Desligou o datashow (DSOFF)", "Adicionou uma sala (addSala)",
                "Adicionou um agendamento (addAgendamento)", "Editou um agendamento (editAgendamento)",
                "Deletou um agendamento (deletAgendamento)","Adicionou um dipositivo (addDispositivo)",
                "Editou um dipositivo (editDispositivo)", "Deletou um dipositivo (deletDispositivo)" };

        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tipos);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipoAcao.setAdapter(adapterSpinner);

        // Configurar Data Picker
        final Calendar dataSelecionada = Calendar.getInstance();
        final boolean[] dataFoiEscolhida = {false}; // Hack para saber se o user escolheu data

        btnData.setOnClickListener(v -> {
            DatePickerDialog dpd = new DatePickerDialog(AcoesActivity.this,
                    (view, year, month, dayOfMonth) -> {
                        dataSelecionada.set(year, month, dayOfMonth);
                        dataFoiEscolhida[0] = true;
                        tvData.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    },
                    dataSelecionada.get(Calendar.YEAR),
                    dataSelecionada.get(Calendar.MONTH),
                    dataSelecionada.get(Calendar.DAY_OF_MONTH));
            dpd.show();
        });

        builder.setPositiveButton("Pesquisar", (dialog, which) -> {
            // 1. Coleta dados
            String autor = etAutor.getText().toString();
            String salaStr = etSala.getText().toString();
            String tipoSelecionado = spinnerTipoAcao.getSelectedItem().toString();
            boolean semSala = cbSemSala.isChecked();

            Calendar dataFiltro = dataFoiEscolhida[0] ? dataSelecionada : null;

            // 2. Chama a filtragem
            filtrarAcoes(autor, salaStr, semSala, tipoSelecionado, dataFiltro);
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.white));

        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.WHITE);
    }

    private void filtrarAcoes(String autor, String salaStr, boolean semSala, String tipoCombo, Calendar dataFiltro) {
        // Pausa a atualização automática para mostrar o resultado da pesquisa
        isFiltering = true;

        // Mostra loading rápido (opcional, Toast por enquanto)
        Toast.makeText(this, "Pesquisando...", Toast.LENGTH_SHORT).show();

        new Thread(() -> {
            try {
                // 1. Busca TODAS as ações do servidor (igual ao Swing)
                String resp = "";
                if (MainApp.sessaoInstance != null) {
                    resp = MainApp.sessaoInstance.logs();
                }

                if (resp != null && !resp.isEmpty() && resp.contains("dataAcao")) {
                    List<Acao> todasAcoes = gs.fromJson(resp, tipoAcao);
                    List<Acao> acoesFiltradas = new ArrayList<>();

                    // Extrai a sigla do tipo (Regex portado do Swing)
                    String tipoSigla = procurarTipoAcao(tipoCombo);

                    int nSalaPesquisa = 0;
                    if (!semSala && !salaStr.isEmpty()) {
                        try {
                            nSalaPesquisa = Integer.parseInt(salaStr);
                        } catch (Exception e) {}
                    }

                    for (Acao acao : todasAcoes) {
                        boolean match = true;

                        // Filtro Autor
                        if (!autor.isEmpty() && !acao.getLogin().toLowerCase().contains(autor.toLowerCase())) {
                            match = false;
                        }

                        // Filtro Tipo
                        if (tipoSigla != null && !acao.getTipoAcao().contains(tipoSigla)) {
                            match = false;
                        }

                        // Filtro Sala
                        if (semSala) {
                            // Se marcou "Geral", queremos ações onde sala é -1 ou 0
                            if (acao.getnSala() != -1 && acao.getnSala() != 0) match = false;
                        } else if (!salaStr.isEmpty()) {
                            if (acao.getnSala() != nSalaPesquisa) match = false;
                        }

                        // Filtro Data
                        if (dataFiltro != null) {
                            Calendar acaoData = acao.getDataAcao();
                            // Compara apenas Dia, Mês e Ano
                            if (acaoData.get(Calendar.YEAR) != dataFiltro.get(Calendar.YEAR) ||
                                    acaoData.get(Calendar.MONTH) != dataFiltro.get(Calendar.MONTH) ||
                                    acaoData.get(Calendar.DAY_OF_MONTH) != dataFiltro.get(Calendar.DAY_OF_MONTH)) {
                                match = false;
                            }
                        }

                        if (match) {
                            acoesFiltradas.add(acao);
                        }
                    }

                    // Ordena
                    Collections.sort(acoesFiltradas, (o1, o2) -> Integer.compare(o2.getIdAcao(), o1.getIdAcao()));

                    // Atualiza UI
                    List<Acao> finalFiltradas = acoesFiltradas;
                    runOnUiThread(() -> {
                        if (finalFiltradas.isEmpty()) {
                            Toast.makeText(AcoesActivity.this, "Nenhuma ação encontrada.", Toast.LENGTH_LONG).show();
                            // Opcional: Voltar ao modo live se nada encontrado
                            // isFiltering = false;
                        } else {
                            acoesAdapter.updateAcoes(finalFiltradas);
                            Toast.makeText(AcoesActivity.this, finalFiltradas.size() + " registros encontrados.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } catch (Exception e) {
                Log.e("Pesquisa", "Erro: " + e.getMessage());
                runOnUiThread(() -> Toast.makeText(AcoesActivity.this, "Erro ao pesquisar", Toast.LENGTH_SHORT).show());
                isFiltering = false;
            }
        }).start();
    }

    // Método portado do Swing para extrair o código dentro dos parenteses
    public String procurarTipoAcao(String texto) {
        if (texto.equals("Todos")) return null;

        String regex = "\\((.*?)\\)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(texto);

        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return null;
        }
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bnv_botton);
        bottomNavigationView.setSelectedItemId(R.id.tab_acoes);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.tab_salas) {
                stopUpdateThread();
                Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
                return true;
            } else if (itemId == R.id.tab_acoes) {
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

    // --- Runnable de Atualização Automática ---
    private class AtualizaDadosAcao implements Runnable {
        @Override
        public void run() {
            while (runningUpdate) {
                // SÓ ATUALIZA SE NÃO ESTIVER FILTRANDO
                if (!isFiltering) {
                    try {
                        String resp = "";
                        if (MainApp.sessaoInstance != null) {
                            resp = MainApp.sessaoInstance.logs();
                        }

                        if (resp != null && !resp.isEmpty() && resp.contains("dataAcao")) {
                            List<Acao> listaAcoes = gs.fromJson(resp, tipoAcao);
                            if (listaAcoes != null) {
                                Collections.sort(listaAcoes, (o1, o2) -> Integer.compare(o2.getIdAcao(), o1.getIdAcao()));
                                final List<Acao> finalAcoes = listaAcoes;
                                runOnUiThread(() -> {
                                    if(!isFiltering) acoesAdapter.updateAcoes(finalAcoes);
                                });
                            }
                        }
                    } catch (Exception e) {
                        Log.e("AcoesActivity", "Erro: " + e.getMessage());
                    }
                }

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }
    }
}
