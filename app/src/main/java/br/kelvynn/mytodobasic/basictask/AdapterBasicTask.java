package br.kelvynn.mytodobasic.basictask;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

import br.kelvynn.mytodobasic.R;
import br.kelvynn.mytodobasic.dados.HelperFirebase;
import br.kelvynn.mytodobasic.dados.basicTask.BasicTask;


@SuppressWarnings("ALL")
public class AdapterBasicTask extends RecyclerView.Adapter<AdapterBasicTask.ViewHolder> {
    private List<BasicTask> basicTasks;
    private LayoutInflater layoutInflater;
    private HelperFirebase helperFirebase;
    private Context context;
    private int tipoLista; // 0 = somente não concluido, 1 = concluido, 2 todos;


    public AdapterBasicTask(Context context, List<BasicTask> basicTasks, int tipoLista) {
        this.layoutInflater = LayoutInflater.from(context);
        this.basicTasks = basicTasks;
        this.helperFirebase = new HelperFirebase();
        this.tipoLista = tipoLista;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = layoutInflater.inflate(R.layout.card_sub_task, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterBasicTask.ViewHolder holder, int position) {
        BasicTask basicTask = this.basicTasks.get(position);
        String nome = basicTask.getNome();
        holder.basicTask = basicTask;
        holder.nome.setText(nome);
        if (basicTask.getConcluido()) {
            holder.radio.setChecked(true);
        }
    }

    @Override
    public int getItemCount() {
        return basicTasks.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nome;
        CheckBox radio;
        BasicTask basicTask;
        RelativeLayout layoutItem, layoutEditItem, cardViewItem;
        Button buttonSalvar, buttonExcluir;
        ImageButton closeButton;

        TextInputLayout campoAddSubTask;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nome = itemView.findViewById(R.id.textNome);
            radio = itemView.findViewById(R.id.radioButton);
            layoutItem = itemView.findViewById(R.id.layoutItem);
            layoutEditItem = itemView.findViewById(R.id.layoutEditItem);
            cardViewItem = itemView.findViewById(R.id.cardViewItem);
            buttonSalvar = itemView.findViewById(R.id.buttonSalvar);
            buttonExcluir = itemView.findViewById(R.id.buttonExcluir);
            closeButton = itemView.findViewById(R.id.closeButton);
            campoAddSubTask = itemView.findViewById(R.id.campoAddSubTask);

            radio.setOnCheckedChangeListener((compoundButton, b) -> {
                if (b != basicTask.getConcluido()) {
                    basicTask.setConcluido(b);
                    helperFirebase.updateBasicTaskSetConcluido(basicTask);
                    if (tipoLista != 2) {
                        notifyItemRemoved(basicTasks.indexOf(basicTask));
                        basicTasks.remove(basicTask);
                    }
                }
            });

            cardViewItem.setOnLongClickListener(view -> {
                if (layoutItem.isVisibleToUserForAutofill(View.VISIBLE)) {
                    transacaoEditar(true);

                }
                return false;
            });

            nome.setOnLongClickListener(view -> {
                if (layoutItem.isVisibleToUserForAutofill(View.VISIBLE)) {
                    transacaoEditar(true);

                }
                return false;
            });


            closeButton.setOnClickListener(view -> transacaoEditar(false));

            buttonExcluir.setOnClickListener(view -> {
                transacaoEditar(false);
                helperFirebase.delateBasicTask(basicTask);
                notifyItemRemoved(basicTasks.indexOf(basicTask));
                basicTasks.remove(basicTask);
            });

            buttonSalvar.setOnClickListener(view -> {
                transacaoEditar(false);
                basicTask.setNome(campoAddSubTask.getEditText().getText().toString());
                nome.setText(basicTask.getNome());
                helperFirebase.updateBasicTaskNome(basicTask);

            });
        }

        private void transacaoEditar(boolean editarAtivado) {
            if (editarAtivado) {
                layoutItem.setVisibility(View.GONE);
                layoutItem.setEnabled(false);
                layoutEditItem.setVisibility(View.VISIBLE);
                layoutEditItem.setEnabled(false);
                campoAddSubTask.getEditText().setText(basicTask.getNome());
            } else {
                layoutItem.setEnabled(true);
                layoutItem.setVisibility(View.VISIBLE);
                layoutEditItem.setVisibility(View.GONE);
                layoutEditItem.setEnabled(true);
                nome.setText(basicTask.getNome());
            }

        }
    }


}

