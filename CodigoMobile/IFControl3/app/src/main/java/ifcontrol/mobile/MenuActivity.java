package ifcontrol.mobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.BarringInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import java.util.List;

public class MenuActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar_menu,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.config){
            Toast.makeText(this,"Configurações",Toast.LENGTH_SHORT).show();
        }
        if(id==R.id.sobre){
            Toast.makeText(this,"Sobre o app",Toast.LENGTH_SHORT).show();
        }
        return true;
    }
    public void onEntrarClicked(int nsala) {
        // Crie a intenção para ir para a SalaActivity
        Intent intent = new Intent(MenuActivity.this, SalaActivity.class);

        // Passe o número da sala (nsala) para a próxima tela, para que ela saiba qual sala mostrar
        intent.putExtra("NumeroSala", nsala);

        // Inicie a nova atividade
        startActivity(intent);
    }
}
