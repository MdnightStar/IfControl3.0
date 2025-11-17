package ifcontrol.mobile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

import Modelo.Sala;

// Passo 1: Implementar a interface do Adapter para receber os cliques
public class MenuActivity extends AppCompatActivity implements SalasAdapter.OnSalaClickListener {

    // Passo 2: Declarar as variáveis para o RecyclerView, o Adapter e a lista de dados
    private RecyclerView recyclerViewSalas;
    private SalasAdapter salasAdapter;
    private List<Sala> listaDeSalas = new ArrayList<>();

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

        // Passo 3: Encontrar o RecyclerView no layout
        recyclerViewSalas = findViewById(R.id.recyclerViewSalas);

        // Passo 4: Criar os dados que serão exibidos (aqui usamos dados de exemplo)
        prepararListaDeSalas();

        // Passo 5: Criar o Adapter, passando a lista de dados e a Activity como "ouvinte" do clique
        salasAdapter = new SalasAdapter(listaDeSalas, this);

        // Passo 6: Definir o LayoutManager (Grid de 2 colunas) e finalmente conectar o Adapter
        recyclerViewSalas.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerViewSalas.setAdapter(salasAdapter);
    }

    /**
     * Método para popular a lista de salas com dados de exemplo.
     */
    private void prepararListaDeSalas() {
        // Sala 1
        Sala sala1 = new Sala(101, "192.168.1.1");
        sala1.setEstadoAr(true);
        sala1.setEstadoDataShow(false);
        sala1.setEstadoLuzes(true);
        sala1.setEstadoSala(false); // false = aberta
        sala1.setPresenca(true);

        // Sala 2
        Sala sala2 = new Sala(102, "192.168.1.2");
        sala2.setEstadoAr(false);
        sala2.setEstadoDataShow(false);
        sala2.setEstadoLuzes(false);
        sala2.setEstadoSala(true); // true = fechada
        sala2.setPresenca(false);
        
        // Sala 3
        Sala sala3 = new Sala(201, "192.168.1.3");
        sala3.setEstadoAr(true);
        sala3.setEstadoDataShow(true);
        sala3.setEstadoLuzes(true);
        sala3.setEstadoSala(false);
        sala3.setPresenca(true);
        
        // Sala 4
        Sala sala4 = new Sala(202, "192.168.1.4");
        sala4.setEstadoAr(false);
        sala4.setEstadoDataShow(true);
        sala4.setEstadoLuzes(false);
        sala4.setEstadoSala(false);
        sala4.setPresenca(false);

        listaDeSalas.add(sala1);
        listaDeSalas.add(sala2);
        listaDeSalas.add(sala3);
        listaDeSalas.add(sala4);
    }

    // Passo 7: Este método será chamado pelo Adapter quando um item da lista for clicado!
    @Override
    public void onSalaClick(Sala sala) {
        Intent intent = new Intent(MenuActivity.this, SalaActivity.class);
        // Passando o número da sala para a próxima Activity, para que ela saiba qual sala carregar
        intent.putExtra("NUMERO_SALA", sala.getnSala());
        startActivity(intent);
    }

    // Métodos do Menu da Toolbar
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
