package br.kelvynn.mytodobasic.faculdade.materia;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import br.kelvynn.mytodobasic.R;

public class MateriaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_materia);
        ImageButton fechar = findViewById(R.id.closeButton);

        fechar.setOnClickListener(view -> finish());
    }
}