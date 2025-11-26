package ifcontrol.mobile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import Modelo.Acao; // Certifique-se que o modelo Acao está neste pacote

public class AcoesAdapter extends RecyclerView.Adapter<AcoesAdapter.AcaoViewHolder> {

    private List<Acao> acoesList = new ArrayList<>();

    @NonNull
    @Override
    public AcaoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Infla o layout do item que criamos (item_acao.xml)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_acao, parent, false);
        return new AcaoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AcaoViewHolder holder, int position) {
        // Pega a ação da posição atual e manda o ViewHolder preencher a view
        Acao acao = acoesList.get(position);
        holder.bind(acao);
    }

    @Override
    public int getItemCount() {
        return acoesList.size();
    }

    // Método para a Activity poder atualizar a lista de ações no adapter
    public void updateAcoes(List<Acao> novasAcoes) {
        this.acoesList.clear();
        if (novasAcoes != null) {
            this.acoesList.addAll(novasAcoes);
        }
        notifyDataSetChanged(); // Avisa o RecyclerView que os dados mudaram e a tela precisa ser redesenhada
    }

    /**
     * ViewHolder que segura as referências para as Views de cada item da lista.
     */
    static class AcaoViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewDescricao;
        private final TextView textViewUsuario;
        private final TextView textViewData;
        private final TextView textViewHora;

        public AcaoViewHolder(@NonNull View itemView) {
            super(itemView);
            // Encontra as Views dentro do item_acao.xml
            textViewDescricao = itemView.findViewById(R.id.textViewAcaoDescricao);
            textViewUsuario = itemView.findViewById(R.id.textViewAcaoUsuario);
            textViewData = itemView.findViewById(R.id.textViewAcaoData);
            textViewHora = itemView.findViewById(R.id.textViewAcaoHora);
        }

        // Método que preenche as Views com os dados do objeto Acao
        public void bind(Acao acao) {
            String descricao = acao.getTipoAcao();
            if (acao.getnSala() != -1) {
                descricao += " na Sala " + acao.getnSala();
            }
            
            textViewDescricao.setText(descricao);
            textViewUsuario.setText("Login: " + acao.getLogin());
            textViewData.setText(acao.dataFormatada()); // Supondo que esses métodos existam na sua classe Acao
            textViewHora.setText(acao.horaFormatada());
        }
    }
}
