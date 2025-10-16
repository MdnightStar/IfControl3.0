package ifcontrol.mobile;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import Controle.Sessao;

public class AppActivity extends Activity {
    protected static Sessao sessao;
    protected static boolean running;
    protected boolean sessaoAberta = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);

        AlertDialog.Builder msg=new AlertDialog.Builder(this);

        sessao= new Sessao();
        if(sessao.iniciarSessao()==false){
            Log.e("Main","A conexão com o servidor falho");
            msg.setMessage("A conexão com o servidor falho");
            msg.show();
        }else{
            Log.i("Main","Sessao iniciada");
            running=true;
            msg.setMessage("Conectado no servidor");
            msg.show();
        }
    }


}