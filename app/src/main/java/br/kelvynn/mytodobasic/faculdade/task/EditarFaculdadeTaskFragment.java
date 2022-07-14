package br.kelvynn.mytodobasic.faculdade.task;

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
import br.kelvynn.mytodobasic.dados.faculdade.FaculdadeTask;
import br.kelvynn.mytodobasic.faculdade.FaculdadeMain;
import br.kelvynn.mytodobasic.faculdade.materia.MateriaActivity;


@SuppressWarnings("ALL")
public class EditarFaculdadeTaskFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private FaculdadeTask task = new FaculdadeTask();

    private ArrayList<String> nomesMateria;
    private ArrayList<String> idMateria;

    private Spinner spinnerMateria;
    private Spinner spinnerPrioriedade;
    private DatePickerDialog datePickerDialog;
    private TextInputLayout titulo;
    private TextInputLayout vencimento;
    private TextInputLayout descricao;

    public EditarFaculdadeTaskFragment() {
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

        View v = inflater.inflate(R.layout.fragment_editar_faculdade_task, container, false);

        iniciarComponentes(v);
        atualizarSpinnerPrioriedade();

        v.findViewById(R.id.buttonExcluir).setOnClickListener(view -> excluirTask());

        v.findViewById(R.id.buttonAddMateria).setOnClickListener(view -> {
            Intent it = new Intent(getActivity(), MateriaActivity.class);
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
            task.setNomeMateria(spinnerMateria.getSelectedItem().toString());
            task.setIdMateria(idMateria.get(spinnerMateria.getSelectedItemPosition()));
            Calendar emissao = Calendar.getInstance();
            Calendar vencimento = Calendar.getInstance();
            vencimento.setTime(dataFormatada);
            task.setDataVencimento(vencimento);

            helperFirebase.updateFaculdadeTASK(task);


            VisualizarTaskFaculdadeFragment visualizarTask = new VisualizarTaskFaculdadeFragment();
            Bundle extra = new Bundle();
            extra.putSerializable("task", task);
            visualizarTask.setArguments(extra);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentTaskPrincipal, visualizarTask).commit();

        }
    }

    private void excluirTask() {
        HelperFirebase helperFirebase = new HelperFirebase();
        helperFirebase.deleteFaculdadeTask(task);
        Intent it = new Intent(getContext(), FaculdadeMain.class);
        it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
        spinnerMateria = v.findViewById(R.id.spinnerMateria);
        nomesMateria = new ArrayList<>();
        idMateria = new ArrayList<>();
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

    public void atualizarSpinnerMateria() {
        HelperFirebase helperFirebase = new HelperFirebase();
        helperFirebase.getAllNameMateria(getContext(), idMateria, nomesMateria, spinnerMateria, task);
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        ArrayList<String> prioriedade = new ArrayList<>();
        prioriedade.add("Baixa");
        prioriedade.add("Media");
        prioriedade.add("Alta");
        prioriedade.add("Urgente");

        if (prioriedade.contains(adapterView.getSelectedItem())) {
            this.task.setPrioriedade(i);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onResume() {
        atualizarSpinnerMateria();
        super.onResume();
    }


}

