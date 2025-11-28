package ifcontrol.mobile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Modelo.Acao;

public class AcoesAdapter extends RecyclerView.Adapter<AcoesAdapter.AcaoViewHolder> {

    private final List<Acao> acoesList = new ArrayList<>();

    @NonNull
    @Override
    public AcaoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_acao, parent, false);
        return new AcaoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AcaoViewHolder holder, int position) {
        Acao acao = acoesList.get(position);
        holder.bind(acao);
    }

    @Override
    public int getItemCount() {
        return acoesList.size();
    }

    public void updateAcoes(List<Acao> novasAcoes) {
        this.acoesList.clear();
        if (novasAcoes != null) {
            this.acoesList.addAll(novasAcoes);
        }
        notifyDataSetChanged();
    }

    static class AcaoViewHolder extends RecyclerView.ViewHolder {

        private final TextView textViewDescricao;
        private final TextView textViewSala; // Novo campo
        private final TextView textViewUsuario;
        private final TextView textViewData;
        private final TextView textViewHora;
        private final TextView textViewId;   // Novo campo
        private final ImageView imgIconAcao;

        public AcaoViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewDescricao = itemView.findViewById(R.id.textViewAcaoDescricao);
            textViewSala = itemView.findViewById(R.id.textViewAcaoSala); // FindById novo
            textViewUsuario = itemView.findViewById(R.id.textViewAcaoUsuario);
            textViewData = itemView.findViewById(R.id.textViewAcaoData);
            textViewHora = itemView.findViewById(R.id.textViewAcaoHora);
            textViewId = itemView.findViewById(R.id.textViewAcaoId); // FindById novo
            imgIconAcao = itemView.findViewById(R.id.imgIconAcao);
        }

        public void bind(Acao acao) {
            String rawTipo = acao.getTipoAcao();

            // 1. Traduz o texto
            String textoTraduzido = traduzirAcao(rawTipo);

            // 2. Define a Descrição (Apenas o que foi feito)
            textViewDescricao.setText(textoTraduzido);

            // 3. Define a Sala separadamente
            if (acao.getnSala() != -1) {
                textViewSala.setText("Sala: " + acao.getnSala());
                textViewSala.setVisibility(View.VISIBLE);
            } else {
                // Se for uma ação de sistema que não tem sala específica
                textViewSala.setText("Sistema / Geral");
                textViewSala.setVisibility(View.VISIBLE); // Ou GONE se preferir esconder
            }

            // 4. Define o ID no canto inferior direito
            textViewId.setText("#" + acao.getIdAcao());

            // 5. Define usuário e data
            textViewUsuario.setText("Login: " + acao.getLogin());
            try {
                textViewData.setText(acao.dataFormatada());
                textViewHora.setText(acao.horaFormatada());
            } catch (Exception e) {
                textViewData.setText("--/--");
                textViewHora.setText("--:--");
            }

            // 6. Define o ícone
            definirIcone(rawTipo);
        }

        private void definirIcone(String tipoAcao) {
            if (tipoAcao == null) return;

            if (tipoAcao.contains("AR")) {
                imgIconAcao.setImageResource(R.drawable.ar);
            }
            else if (tipoAcao.contains("DS")) {
                imgIconAcao.setImageResource(R.drawable.datashow_icon);
            }
            else if (tipoAcao.contains("LZ")) {
                imgIconAcao.setImageResource(R.drawable.lampada);
            }
            else if (tipoAcao.contains("add") || tipoAcao.contains("edit") || tipoAcao.contains("delet")) {
                imgIconAcao.setImageResource(R.drawable.acao_objeto);
            }
            else {
                imgIconAcao.setImageResource(R.drawable.icone_acao);
            }
        }

        // --- MÉTODOS DE TRADUÇÃO ---

        private String traduzirAcao(String tipoAcao) {
            if (tipoAcao == null) return "Ação desconhecida";

            if (tipoAcao.contains("ARON")) return "Ligou o Ar";
            if (tipoAcao.contains("AROFF")) return "Desligou o Ar";
            if (tipoAcao.contains("DSON")) return "Ligou o DataShow";
            if (tipoAcao.contains("DSOFF")) return "Desligou o DataShow";
            if (tipoAcao.contains("LZON")) return "Ligou a luz";
            if (tipoAcao.contains("LZOFF")) return "Desligou a luz";
            if (tipoAcao.contains("OCP")) return "Ocupou a sala";
            if (tipoAcao.equals("DSC")) return "Desocupou a sala";

            if (tipoAcao.contains("--addSala--")) return "Adicionou sala";
            if (tipoAcao.contains("--addAgendamento--")) return "Adicionou agendamento";
            if (tipoAcao.contains("--deletAgendamento--")) return "Apagou agendamento";
            if (tipoAcao.contains("--editAgendamento--")) return "Editou agendamento";
            if (tipoAcao.contains("--addDispositivo--")) return "Adicionou dispositivo";
            if (tipoAcao.contains("--editDispositivo--")) return "Editou dispositivo";
            if (tipoAcao.contains("--deletDispositivo--")) return "Apagou dispositivo";

            return decodificar(tipoAcao);
        }

        private String decodificar(String comando) {
            if (comando == null || comando.isEmpty()) return "Comando vazio";
            String cmd = comando.toUpperCase().trim();

            if (cmd.startsWith("AR")) return decodificarArCondicionado(cmd);
            else if (cmd.startsWith("DS")) return decodificarDataShow(cmd);
            else return cmd;
        }

        private String decodificarArCondicionado(String cmd) {
            cmd = cmd.replace(".", "");
            Map<String, String> modos = new HashMap<>();
            modos.put("COOL", "Cool");
            modos.put("FAN", "Fan");
            modos.put("AUTO", "Auto");
            modos.put("DRY", "Dry");

            for (Map.Entry<String, String> entry : modos.entrySet()) {
                if (cmd.contains(entry.getKey())) {
                    String modo = entry.getValue();
                    String tempStr = cmd.substring(cmd.indexOf(entry.getKey()) + entry.getKey().length());
                    try {
                        int temperatura = Integer.parseInt(tempStr);
                        return String.format("Ar: %s em %d°C", modo, temperatura);
                    } catch (NumberFormatException e) {
                        return "Ar: " + modo;
                    }
                }
            }
            return "Comando Ar não reconhecido";
        }

        private String decodificarDataShow(String cmd) {
            cmd = cmd.replace(".", "");
            String[] comandosValidos = {"OK", "ESQ", "CIMA", "BAIXO", "DIR", "ESC", "FREEZE", "MENU"};
            if(cmd.length() <= 2) return "Datashow";
            String comandoDS = cmd.substring(2);
            for (String valido : comandosValidos) {
                if (comandoDS.equals(valido)) return String.format("Datashow: %s", valido);
            }
            return "Datashow: " + comandoDS;
        }
    }
}
