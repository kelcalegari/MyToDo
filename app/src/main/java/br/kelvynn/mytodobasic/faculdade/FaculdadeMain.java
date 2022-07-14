package br.kelvynn.mytodobasic.faculdade;

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
import br.kelvynn.mytodobasic.faculdade.task.CriarTaskFacudadeActivity;
import br.kelvynn.mytodobasic.faculdade.task.list.ConcluidoFragment;
import br.kelvynn.mytodobasic.faculdade.task.list.FaculdadeTaskFragment;
import br.kelvynn.mytodobasic.faculdade.task.list.HojeTaskFaculdadeFragment;
import br.kelvynn.mytodobasic.faculdade.task.list.MateriaOrderFragment;
import br.kelvynn.mytodobasic.faculdade.task.list.PrioriedadeOrderFragment;
import br.kelvynn.mytodobasic.menu.MenuActivity;


public class FaculdadeMain extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private ImageButton menu;
    private ImageButton newTask;
    private Spinner spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculdade_main);
        FirebaseUser usuarioAtual = FirebaseAuth.getInstance().getCurrentUser();
        if (usuarioAtual != null) {

            inicializarComponentes();
            inicializarSpinnerList();

            newTask.setOnClickListener(view -> {
                Intent it = new Intent(FaculdadeMain.this, CriarTaskFacudadeActivity.class);
                startActivity(it);
            });

            menu.setOnClickListener(view -> {
                Intent it = new Intent(FaculdadeMain.this, MenuActivity.class);
                startActivity(it);
            });
        }

    }

    private void inicializarSpinnerList() {
        ArrayAdapter<CharSequence> adpter = ArrayAdapter.createFromResource(this, R.array.tipoListaFaculdade, R.layout.spinner_item);
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
            Intent it = new Intent(FaculdadeMain.this, MenuActivity.class);
            startActivity(it);

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (i == 0) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentPrincipal, new HojeTaskFaculdadeFragment()).commit();
        } else if (i == 1) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentPrincipal, new FaculdadeTaskFragment()).commit();
        } else if (i == 2) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentPrincipal, new MateriaOrderFragment()).commit();
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