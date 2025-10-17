package ifcontrol.mobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.app.AlertDialog;

import Controle.Sessao;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AlertDialog.Builder msg = new AlertDialog.Builder(this);
        msg.setTitle("Erro");

        // *** CORREÇÃO: Acesso estático ao status da sessão global ***
        boolean sessaoAberto = MainApp.isSessaoAberta();

        if (sessaoAberto) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(MainActivity.this, AppActivity.class);
                    startActivity(i);
                    finish();
                }
            }, 2000);
        } else {
            msg.setMessage("Erro ao iniciar sessão");
            msg.show();
        }
    }
}