package ifcontrol.mobile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.widget.EditText;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;

import Controle.Sessao;

public class AppActivity extends Activity {
    private EditText editTextLogin;
    private EditText editTextPassword;
    private Button buttonLogin;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);

        // Inicializa os componentes
        editTextLogin = findViewById(R.id.editTextLogin);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin); // Verifique se o ID está correto

        // Define o Listener do botão para executar a validação
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                realizarLogin();
            }
        });

        // As linhas abaixo eram redundantes no onCreate e foram movidas para realizarLogin()
        // String textLogin = editTextLogin.getText().toString();
        // String textPassword = editTextPassword.getText().toString();
    }

    // Método para exibir o AlertDialog
    private void mostrarAlerta(String titulo, String mensagem) {
        new AlertDialog.Builder(this)
                .setTitle(titulo)
                .setMessage(mensagem)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss(); // Fecha a caixa de diálogo
                    }
                })
                .show();
    }

    private void realizarLogin() {
        // Obtém o texto dos campos e remove espaços extras no início/fim
        String login = editTextLogin.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        // 3. Implementação da Lógica de Validação
        if (login.isEmpty()) {
            mostrarAlerta("ERRO", "Insira o Login!");
            editTextLogin.requestFocus(); // Coloca o foco no campo de login

        } else if (password.isEmpty()) {
            mostrarAlerta("ERRO", "Insira a Senha!");
            editTextPassword.requestFocus(); // Coloca o foco no campo de senha

        } else {
            // Todos os campos estão preenchidos. Prossegue com a lógica de login.
            Log.i("Login", "Login e senha preenchidos. Tentando autenticação...");

            // *** CORREÇÃO: Chamada de login adaptada para Android ***
            // Acessa o objeto Sessao inicializado na MainApp
            String retorno = MainApp.sessaoInstance.login(login, password);

            // Processamento do 'retorno' da sua função de login
            if ("LOGIN_OK".equalsIgnoreCase(retorno)) { // Exemplo
                mostrarAlerta("SUCESSO", "Login realizado com sucesso!");
                // Adicione aqui a navegação para a próxima Activity
                //navegação para a pagina menuActivity

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(AppActivity.this, MenuActivity.class);
                        startActivity(i);
                        finish();
                    }
                }, 2000);
            } else {
                mostrarAlerta("FALHA", "Credenciais inválidas ou erro no servidor.");
            }
        }
    }
}