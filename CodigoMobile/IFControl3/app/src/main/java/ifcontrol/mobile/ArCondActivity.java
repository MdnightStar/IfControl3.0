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
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ArCondActivity  extends AppCompatActivity {
    TextView labelTemperaturaAr;
    Spinner spinnerTemperaturaAr;
    TextView labelModoAr;
    Spinner spinnerModoAr;
    Button buttonEnviar;
    Button buttonVoltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arcond);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        spinnerTemperaturaAr = findViewById(R.id.spinnerTemperaturaAr);
        spinnerModoAr = findViewById(R.id.spinnerModoAr);

        // Adapter para o Spinner de Temperatura
        ArrayAdapter<CharSequence> adapterTemp = ArrayAdapter.createFromResource(this,
                R.array.temperaturas_array, android.R.layout.simple_spinner_item);
        adapterTemp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTemperaturaAr.setAdapter(adapterTemp);

        // Adapter para o Spinner de Modo
        ArrayAdapter<CharSequence> adapterModo = ArrayAdapter.createFromResource(this,
                R.array.modos_ar_array, android.R.layout.simple_spinner_item);
        adapterModo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerModoAr.setAdapter(adapterModo);

        buttonVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ArCondActivity.this, MenuActivity.class);
                startActivity(intent);
            }
        });
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
}
