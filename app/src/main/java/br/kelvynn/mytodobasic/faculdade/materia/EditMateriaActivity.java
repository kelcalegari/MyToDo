package br.kelvynn.mytodobasic.faculdade.materia;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import br.kelvynn.mytodobasic.R;
import br.kelvynn.mytodobasic.dados.HelperFirebase;

@SuppressWarnings("ALL")
public class EditMateriaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_materia);
        TextInputLayout titulo = findViewById(R.id.campoTitulo);
        Bundle extras = getIntent().getExtras();
        String id = extras.getString("id");
        titulo.getEditText().setText(extras.getString("nome"));
        Button salvar = findViewById(R.id.buttonSalvar);
        Button excluir = findViewById(R.id.buttonExcluir);
        ImageButton voltar = findViewById(R.id.imageVoltar);


        salvar.setOnClickListener(view -> {
            HelperFirebase helperFirebase = new HelperFirebase();
            helperFirebase.updateMateriaNome(id, titulo.getEditText().getText().toString());
            finish();
        });

        voltar.setOnClickListener(view -> finish());
        excluir.setOnClickListener(view -> {
            HelperFirebase helperFirebase = new HelperFirebase();
            helperFirebase.deleteMateria(id);
            finish();
        });


    }
}