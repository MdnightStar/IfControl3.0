package ifcontrol.mobile;

import android.app.Application; // Importante: deve herdar de Application
import android.util.Log;
import Controle.Sessao;

public class MainApp extends Application {

    // VARIÁVEIS ESTÁTICAS para acesso global
    private static Sessao sessaoInstance;
    private static boolean isSessaoAberta = false;

    // O Android chama este método UMA VEZ no ciclo de vida da aplicação.
    @Override
    public void onCreate() {
        super.onCreate();

        sessaoInstance = new Sessao();

        // Inicializa a Sessão
        if (sessaoInstance.iniciarSessao()) {
            Log.i("MainApp", "Sessão iniciada com sucesso.");
            isSessaoAberta = true;
        } else {
            Log.e("MainApp", "Falha ao iniciar a sessão.");
            isSessaoAberta = false;
        }
    }

    // Método estático para obter o objeto Sessao (usado no login)
    public static Sessao getSessao() {
        return sessaoInstance;
    }

    // Método estático para obter o status da sessão (usado no MainActivity)
    public static boolean isSessaoAberta() {
        return isSessaoAberta;
    }
}