package ifcontrol.mobile;

import android.content.Context;
import android.content.Intent; // Import necessário
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

public class SalaView extends ConstraintLayout {

    private int nsala;
    // Mantendo a lógica estática do Swing para pausar atualizações quando uma sala abre
    public static boolean salaAberta = false;
    private OnSalaEntrarListener listener;

    private TextView textViewNSala, textViewArCond, textViewDS, textViewLuzes;
    private View viewEstadoSala;
    private ImageView imageViewMovimento;
    private Button buttonEntrar;

    // Cores baseadas no Swing (SalaPanel)
    private final int COLOR_ON = Color.parseColor("#009933");
    private final int COLOR_OFF = Color.RED;

    // Interface mantida caso precise usar externamente, mas a ação principal será feita aqui dentro agora
    public interface OnSalaEntrarListener {
        void onEntrarClicked(int nsala);
    }

    public void setOnSalaEntrarListener(OnSalaEntrarListener listener) {
        this.listener = listener;
    }

    public SalaView(@NonNull Context context) {
        super(context);
        initView();
    }

    public SalaView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public SalaView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.item_sala, this, true);

        textViewNSala = findViewById(R.id.textViewNSala);
        textViewArCond = findViewById(R.id.textViewArCond);
        textViewDS = findViewById(R.id.textViewDS);
        textViewLuzes = findViewById(R.id.textViewLuzes);
        viewEstadoSala = findViewById(R.id.viewEstadoSala);
        imageViewMovimento = findViewById(R.id.imageViewMovimento);
        buttonEntrar = findViewById(R.id.buttonEntrar);

        buttonEntrar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 1. Define que uma sala foi aberta (pausa a thread no MenuActivity)
                salaAberta = true;

                // 2. IMPLEMENTAÇÃO DA NAVEGAÇÃO DIRETA AQUI
                // Usamos getContext() pois estamos dentro de uma View, não de uma Activity
                Intent intent = new Intent(getContext(), SalaActivity.class);

                // Passa o número da sala
                intent.putExtra("NUMERO_SALA", nsala);
                // Fallback caso sua Activity use a chave antiga
                intent.putExtra("nSala", nsala);

                // Inicia a nova tela
                getContext().startActivity(intent);

                // 3. Mantemos o listener caso o Adapter precise saber que houve um clique (opcional)
                if (listener != null) {
                    listener.onEntrarClicked(nsala);
                }
            }
        });
    }

    public void setup(String textNSala, boolean estadoAr, boolean estadoDS, boolean estadoLuzes, boolean estadoSala, boolean presenca, int nsala) {
        this.nsala = nsala;
        textViewNSala.setText(textNSala);
        atualizar(estadoSala, estadoDS, estadoLuzes, estadoAr, presenca);
    }

    /**
     * Lógica copiada e adaptada de SalaPanel.java
     */
    public void atualizar(boolean estadoSala, boolean estadoDS, boolean estadoLuzes, boolean estadoAr, boolean presenca) {

        // Lógica do Ar Condicionado
        if (estadoAr) {
            textViewArCond.setText("ON");
            textViewArCond.setTextColor(COLOR_ON);
        } else {
            textViewArCond.setText("OFF");
            textViewArCond.setTextColor(COLOR_OFF);
        }

        // Lógica do DataShow
        if (estadoDS) {
            textViewDS.setText("ON");
            textViewDS.setTextColor(COLOR_ON);
        } else {
            textViewDS.setText("OFF");
            textViewDS.setTextColor(COLOR_OFF);
        }

        // Lógica das Luzes
        if (estadoLuzes) {
            textViewLuzes.setText("ON");
            textViewLuzes.setTextColor(COLOR_ON);
        } else {
            textViewLuzes.setText("OFF");
            textViewLuzes.setTextColor(COLOR_OFF);
        }

        // Lógica do Estado da Sala (Disponibilidade)
        if (estadoSala) {
            // Sala Ocupada/Fechada
            viewEstadoSala.setBackgroundColor(Color.RED);
            buttonEntrar.setEnabled(false);
        } else {
            // Sala Livre
            viewEstadoSala.setBackgroundColor(Color.GREEN);
            buttonEntrar.setEnabled(true);
        }

        // Lógica de Presença
        if (presenca) {
            imageViewMovimento.setImageResource(R.drawable.movimento);
        } else {
            imageViewMovimento.setImageResource(R.drawable.repouso);
        }
    }

    public int getNsala() {
        return nsala;
    }
}

