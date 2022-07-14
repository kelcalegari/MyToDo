package br.kelvynn.mytodobasic.menu;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import br.kelvynn.mytodobasic.R;
import br.kelvynn.mytodobasic.Usuario.LoginActivity;


public class ConfiguracaoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracao);
        ImageButton buttonVoltar = findViewById(R.id.imageVoltar);
        RelativeLayout login = findViewById(R.id.login);
        buttonVoltar.setOnClickListener(view -> finish());

        login.setOnClickListener(view -> {
            Intent it = new Intent(ConfiguracaoActivity.this, LoginActivity.class);
            startActivity(it);
        });
    }
}