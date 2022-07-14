package br.kelvynn.mytodobasic.mercado;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.kelvynn.mytodobasic.R;
import br.kelvynn.mytodobasic.dados.HelperFirebase;
import br.kelvynn.mytodobasic.dados.mercado.ItemMercado;


@SuppressWarnings("ALL")
public class AdapterMercado extends RecyclerView.Adapter<AdapterMercado.ViewHolder> {
    private List<ItemMercado> itemMercados;
    private LayoutInflater layoutInflater;
    private HelperFirebase helperFirebase;


    public AdapterMercado(Context context, List<ItemMercado> itemMercados) {
        this.layoutInflater = LayoutInflater.from(context);
        this.itemMercados = itemMercados;
        this.helperFirebase = new HelperFirebase();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = layoutInflater.inflate(R.layout.card_mercado, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemMercado itemMercado = this.itemMercados.get(position);
        String nome = itemMercado.getNome();
        holder.itemMercado = itemMercado;
        holder.nome.setText(nome);
        holder.quantidadeText.setText(String.valueOf(itemMercado.getQuantidade()));
        holder.campoQuantidade.setText(String.valueOf(itemMercado.getQuantidade()));
        holder.categoria.setText(itemMercado.getNomeCategoria());
        if (itemMercado.isComprado()) {
            holder.radio.setChecked(true);
        }
        if (itemMercado.isComprado()) {
            holder.mudancaContext(holder.itemView.getResources().getColor(R.color.cinza, null), true);
        }
    }

    @Override
    public int getItemCount() {
        return itemMercados.size();
    }


    @SuppressWarnings({"CanBeFinal", "ConstantConditions"})
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nome, quantidadeText, categoria, separador;
        CheckBox radio;
        View view;
        ItemMercado itemMercado;
        RelativeLayout layoutEditor, layoutAtivar;
        Button buttonSalvar, buttonExcluir;
        ImageButton menos, mais;
        EditText campoQuantidade;
        CardView cardView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nome = itemView.findViewById(R.id.textNome);
            quantidadeText = itemView.findViewById(R.id.textQuantidade);
            campoQuantidade = itemView.findViewById(R.id.editQuantidade);
            radio = itemView.findViewById(R.id.radioButton);
            layoutEditor = itemView.findViewById(R.id.relativeEditorItem);
            layoutAtivar = itemView.findViewById(R.id.layout1);
            menos = itemView.findViewById(R.id.menos);
            mais = itemView.findViewById(R.id.mais);
            cardView = itemView.findViewById(R.id.ativar);
            buttonSalvar = itemView.findViewById(R.id.buttonSalvar);
            buttonExcluir = itemView.findViewById(R.id.buttonExcluir);
            categoria = itemView.findViewById(R.id.textCategoria);
            view = itemView.findViewById(R.id.linha1);
            separador = itemView.findViewById(R.id.textDivisa);


            radio.setOnCheckedChangeListener((compoundButton, b) -> {
                if (b != itemMercado.isComprado()) {
                    itemMercado.setComprado(b);
                    helperFirebase.updateCompradoItemMercado(itemMercado);
                    if (b) {
                        mudancaContext(itemView.getResources().getColor(R.color.cinza, null), b);
                    } else {
                        mudancaContext(itemView.getResources().getColor(R.color.black, null), b);
                    }


                }
            });

            layoutAtivar.setOnLongClickListener(view -> {
                Log.d("teste", "ativou layoutAtivar");
                editarComponentes();
                return false;
            });

            cardView.setOnLongClickListener(view -> {
                Log.d("teste", "ativou cardView");
                editarComponentes();
                return false;
            });


            nome.setOnLongClickListener(view -> {
                Log.d("teste", "ativou nome");
                editarComponentes();
                return false;
            });

            categoria.setOnLongClickListener(view -> {
                Log.d("teste", "ativou categoria");
                editarComponentes();
                return false;
            });

            quantidadeText.setOnLongClickListener(view -> {
                Log.d("teste", "ativou quantidadeText");
                editarComponentes();
                return false;
            });

            separador.setOnLongClickListener(view -> {
                Log.d("teste", "ativou separador");
                editarComponentes();
                return false;
            });

            mais.setOnClickListener(view -> {
                int quantidad = Integer.parseInt(campoQuantidade.getText().toString());
                if (quantidad < 99) {
                    quantidad++;
                }
                campoQuantidade.setText(String.valueOf(quantidad));
            });


            menos.setOnClickListener(view -> {
                int quantidad = Integer.parseInt(campoQuantidade.getText().toString());
                if (quantidad > 1) {
                    quantidad--;
                }
                campoQuantidade.setText(String.valueOf(quantidad));
            });


            buttonExcluir.setOnClickListener(view -> {
                layoutEditor.setVisibility(View.GONE);
                helperFirebase.delateItemMercado(itemMercado);
                notifyItemRemoved(itemMercados.indexOf(itemMercado));
                itemMercados.remove(itemMercado);
            });

            buttonSalvar.setOnClickListener(view -> {
                layoutEditor.setVisibility(View.GONE);
                quantidadeText.setText(campoQuantidade.getText().toString());
                int quantidade = Integer.parseInt(campoQuantidade.getText().toString());
                itemMercado.setQuantidade(quantidade);
                helperFirebase.updateItemMercado(itemMercado);

            });
        }

        private void editarComponentes() {
            if (layoutEditor.getVisibility() == View.GONE) {
                layoutEditor.setVisibility(View.VISIBLE);

            } else {
                layoutEditor.setVisibility(View.GONE);
            }
        }

        private void mudancaContext(int color, boolean b) {
            nome.setTextColor(color);
            quantidadeText.setTextColor(color);
            categoria.setTextColor(color);
            if (b) {
                view.setBackgroundResource(R.color.cinza);
            } else {
                view.setBackgroundResource(R.color.black);
            }
            separador.setTextColor(color);
        }


    }


}

