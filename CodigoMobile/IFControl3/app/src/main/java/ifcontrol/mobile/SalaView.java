package ifcontrol.mobile;

import android.content.Context;
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
import androidx.core.content.ContextCompat;

public class SalaView extends ConstraintLayout {

    private int nsala;
    private OnSalaEntrarListener listener;

    private TextView textViewNSala, textViewArCond, textViewDS, textViewLuzes;
    private View viewEstadoSala;
    private ImageView imageViewMovimento;
    private Button buttonEntrar;

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
                if (listener != null) {
                    listener.onEntrarClicked(nsala);
                }
            }
        });
    }

    public void setup(String textNSala,boolean estadoAr,boolean estadoDS, boolean estadoLuzes, boolean estadoSala, boolean presenca, int nsala) {
        this.nsala = nsala;
        textViewNSala.setText(textNSala);
        atualizar(estadoSala, estadoDS, estadoLuzes, estadoAr, presenca);
    }

    public void atualizar(boolean estadoSala, boolean estadoDS, boolean estadoLuzes, boolean estadoAr,boolean presenca) {

        int corOn= ContextCompat.getColor(getContext(),R.color.text_on);
        int corOff= ContextCompat.getColor(getContext(),R.color.text_off);

        if(estadoAr){
            textViewArCond.setText("ON");
            textViewArCond.setTextColor(corOn);
        }
        else{
            textViewArCond.setText("OFF");
            textViewArCond.setTextColor(corOff);
        }

        if(estadoDS){
            textViewDS.setText("ON");
            textViewDS.setTextColor(corOn);
        }
        else{
            textViewDS.setText("OFF");
            textViewDS.setTextColor(corOff);
        }

        if(estadoLuzes){
            textViewLuzes.setText("ON");
            textViewLuzes.setTextColor(corOn);
        }
        else{
            textViewLuzes.setText("OFF");
            textViewLuzes.setTextColor(corOff);
        }

        if(estadoSala){
            viewEstadoSala.setBackgroundResource(R.drawable.circle_red);
            buttonEntrar.setEnabled(false);
            buttonEntrar.setText("Fechada :(");
        }
        else{
            viewEstadoSala.setBackgroundResource(R.drawable.circle_green);
            buttonEntrar.setEnabled(true);
            buttonEntrar.setText("Entrar :)");
        }
        if(presenca){
            imageViewMovimento.setImageResource(R.drawable.movimento);
        }
        else{
            imageViewMovimento.setImageResource(R.drawable.repouso);
        }

        }

        public int getNsala(){
            return nsala;
        }
    }