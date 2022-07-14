package br.kelvynn.mytodobasic.task;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import br.kelvynn.mytodobasic.R;
import br.kelvynn.mytodobasic.menu.MenuActivity;
import br.kelvynn.mytodobasic.task.task.CriarTaskActivity;
import br.kelvynn.mytodobasic.task.task.list.CategoriaOrderFragment;
import br.kelvynn.mytodobasic.task.task.list.ConcluidoFragment;
import br.kelvynn.mytodobasic.task.task.list.HojeTaskFragment;
import br.kelvynn.mytodobasic.task.task.list.PrioriedadeOrderFragment;
import br.kelvynn.mytodobasic.task.task.list.TaskFragment;


public class TaskActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private ImageButton menu;
    private ImageButton newTask;
    private Spinner spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        FirebaseUser usuarioAtual = FirebaseAuth.getInstance().getCurrentUser();
        if (usuarioAtual != null) {

            inicializarComponentes();
            inicializarSpinnerList();

            newTask.setOnClickListener(view -> {
                Intent it = new Intent(TaskActivity.this, CriarTaskActivity.class);
                startActivity(it);
            });

            menu.setOnClickListener(view -> {
                Intent it = new Intent(TaskActivity.this, MenuActivity.class);
                startActivity(it);
            });
        }

    }

    private void inicializarSpinnerList() {
        ArrayAdapter<CharSequence> adpter = ArrayAdapter.createFromResource(this, R.array.tipoLista, R.layout.spinner_item);
        adpter.setDropDownViewResource(R.layout.spinner_item_dropdown);
        spinner.setAdapter(adpter);
        spinner.setOnItemSelectedListener(this);
        spinner.setSelection(1);

    }

    private void inicializarComponentes() {
        menu = findViewById(R.id.menu);
        newTask = findViewById(R.id.imageAddTask);
        spinner = findViewById(R.id.spinner);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) { //or switch-case
            finish();
        }

        if (id == R.id.menu) {
            Intent it = new Intent(TaskActivity.this, MenuActivity.class);
            startActivity(it);

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (i == 0) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentPrincipal, new HojeTaskFragment()).commit();
        } else if (i == 1) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentPrincipal, new TaskFragment()).commit();
        } else if (i == 2) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentPrincipal, new CategoriaOrderFragment()).commit();
        } else if (i == 3) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentPrincipal, new PrioriedadeOrderFragment()).commit();
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentPrincipal, new ConcluidoFragment()).commit();
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }


}