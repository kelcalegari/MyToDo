package br.kelvynn.mytodobasic.faculdade.task.list;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.widget.CompoundButtonCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;
import java.util.List;

import br.kelvynn.mytodobasic.R;
import br.kelvynn.mytodobasic.dados.HelperFirebase;
import br.kelvynn.mytodobasic.dados.faculdade.FaculdadeTask;

@SuppressWarnings("ALL")
public class AdapterTaskFaculdade extends RecyclerView.Adapter<AdapterTaskFaculdade.ViewHolder> {
    @SuppressWarnings("FieldMayBeFinal")
    private List<FaculdadeTask> tasks;
    @SuppressWarnings("FieldMayBeFinal")
    private LayoutInflater layoutInflater;
    @SuppressWarnings("FieldMayBeFinal")
    private Context context;
    @SuppressWarnings("FieldMayBeFinal")
    private HelperFirebase helperFirebase;


    public AdapterTaskFaculdade(Context context, List<FaculdadeTask> tasks) {
        this.layoutInflater = LayoutInflater.from(context);
        this.tasks = tasks;
        this.context = context;
        this.helperFirebase = new HelperFirebase();

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = layoutInflater.inflate(R.layout.card_task, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterTaskFaculdade.ViewHolder holder, int position) {
        FaculdadeTask task = this.tasks.get(position);
        String titulo = task.getTitulo() + task.getPrioriedadeString();
        String data = task.getDataVencimentoStringShow();
        String categoria = task.getNomeMateria();
        if (task.isConcluido()) {
            String dataConclusao = task.getDataConclusaoStringShow();
            holder.labelConclusao.setVisibility(View.VISIBLE);
            holder.dataConcluido.setVisibility(View.VISIBLE);
            holder.dataConcluido.setText(dataConclusao);
        }

        holder.titulo.setText(titulo);
        holder.data.setText(data);
        holder.categoria.setText(categoria);
        holder.task = task;

        if (task.isConcluido()) {
            holder.radio.setChecked(true);
        } else if (task.isAtrasado()) {
            holder.data.setTextColor(Color.RED);
        }
        ColorStateList corBox;

        switch (task.getPrioriedadeString().length()) {
            case 1:
                corBox = ContextCompat.getColorStateList(context, R.color.prioBaixa);
                CompoundButtonCompat.setButtonTintList(holder.radio, corBox);
                break;
            case 2:
                corBox = ContextCompat.getColorStateList(context, R.color.prioMedia);
                CompoundButtonCompat.setButtonTintList(holder.radio, corBox);
                break;
            case 3:
                corBox = ContextCompat.getColorStateList(context, R.color.prioAlta);
                CompoundButtonCompat.setButtonTintList(holder.radio, corBox);
                break;
            default:
                corBox = ContextCompat.getColorStateList(context, R.color.prioUrgente);
                CompoundButtonCompat.setButtonTintList(holder.radio, corBox);
        }

    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView titulo, data, categoria, labelConclusao, dataConcluido;
        FaculdadeTask task;
        CheckBox radio;
        RelativeLayout layout;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titulo = itemView.findViewById(R.id.textTitulo);
            data = itemView.findViewById(R.id.textData);
            radio = itemView.findViewById(R.id.radioButton);
            layout = itemView.findViewById(R.id.layoutItem);
            categoria = itemView.findViewById(R.id.textCatMat);
            labelConclusao = itemView.findViewById(R.id.lebelConluido);
            dataConcluido = itemView.findViewById(R.id.textDataConclusao);

            radio.setOnCheckedChangeListener((compoundButton, b) -> {
                if (radio.isChecked() && (!task.isConcluido())) {
                    task.setConcluido(true);
                    Calendar concluido = Calendar.getInstance();
                    task.setDataConclusao(concluido);

                    helperFirebase.updateFaculdadeTaskConcluido(task);
                    notifyItemRemoved(tasks.indexOf(task));
                    tasks.remove(task);

                }
                if (!radio.isChecked() && (task.isConcluido())) {
                    task.setConcluido(false);
                    task.delConcluido();

                    helperFirebase.updateFaculdadeTaskConcluido(task);
                    notifyItemRemoved(tasks.indexOf(task));
                    tasks.remove(task);
                }
            });

            layout.setOnClickListener(view -> {
                Intent it = new Intent(context, FaculdadeTaskActivity.class);
                it.putExtra("task", task);
                view.getContext().startActivity(it);
            });
        }
    }


}

