package br.kelvynn.mytodobasic.basictask;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.google.android.material.textfield.TextInputLayout;

import br.kelvynn.mytodobasic.R;
import br.kelvynn.mytodobasic.dados.HelperFirebase;
import br.kelvynn.mytodobasic.dados.basicTask.BasicTask;
import br.kelvynn.mytodobasic.menu.MenuActivity;

@SuppressWarnings("ALL")
public class BasicTaskActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Spinner spinner;
    private ImageButton menu, newTask;
    private View view;
    private RelativeLayout layoutNewTask;
    private TextInputLayout campoTitulo;
    private Button salvar;
    private HelperFirebase helperFirebase;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_task);
        inicializarComponentes();
        inicializarSpinnerList();

        menu.setOnClickListener(view -> {
            Intent it = new Intent(BasicTaskActivity.this, MenuActivity.class);
            startActivity(it);
        });

        newTask.setOnClickListener(view -> mudarContext(true, View.VISIBLE));

        view.setOnLongClickListener(view -> {
            mudarContext(false, View.GONE);
            esconderTeclado();
            return false;
        });


        salvar.setOnClickListener(view -> {
            if(campoTitulo.getEditText().getText().toString().equals("")){
                campoTitulo.getEditText().setError("Verificar Task!!");
                campoTitulo.getEditText().requestFocus();
            }
            else{
                esconderTeclado();
                campoTitulo.setErrorEnabled(false);
                campoTitulo.requestFocus();
                helperFirebase.createBasicTask(new BasicTask(campoTitulo.getEditText().getText().toString(),false));
                campoTitulo.getEditText().setText("");
                mudarContext(false, View.GONE);
                onRefresh();
            }
        });





    }

    private void mudarContext(boolean b, int visibilidade) {
        layoutNewTask.setEnabled(b);
        layoutNewTask.setVisibility(visibilidade);
    }

    private void esconderTeclado() {
        InputMethodManager imm = (InputMethodManager) getBaseContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(campoTitulo.getEditText().getWindowToken(), 0);
    }

    private void inicializarComponentes() {
        menu = (ImageButton) findViewById(R.id.menu);
        newTask = (ImageButton) findViewById(R.id.imageAddTask);
        spinner = findViewById(R.id.spinner);
        helperFirebase = new HelperFirebase();
        view = (View) findViewById(R.id.viewSair);
        layoutNewTask = (RelativeLayout) findViewById(R.id.layoutAddBasicTask);
        campoTitulo = (TextInputLayout) findViewById(R.id.campoBasicTask);
        salvar = (Button) findViewById(R.id.buttonSalvar);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerviewBasicTask);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        onRefresh();
    }

    private void inicializarSpinnerList() {
        ArrayAdapter<CharSequence> adpter = ArrayAdapter.createFromResource(this, R.array.ListSubTask, R.layout.spinner_item);
        adpter.setDropDownViewResource(R.layout.spinner_item_dropdown);
        spinner.setAdapter(adpter);
        spinner.setOnItemSelectedListener(this);

       }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (i) {
            case 0:
                helperFirebase.getNotConcluidoBasicTask(this, recyclerView);
                break;
            case 1:
                helperFirebase.getConcluidoBasicTask(this, recyclerView);
                break;
            case 2:
                helperFirebase.getAllBasicTask(this, recyclerView);
                break;
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void onRefresh() {
        switch (spinner.getSelectedItemPosition()) {
            case 0:
                helperFirebase.getNotConcluidoBasicTask(this, recyclerView);
                break;
            case 1:
                helperFirebase.getConcluidoBasicTask(this, recyclerView);
                break;
            case 2:
                helperFirebase.getAllBasicTask(this, recyclerView);
                break;

        }


    }
}