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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import Modelo.Sala;

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
    Button buttonVoltarSala;
    boolean isArCond = true;
    private Sala sala;
    private int nSala;
    private boolean estadoAr, estadoDS, estadoLuzes;
    private Gson gs;
    private java.lang.reflect.Type tipoSala;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sala);

        MainApp.sessaoInstance.trataAcao("OCP"+""+nSala);
        gs = new Gson();
        this.tipoSala= new TypeToken<Sala>(){}.getType();
        String resposta = MainApp.sessaoInstance.getSala(nSala);
        sala = gs.fromJson(resposta, tipoSala);
        estadoAr = sala.isEstadoAr();
        estadoDS = sala.isEstadoDataShow();
        estadoLuzes = sala.isEstadoLuzes();

        if(!estadoAr){
            buttonArCond.setImageResource(R.drawable.arcon_off);
        }else{
            buttonArCond.setImageResource(R.drawable.arcon_on);
        }
        if(!estadoDS){
            buttonDS.setImageResource(R.drawable.datashow_off);
        }else{
            buttonDS.setImageResource(R.drawable.datashow_on);
        }
        if(!estadoLuzes){
            buttonLuz.setImageResource(R.drawable.luz_off);
        }else {
            buttonLuz.setImageResource(R.drawable.luz_on);
        }
        new Thread(new AtualizaDadosSala()).start();


        nSala = getIntent().getIntExtra("nSala",0);
        if(nSala!=0){
            Toolbar toolbar = findViewById(R.id.toolbar);
            toolbar.setTitle("Sala"+nSala);
            setSupportActionBar(toolbar);
        }
        buttonAr = findViewById(R.id.buttonAr);
        buttonDataShow = findViewById(R.id.buttonDataShow);
        buttonVoltarSala = findViewById(R.id.buttonVoltarSala);

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

        buttonVoltarSala.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SalaActivity.this, MenuActivity.class);
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

    private class AtualizaDadosSala implements Runnable {

        @Override
        public void run() {
            while (isDisplayable()) {
                String resposta = MainApp.sessaoInstance.getSala(nSala);
                if (resposta.contains("nSala")) {
                    sala = gs.fromJson(resposta, tipoSala);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textViewSalaTemp.setText(sala.getTemperatura() + "ºC");
                            textViewSalaUmi.setText(sala.getUmidade() + "%");
                            if (sala.isPresenca()) {
                                textViewSalaPres.setText("Com movimento");
                            } else {
                                textViewSalaPres.setText("Sem movimento");
                            }
                            estadoAr = sala.isEstadoAr();
                            estadoDS = sala.isEstadoDataShow();
                            estadoLuzes = sala.isEstadoLuzes();
                            /*if (!estadoAr) {
                                jLabelAr.setIcon(new ImageIcon(getClass().getResource("/Imagens/ar-condicionado-off (1).png")));
                            } else {

                                jLabelAr.setIcon(new ImageIcon(getClass().getResource("/Imagens/ar-condicionado-on (1).png")));
                            }
                            if (!estadoLuzes) {
                                jLabelLuz.setIcon(new ImageIcon(getClass().getResource("/Imagens/luz-off (1).png")));
                            } else {
                                jLabelLuz.setIcon(new ImageIcon(getClass().getResource("/Imagens/luz-on (1).png")));
                            }
                            if (!estadoDS) {

                                jLabelDS.setIcon(new ImageIcon(getClass().getResource("/Imagens/data-show-off (1).png")));
                            } else {

                                jLabelDS.setIcon(new ImageIcon(getClass().getResource("/Imagens/data-show-on (1).png")));
                            }*/
                        }
                    });
                }
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException ex) {
                    System.out.println("Erro ao atualizar");
                }

            }

            MainApp.sessaoInstance.trataAcao("DSC"+":"+nSala);

            SalaView.salaAberta = false;
        }
    }


}
