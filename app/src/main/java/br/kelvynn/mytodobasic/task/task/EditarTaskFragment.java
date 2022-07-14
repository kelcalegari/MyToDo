package br.kelvynn.mytodobasic.task.task;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import br.kelvynn.mytodobasic.R;
import br.kelvynn.mytodobasic.dados.HelperFirebase;
import br.kelvynn.mytodobasic.dados.task.Task;
import br.kelvynn.mytodobasic.task.TaskActivity;
import br.kelvynn.mytodobasic.task.categoria.CategoriaActivity;


@SuppressWarnings("ALL")
public class EditarTaskFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private Task task = new Task();

    private ArrayList<String> nomesCategorias;
    private ArrayList<String> idCategorias;
    private DatePickerDialog datePickerDialog;
    private Spinner spinnerCategoria;
    private Spinner spinnerPrioriedade;

    private TextInputLayout titulo;
    private TextInputLayout vencimento;
    private TextInputLayout descricao;


    public EditarTaskFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        this.task = (Task) bundle.getSerializable("task");


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_editar_task, container, false);

        iniciarComponentes(v);
        atualizarSpinnerPrioriedade();

        v.findViewById(R.id.buttonExcluir).setOnClickListener(view -> excluirTask());

        v.findViewById(R.id.buttonAddCategoria).setOnClickListener(view -> {
            Intent it = new Intent(getActivity(), CategoriaActivity.class);
            startActivity(it);
        });

        v.findViewById(R.id.buttonSalvar).setOnClickListener(view -> salvarTask());

        vencimento.getEditText().setOnClickListener(view -> {
            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR); // current year
            int mMonth = c.get(Calendar.MONTH); // current month
            int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
            // date picker dialog
            datePickerDialog = new DatePickerDialog(getContext(),
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(
                                DatePicker view,
                                int year,
                                int monthOfYear, int dayOfMonth) {
                            // set day of month , month and year value in the edit text
                            vencimento.getEditText().setText(dayOfMonth + "/"
                                    + (monthOfYear + 1) + "/" + year);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        });
        return v;
    }

    private void salvarTask() {
        boolean valido = true;
        Date dataFormatada = new Date();
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        if (titulo.getEditText().getText().toString().equals("")) {
            titulo.getEditText().setError("Verificar título!!");
            titulo.getEditText().requestFocus();
            valido = false;
        }
        if (!vencimento.getEditText().getText().toString().equals("")) {
            try {
                dataFormatada = formato.parse(vencimento.getEditText().getText().toString());
            } catch (ParseException e) {
                vencimento.getEditText().setError("Verificar data!!");
                vencimento.getEditText().requestFocus();
                valido = false;
            }
        }
        if (valido) {
            HelperFirebase helperFirebase = new HelperFirebase();
            task.setTitulo(titulo.getEditText().getText().toString());
            task.setDescricao(descricao.getEditText().getText().toString());
            task.setPrioriedade(spinnerPrioriedade.getSelectedItemPosition());
            task.setNomeCategoria(spinnerCategoria.getSelectedItem().toString());
            task.setIdCategoriaTask(idCategorias.get(spinnerCategoria.getSelectedItemPosition()));
            Calendar vencimento = Calendar.getInstance();
            vencimento.setTime(dataFormatada);
            task.setDataVencimento(vencimento);

            helperFirebase.updateTASK(task);

            VisualizarTask visualizarTask = new VisualizarTask();
            Bundle extra = new Bundle();
            extra.putSerializable("task", task);
            visualizarTask.setArguments(extra);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentTaskPrincipal, visualizarTask).commit();

        }
    }

    private void excluirTask() {
        HelperFirebase helperFirebase = new HelperFirebase();
        helperFirebase.deleteTask(task.getId());
        Intent it = new Intent(getContext(), TaskActivity.class);
        startActivity(it);
    }

    private void iniciarComponentes(View v) {
        titulo = v.findViewById(R.id.campoTitulo);
        vencimento = v.findViewById(R.id.campoData);
        descricao = v.findViewById(R.id.campoDescricao);
        titulo.getEditText().setText(task.getTitulo());
        vencimento.getEditText().setText(task.getDataVencimentoStringShow());
        descricao.getEditText().setText(task.getDescricao());
        spinnerPrioriedade = v.findViewById(R.id.spinnerPrioriedade);
        spinnerCategoria = v.findViewById(R.id.spinnerCategoria);
        nomesCategorias = new ArrayList<>();
        idCategorias = new ArrayList<>();
        Locale locale = new Locale("pt", "BR");
        Locale.setDefault(locale);
    }

    private void atualizarSpinnerPrioriedade() {
        ArrayAdapter<CharSequence> adapterPrioriedade = ArrayAdapter.createFromResource(getContext(), R.array.Prioriedade, android.R.layout.simple_spinner_item);
        adapterPrioriedade.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPrioriedade.setAdapter(adapterPrioriedade);
        spinnerPrioriedade.setSelection(task.getPrioriedade());
        spinnerPrioriedade.setOnItemSelectedListener(this);
    }

    public void atualizarSpinnerCategoria() {
        HelperFirebase helperFirebase = new HelperFirebase();
        helperFirebase.getAllNameCategoria(getContext(), idCategorias, nomesCategorias, spinnerCategoria, task);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        ArrayList<String> prioriedade = new ArrayList<>();
        prioriedade.add("Baixa");
        prioriedade.add("Media");
        prioriedade.add("Alta");
        prioriedade.add("Urgente");

        //noinspection SuspiciousMethodCalls
        if (prioriedade.contains(adapterView.getSelectedItem())) {
            this.task.setPrioriedade(i);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onResume() {
        atualizarSpinnerCategoria();
        super.onResume();
    }


}

