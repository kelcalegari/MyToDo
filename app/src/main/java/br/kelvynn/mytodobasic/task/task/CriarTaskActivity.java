package br.kelvynn.mytodobasic.task.task;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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
import br.kelvynn.mytodobasic.dados.task.SubTask;
import br.kelvynn.mytodobasic.dados.task.Task;
import br.kelvynn.mytodobasic.task.categoria.CategoriaActivity;

@SuppressWarnings("ALL")
public class CriarTaskActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Task task = new Task();
    private ArrayList<String> nomesCategorias;
    private ArrayList<String> idCategorias;
    private Spinner spinnerCategoria;
    private RecyclerView recyclerView;
    private HelperFirebase helperFirebase;
    private Boolean isNotSaved = true;
    private DatePickerDialog datePickerDialog;
    private TextInputLayout titulo, vencimento, descricao, nomeAddSubTask;
    private Spinner spinnerPrioriedade;
    private ImageButton addCategoria, addSubTask, close;
    private Button salvar;
    private Calendar dataVencimento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_task);
        helperFirebase = new HelperFirebase();
        task.setId(helperFirebase.CreateIdTASK());


        iniciarComponentes();

        onRefresh();
        atualizarSpinnerPrioriedade();
        atualizarSpinnerCategoria();


        vencimento.getEditText().setOnClickListener(view -> {
            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR); // current year
            int mMonth = c.get(Calendar.MONTH); // current month
            int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
            // date picker dialog
            datePickerDialog = new DatePickerDialog(CriarTaskActivity.this,
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


        addCategoria.setOnClickListener(view -> {
            Intent it = new Intent(CriarTaskActivity.this, CategoriaActivity.class);
            startActivity(it);
        });

        addSubTask.findViewById(R.id.buttonAddSubTask).setOnClickListener(view -> addSubTask());

        salvar.setOnClickListener(view -> salvarTask());

        close.findViewById(R.id.closeButton).setOnClickListener(view -> finish());
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
            task.setDescricao(descricao.getEditText().getText().toString());
            task.setIdCategoriaTask(idCategorias.get(spinnerCategoria.getSelectedItemPosition()));
            task.setNomeCategoria(((String) spinnerCategoria.getSelectedItem()));
            task.setConcluido(false);
            dataVencimento.setTime(dataFormatada);
            task.setDataVencimento(dataVencimento);
            Calendar ontem = Calendar.getInstance();
            ontem.add(Calendar.DAY_OF_MONTH, -1);
            task.setAtrasado(ontem.after(dataVencimento));

            helperFirebase.createTask(task);
            isNotSaved = false;
            finish();
        }
    }

    private void addSubTask() {
        if (nomeAddSubTask.getEditText().getText().toString().equals("")) {
            nomeAddSubTask.getEditText().setError("Informar o SubTask!!");
            nomeAddSubTask.getEditText().requestFocus();
        } else {

            HelperFirebase helperFirebase = new HelperFirebase();
            helperFirebase.createSubTask(new SubTask(nomeAddSubTask.getEditText().getText().toString(), false), task.getId());
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
        spinnerPrioriedade.setOnItemSelectedListener(this);

    }

    private void iniciarComponentes() {
        titulo = findViewById(R.id.campoTitulo);
        vencimento = findViewById(R.id.campoData);
        descricao = findViewById(R.id.campoDescricao);
        nomeAddSubTask = findViewById(R.id.campoAddSubTask);
        spinnerPrioriedade = findViewById(R.id.spinnerPrioriedade);
        addCategoria = findViewById(R.id.buttonAddCategoria);
        close = findViewById(R.id.closeButton);
        addSubTask = findViewById(R.id.buttonAddSubTask);
        salvar = findViewById(R.id.buttonSalvar);
        nomesCategorias = new ArrayList<>();
        idCategorias = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerviewSubTask);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        spinnerCategoria = findViewById(R.id.spinnerCategoria);
        dataVencimento = Calendar.getInstance();
        Locale locale = new Locale("pt", "BR");
        Locale.setDefault(locale);
    }

    public void atualizarSpinnerCategoria() {

        helperFirebase.getAllNameCategoria(this, idCategorias, nomesCategorias, spinnerCategoria, task);
    }

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
        atualizarSpinnerCategoria();
        super.onResume();
    }


    @Override
    protected void onDestroy() {
        if (isNotSaved) {
            helperFirebase.delateAllSubTask(task.getId());
        }
        super.onDestroy();
    }

    public void onRefresh() {

        helperFirebase.getAllSubTask(this, recyclerView, task.getId());

    }


}