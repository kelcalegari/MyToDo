package br.kelvynn.mytodobasic.faculdade.task;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import br.kelvynn.mytodobasic.dados.task.SubTask;
import br.kelvynn.mytodobasic.faculdade.materia.MateriaActivity;

@SuppressWarnings("ALL")
public class CriarTaskFacudadeActivity extends AppCompatActivity {
    private FaculdadeTask task = new FaculdadeTask();
    private ArrayList<String> nomesMaterias;
    private ArrayList<String> idMaterias;
    private Spinner spinnerMateria;
    private RecyclerView recyclerView;
    private HelperFirebase helperFirebase;
    private Boolean isNotSaved = true;
    private TextInputLayout titulo;
    private TextInputLayout vencimento;
    private TextInputLayout descricao;
    private TextInputLayout nomeAddSubTask;
    private Spinner spinnerPrioriedade;
    private ImageButton addMateria;
    private ImageButton close;
    private ImageButton addSubTask;
    private Button salvar;
    private Calendar dataVencimento;
    private DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_task_facudade);
        helperFirebase = new HelperFirebase();
        task.setId(helperFirebase.createIdFaculdadeTask());


        iniciarComponentes();

        onRefresh();
        atualizarSpinnerPrioriedade();
        atualizarSpinnerMateria();


        addMateria.setOnClickListener(view -> {
            Intent it = new Intent(CriarTaskFacudadeActivity.this, MateriaActivity.class);
            startActivity(it);
        });

        addSubTask.findViewById(R.id.buttonAddSubTask).setOnClickListener(view -> addSubTask());

        salvar.setOnClickListener(view -> salvarTask());

        close.findViewById(R.id.closeButton).setOnClickListener(view -> finish());

        vencimento.getEditText().setOnClickListener(view -> {
            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR); // current year
            int mMonth = c.get(Calendar.MONTH); // current month
            int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
            // date picker dialog
            datePickerDialog = new DatePickerDialog(CriarTaskFacudadeActivity.this,
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
            task.setTitulo(titulo.getEditText().getText().toString());
            task.setPrioriedade(spinnerPrioriedade.getSelectedItemPosition());
            task.setDescricao(descricao.getEditText().getText().toString());
            task.setIdMateria(idMaterias.get(spinnerMateria.getSelectedItemPosition()));
            task.setNomeMateria(((String) spinnerMateria.getSelectedItem()));
            task.setConcluido(false);
            dataVencimento.setTime(dataFormatada);
            task.setDataVencimento(dataVencimento);
            Calendar ontem = Calendar.getInstance();
            ontem.add(Calendar.DAY_OF_MONTH, -1);
            task.setAtrasado(ontem.after(dataVencimento));

            helperFirebase.createFaculdadeTask(task);
            isNotSaved = false;
            finish();
        }
    }

    private void addSubTask() {
        if (nomeAddSubTask.getEditText().getText().toString().equals("")) {
            nomeAddSubTask.getEditText().setError("Informar o SubTask!!");
            nomeAddSubTask.getEditText().requestFocus();
        } else {
            helperFirebase.createSubTaskFaculdade(new SubTask(nomeAddSubTask.getEditText().getText().toString(), false), task.getId());

            onRefresh();
            nomeAddSubTask.setErrorEnabled(false);
            nomeAddSubTask.requestFocus();
            nomeAddSubTask.getEditText().setText("");

        }
    }

    private void atualizarSpinnerPrioriedade() {
        ArrayAdapter<CharSequence> adapterPrioriedade = ArrayAdapter.createFromResource(this, R.array.Prioriedade, android.R.layout.simple_spinner_item);
        adapterPrioriedade.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPrioriedade.setAdapter(adapterPrioriedade);

    }

    private void iniciarComponentes() {
        titulo = findViewById(R.id.campoTitulo);
        vencimento = findViewById(R.id.campoData);
        descricao = findViewById(R.id.campoDescricao);
        nomeAddSubTask = findViewById(R.id.campoAddSubTask);
        spinnerPrioriedade = findViewById(R.id.spinnerPrioriedade);
        addMateria = findViewById(R.id.buttonAddMateria);
        close = findViewById(R.id.closeButton);
        addSubTask = findViewById(R.id.buttonAddSubTask);
        salvar = findViewById(R.id.buttonSalvar);
        nomesMaterias = new ArrayList<>();
        idMaterias = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerviewSubTask);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        spinnerMateria = findViewById(R.id.spinnerMateria);
        dataVencimento = Calendar.getInstance();
        Locale locale = new Locale("pt", "BR");
        Locale.setDefault(locale);
    }

    public void atualizarSpinnerMateria() {

        helperFirebase.getAllNameMateria(this, idMaterias, nomesMaterias, spinnerMateria, task);
    }


    @Override
    public void onResume() {
        atualizarSpinnerMateria();
        super.onResume();
    }


    @Override
    protected void onDestroy() {
        if (isNotSaved) {
            helperFirebase.delateAllSubTaskFaculdade(task.getId());
        }
        super.onDestroy();
    }

    public void onRefresh() {

        helperFirebase.getAllSubTaskFaculdade(this, recyclerView, task.getId());

    }


}