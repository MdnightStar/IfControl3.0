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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class SalaActivity extends AppCompatActivity {
    TextView labelTemp;
    TextView textViewSalaTemp;
    TextView labelUmi;
    TextView textViewSalaUmi;
    TextView labelPres;
    TextView textViewSalaPres;
    ImageView buttonArCond;
    ImageView buttonLuz;
    ImageView buttonDS;
    Button buttonAr;
    Button buttonDataShow;
    boolean isArCond = true;

    private int nSala;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sala);

        nSala = getIntent().getIntExtra("nSala",0);
        if(nSala!=0){
            Toolbar toolbar = findViewById(R.id.toolbar);
            toolbar.setTitle("Sala"+nSala);
            setSupportActionBar(toolbar);
        }
        buttonAr = findViewById(R.id.buttonAr);
        buttonDataShow = findViewById(R.id.buttonDataShow);

        buttonAr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SalaActivity.this, ArCondActivity.class);
                startActivity(intent);
            }
        });

        buttonDataShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SalaActivity.this, DataShowActivity.class);
                startActivity(intent);
            }
        });

        buttonArCond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isArCond) {
                    buttonArCond.setImageResource(R.drawable.arcon_off);
                    isArCond = false;
                } else {
                    buttonArCond.setImageResource(R.drawable.arcon_on);
                    isArCond = true;
                }
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
