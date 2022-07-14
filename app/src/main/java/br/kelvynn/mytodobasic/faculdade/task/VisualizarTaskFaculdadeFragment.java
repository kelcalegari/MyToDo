package br.kelvynn.mytodobasic.faculdade.task;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.widget.CompoundButtonCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

import br.kelvynn.mytodobasic.R;
import br.kelvynn.mytodobasic.dados.HelperFirebase;
import br.kelvynn.mytodobasic.dados.faculdade.FaculdadeTask;
import br.kelvynn.mytodobasic.dados.task.SubTask;


@SuppressWarnings("ALL")
public class VisualizarTaskFaculdadeFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private FaculdadeTask task;
    private RecyclerView recyclerView;

    private TextView titulo;
    private TextView vencimento;
    private TextView descricao;
    private TextView prioriedade;
    private TextView materia;
    private TextInputLayout nomeAddSubTask;
    private CheckBox concluir;
    private ImageButton addSubTask;
    private HelperFirebase helperFirebase;
    private Spinner spinnerListSubTask;


    public VisualizarTaskFaculdadeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        this.task = (FaculdadeTask) bundle.getSerializable("task");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_visualizar_task_faculdade, container, false);
        iniciarComponentes(v);
        atualizarSpinnerListSubTask();
        onRefresh();

        spinnerListSubTask.setOnFocusChangeListener((view, b) -> onRefresh());

        if (task.isConcluido()) {
            setVisiblityGone(v, View.GONE, false);

        }

        v.findViewById(R.id.buttonAddSubTask).setOnClickListener(view -> addSubTask());

        ColorStateList corBox;

        switch (task.getPrioriedadeString().length()) {
            case 1:
                corBox = ContextCompat.getColorStateList(getContext(), R.color.prioBaixa);
                CompoundButtonCompat.setButtonTintList(concluir, corBox);
                break;
            case 2:
                corBox = ContextCompat.getColorStateList(getContext(), R.color.prioMedia);
                CompoundButtonCompat.setButtonTintList(concluir, corBox);
                break;
            case 3:
                corBox = ContextCompat.getColorStateList(getContext(), R.color.prioAlta);
                CompoundButtonCompat.setButtonTintList(concluir, corBox);
                break;
            default:
                corBox = ContextCompat.getColorStateList(getContext(), R.color.prioUrgente);
                CompoundButtonCompat.setButtonTintList(concluir, corBox);
        }

        if (task.isConcluido()) {
            concluir.setChecked(true);
        } else if (task.isAtrasado()) {
            vencimento.setTextColor(Color.RED);
        }


        setTextsView();
        v.findViewById(R.id.checkBoxConcluir).setOnClickListener(view -> altTaskConclusao(v));

        v.findViewById(R.id.buttonExcluir).setOnClickListener(view -> excluirTask());

        v.findViewById(R.id.buttonEditar).setOnClickListener(view -> editarTask());


        titulo.setOnLongClickListener(view -> {
            editarTask();
            return false;
        });
        descricao.setOnLongClickListener(view -> {
            editarTask();
            return false;
        });
        vencimento.setOnLongClickListener(view -> {
            editarTask();
            return false;
        });

        return v;
    }

    private void altTaskConclusao(View v) {
        if (concluir.isChecked() && (!task.isConcluido())) {
            task.setConcluido(true);
            Calendar concluido = Calendar.getInstance();
            task.setDataConclusao(concluido);
            helperFirebase.updateFaculdadeTASK(task);
            setVisiblityGone(v, View.GONE, false);

        }
        if (!concluir.isChecked() && (task.isConcluido())) {
            task.setConcluido(false);
            task.delConcluido();
            helperFirebase.updateFaculdadeTASK(task);
            setVisiblityGone(v, View.VISIBLE, true);

        }
    }

    private void setTextsView() {

        titulo.setText(task.getTitulo());
        vencimento.setText(task.getDataVencimentoStringShow());
        descricao.setText(task.getDescricao());
        String[] prioriedades = getResources().getStringArray(R.array.Prioriedade);
        prioriedade.setText(String.valueOf(prioriedades[task.getPrioriedade()]));
        materia.setText(this.task.getNomeMateria());
    }

    private void excluirTask() {
        helperFirebase.deleteFaculdadeTask(task);
        getActivity().supportFinishAfterTransition();
    }

    private void addSubTask() {
        if (nomeAddSubTask.isEnabled()) {
            if (nomeAddSubTask.getEditText().getText().toString().equals("")) {
                nomeAddSubTask.getEditText().setError("Informar o SubTask!!");
                nomeAddSubTask.getEditText().requestFocus();
            } else {
                helperFirebase.createSubTaskFaculdade(new SubTask(nomeAddSubTask.getEditText().getText().toString(), false), task.getId());
                onRefresh();
                nomeAddSubTask.setErrorEnabled(false);
                nomeAddSubTask.requestFocus();
                nomeAddSubTask.getEditText().setText("");
                nomeAddSubTask.setEnabled(false);
                nomeAddSubTask.setVisibility(View.GONE);
            }
        } else {
            nomeAddSubTask.setEnabled(true);
            nomeAddSubTask.setVisibility(View.VISIBLE);
            nomeAddSubTask.requestFocus();
        }
    }

    private void setVisiblityGone(View v, int gone, boolean b) {
        addSubTask.setVisibility(gone);
        addSubTask.setEnabled(b);
        addSubTask.setFocusable(b);
        RelativeLayout relativeLayout = v.findViewById(R.id.layoutSubTasks);
        relativeLayout.setVisibility(gone);
        recyclerView.setVisibility(gone);
        recyclerView.setEnabled(b);
    }

    private void iniciarComponentes(View v) {
        titulo = v.findViewById(R.id.campoTitulo);
        vencimento = v.findViewById(R.id.campoData);
        descricao = v.findViewById(R.id.campoDescricao);
        prioriedade = v.findViewById(R.id.campoPrioriedade);
        materia = v.findViewById(R.id.campoMateria);
        nomeAddSubTask = v.findViewById(R.id.campoAddSubTask);
        concluir = v.findViewById(R.id.checkBoxConcluir);
        addSubTask = v.findViewById(R.id.buttonAddSubTask);
        recyclerView = v.findViewById(R.id.recyclerviewSubTask);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        TextView dataconclusao = v.findViewById(R.id.campoDataConluido);
        TextView lebelConclusao = v.findViewById(R.id.lebelConluido);
        spinnerListSubTask = v.findViewById(R.id.spinnerListSubTask);
        helperFirebase = new HelperFirebase();
    }

    private void editarTask() {
        EditarFaculdadeTaskFragment editarTaskFragment = new EditarFaculdadeTaskFragment();
        Bundle extra = new Bundle();
        extra.putSerializable("task", task);
        editarTaskFragment.setArguments(extra);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentTaskPrincipal, editarTaskFragment).addToBackStack("EditarTaskFragment").commit();
    }

    public void onRefresh() {
        switch (spinnerListSubTask.getSelectedItemPosition()) {
            case 0:
                helperFirebase.getNotConcluidoSubTaskFaculdade(getContext(), recyclerView, task.getId());
                break;
            case 1:
                helperFirebase.getConcluidoSubTaskFaculdade(getContext(), recyclerView, task.getId());
                break;
            case 2:
                helperFirebase.getAllSubTaskFaculdade(getContext(), recyclerView, task.getId());
                break;

        }


    }

    private void atualizarSpinnerListSubTask() {
        ArrayAdapter<CharSequence> adapterListSubTask = ArrayAdapter.createFromResource(getContext(), R.array.ListSubTask, android.R.layout.simple_spinner_item);
        adapterListSubTask.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerListSubTask.setAdapter(adapterListSubTask);
        spinnerListSubTask.setOnItemSelectedListener(this);


    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (i) {
            case 0:
                helperFirebase.getNotConcluidoSubTaskFaculdade(getContext(), recyclerView, task.getId());
                break;
            case 1:
                helperFirebase.getConcluidoSubTaskFaculdade(getContext(), recyclerView, task.getId());
                break;
            case 2:
                helperFirebase.getAllSubTaskFaculdade(getContext(), recyclerView, task.getId());
                break;
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


}