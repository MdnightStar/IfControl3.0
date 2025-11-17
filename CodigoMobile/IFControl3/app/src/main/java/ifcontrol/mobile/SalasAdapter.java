package ifcontrol.mobile;

import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import Modelo.Sala;

/**
 * Adapter para gerenciar e exibir uma lista de Salas em um RecyclerView.
 * Conecta a lista de dados (List<Sala>) com a interface (RecyclerView).
 */
public class SalasAdapter extends RecyclerView.Adapter<SalasAdapter.SalaViewHolder> {

    private final List<Sala> listaDeSalas;
    private final OnSalaClickListener listener;

    /**
     * Interface para comunicar o clique em uma sala para a Activity.
     * A Activity irá implementar esta interface para receber o evento.
     */
    public interface OnSalaClickListener {
        void onSalaClick(Sala sala);
    }

    /**
     * Construtor do Adapter.
     * @param listaDeSalas A lista de dados que será exibida.
     * @param listener A Activity (ou Fragment) que vai "ouvir" os cliques.
     */
    public SalasAdapter(List<Sala> listaDeSalas, OnSalaClickListener listener) {
        this.listaDeSalas = listaDeSalas;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SalaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Cria uma nova SalaView programaticamente.
        SalaView salaView = new SalaView(parent.getContext());
        return new SalaViewHolder(salaView);
    }

    @Override
    public void onBindViewHolder(@NonNull SalaViewHolder holder, int position) {
        Sala salaAtual = listaDeSalas.get(position);
        holder.bind(salaAtual, listener);
    }

    @Override
    public int getItemCount() {
        return listaDeSalas != null ? listaDeSalas.size() : 0;
    }

    /**
     * O ViewHolder atua como um cache para as referências de View de cada item.
     * Ele segura a SalaView, melhorando a performance ao evitar chamadas repetitivas.
     */
    static class SalaViewHolder extends RecyclerView.ViewHolder {
        private final SalaView salaView;

        public SalaViewHolder(@NonNull View itemView) {
            super(itemView);
            this.salaView = (SalaView) itemView;
        }

        /**
         * Configura a SalaView com os dados do objeto Sala e define o listener de clique.
         */
        public void bind(final Sala sala, final OnSalaClickListener listener) {
            // Chama o método setup da SalaView, mapeando os dados do objeto 'sala'
            // para os parâmetros na ordem correta.
            salaView.setup(
                "Sala " + sala.getnSala(), // textNSala (String)
                sala.isEstadoAr(),         // estadoAr (boolean)
                sala.isEstadoDataShow(),   // estadoDS (boolean)
                sala.isEstadoLuzes(),      // estadoLuzes (boolean)
                sala.isEstadoSala(),       // estadoSala (boolean)
                sala.isPresenca(),         // presenca (boolean)
                sala.getnSala()            // nsala (int)
            );

            // Configura o listener do botão "Entrar" que está dentro da SalaView.
            salaView.setOnSalaEntrarListener(new SalaView.OnSalaEntrarListener() {
                @Override
                public void onEntrarClicked(int nsala) {
                    // Quando o botão é clicado, o listener da SalaView avisa o Adapter,
                    // que por sua vez avisa a Activity, passando o objeto 'sala' completo.
                    if (listener != null) {
                        listener.onSalaClick(sala);
                    }
                }
            });
        }
    }
}
