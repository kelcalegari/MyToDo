package br.kelvynn.mytodobasic.task.task.subtask;

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
import br.kelvynn.mytodobasic.dados.task.SubTask;

@SuppressWarnings("ALL")
public class AdapterSubTask extends RecyclerView.Adapter<AdapterSubTask.ViewHolder> {
    @SuppressWarnings("FieldMayBeFinal")
    private List<SubTask> subTasks;
    private LayoutInflater layoutInflater;
    private String idTask;
    private HelperFirebase helperFirebase;
    private int tipoLista; // 0 = somente não concluido, 1 = concluido, 2 todos;


    public AdapterSubTask(Context context, List<SubTask> subTasks, String idTask, int tipoLista) {
        this.layoutInflater = LayoutInflater.from(context);
        this.subTasks = subTasks;
        this.idTask = idTask;
        this.helperFirebase = new HelperFirebase();
        this.tipoLista = tipoLista;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = layoutInflater.inflate(R.layout.card_sub_task, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterSubTask.ViewHolder holder, int position) {
        SubTask subTask = this.subTasks.get(position);
        String nome = subTask.getNome();
        holder.subTask = subTask;
        holder.nome.setText(nome);
        if (subTask.getConcluido()) {
            holder.radio.setChecked(true);
        }
    }

    @Override
    public int getItemCount() {
        return subTasks.size();
    }


    @SuppressWarnings("ConstantConditions")
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nome;
        CheckBox radio;
        SubTask subTask;
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
                if (b != subTask.getConcluido()) {
                    subTask.setConcluido(b);
                    helperFirebase.updateSubTaskSetConcluido(subTask, idTask);
                    if (tipoLista != 2) {
                        notifyItemRemoved(subTasks.indexOf(subTask));
                        subTasks.remove(subTask);
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
                helperFirebase.delateSubTask(subTask, idTask);
                notifyItemRemoved(subTasks.indexOf(subTask));
                subTasks.remove(subTask);
            });

            buttonSalvar.setOnClickListener(view -> {
                transacaoEditar(false);
                subTask.setNome(campoAddSubTask.getEditText().getText().toString());
                nome.setText(subTask.getNome());
                helperFirebase.updateSubTaskNome(subTask, idTask);

            });
        }

        private void transacaoEditar(boolean editarAtivado) {
            if (editarAtivado) {
                layoutItem.setVisibility(View.GONE);
                layoutItem.setEnabled(false);
                layoutEditItem.setVisibility(View.VISIBLE);
                layoutEditItem.setEnabled(false);
                campoAddSubTask.getEditText().setText(subTask.getNome());
            } else {
                layoutItem.setEnabled(true);
                layoutItem.setVisibility(View.VISIBLE);
                layoutEditItem.setVisibility(View.GONE);
                layoutEditItem.setEnabled(true);
                nome.setText(subTask.getNome());
            }

        }
    }


}

