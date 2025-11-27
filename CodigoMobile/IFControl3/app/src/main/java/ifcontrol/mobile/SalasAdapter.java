package ifcontrol.mobile;

import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import Modelo.Sala;

public class SalasAdapter extends RecyclerView.Adapter<SalasAdapter.SalaViewHolder> {

    // Inicializa a lista para evitar NullPointerException
    private List<Sala> listaDeSalas = new ArrayList<>();
    private final OnSalaClickListener listener;

    public interface OnSalaClickListener {
        void onSalaClick(Sala sala);
    }

    public SalasAdapter(List<Sala> listaDeSalas, OnSalaClickListener listener) {
        this.listaDeSalas = listaDeSalas;
        this.listener = listener;
    }

    /**
     * Método novo para atualizar os dados vindo da Thread (similar ao loop de atualização do Swing)
     */
    public void atualizarDados(List<Sala> novasSalas) {
        this.listaDeSalas = novasSalas;
        notifyDataSetChanged(); // Avisa a lista para se redesenhar
    }

    @NonNull
    @Override
    public SalaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SalaView salaView = new SalaView(parent.getContext());
        // Ajusta layout params para garantir que o card fique com tamanho correto no Grid
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(8, 8, 8, 8); // Margens opcionais
        salaView.setLayoutParams(lp);

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

    static class SalaViewHolder extends RecyclerView.ViewHolder {
        private final SalaView salaView;

        public SalaViewHolder(@NonNull SalaView itemView) {
            super(itemView);
            this.salaView = itemView;
        }

        public void bind(final Sala sala, final OnSalaClickListener listener) {
            salaView.setup(
                    "Sala " + sala.getnSala(),
                    sala.isEstadoAr(),
                    sala.isEstadoDataShow(),
                    sala.isEstadoLuzes(),
                    sala.isEstadoSala(),
                    sala.isPresenca(),
                    sala.getnSala()
            );

            salaView.setOnSalaEntrarListener(new SalaView.OnSalaEntrarListener() {
                @Override
                public void onEntrarClicked(int nsala) {
                    if (listener != null) {
                        listener.onSalaClick(sala);
                    }
                }
            });
        }
    }
}
