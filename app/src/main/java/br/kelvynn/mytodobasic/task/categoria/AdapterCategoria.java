package br.kelvynn.mytodobasic.task.categoria;

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
import br.kelvynn.mytodobasic.dados.task.Categoria;


@SuppressWarnings({"ALL", "CanBeFinal"})
public class AdapterCategoria extends RecyclerView.Adapter<AdapterCategoria.ViewHolder> {
    private List<Categoria> categorias;
    private LayoutInflater layoutInflater;
    private Context context;

    public AdapterCategoria(Context context, List<Categoria> categorias) {

        this.layoutInflater = LayoutInflater.from(context);
        this.categorias = categorias;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterCategoria.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = layoutInflater.inflate(R.layout.card_categoria, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterCategoria.ViewHolder holder, int position) {
        Categoria categoria = this.categorias.get(position);
        String nome = this.categorias.get(position).getNome();
        holder.categoria = categoria;
        holder.nome.setText(nome);

    }

    @Override
    public int getItemCount() {
        return categorias.size();
    }

    @SuppressWarnings("CanBeFinal")
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nome;
        Categoria categoria;
        RelativeLayout relativeLayout;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nome = itemView.findViewById(R.id.textViewNomeCategoria);
            relativeLayout = itemView.findViewById(R.id.layoutCategoria);
            relativeLayout.setOnClickListener(view -> {
                Intent it = new Intent(context, EditCategoriaActivity.class);
                it.putExtra("id", categoria.getId());
                it.putExtra("nome", categoria.getNome());
                view.getContext().startActivity(it);

            });

        }
    }


}
