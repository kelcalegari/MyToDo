package br.kelvynn.mytodobasic.task.categoria;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import br.kelvynn.mytodobasic.R;

public class CategoriaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_categoria);
        ImageButton fechar = findViewById(R.id.closeButton);

        fechar.setOnClickListener(view -> finish());
    }
}