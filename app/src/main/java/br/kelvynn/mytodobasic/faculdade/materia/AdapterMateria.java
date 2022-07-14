package br.kelvynn.mytodobasic.faculdade.materia;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.kelvynn.mytodobasic.R;
import br.kelvynn.mytodobasic.dados.faculdade.Materia;


public class AdapterMateria extends RecyclerView.Adapter<AdapterMateria.ViewHolder> {
    @SuppressWarnings({"FieldMayBeFinal", "CanBeFinal"})
    private List<Materia> materias;
    @SuppressWarnings({"FieldMayBeFinal", "CanBeFinal"})
    private LayoutInflater layoutInflater;
    @SuppressWarnings({"FieldMayBeFinal", "CanBeFinal"})
    private Context context;

    public AdapterMateria(Context context, List<Materia> materias) {

        this.layoutInflater = LayoutInflater.from(context);
        this.materias = materias;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterMateria.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = layoutInflater.inflate(R.layout.card_categoria, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterMateria.ViewHolder holder, int position) {
        Materia materia = this.materias.get(position);
        String nome = this.materias.get(position).getNome();
        holder.materia = materia;
        holder.nome.setText(nome);

    }

    @Override
    public int getItemCount() {

        return materias.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @SuppressWarnings("CanBeFinal")
        TextView nome;
        Materia materia;
        @SuppressWarnings("CanBeFinal")
        RelativeLayout relativeLayout;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nome = itemView.findViewById(R.id.textViewNomeCategoria);
            relativeLayout = itemView.findViewById(R.id.layoutCategoria);
            relativeLayout.setOnClickListener(view -> {

                Intent it = new Intent(context, EditMateriaActivity.class);
                it.putExtra("id", materia.getId());
                it.putExtra("nome", materia.getNome());
                view.getContext().startActivity(it);

            });

        }
    }


}
