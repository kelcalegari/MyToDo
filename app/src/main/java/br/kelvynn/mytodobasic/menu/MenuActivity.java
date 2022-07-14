package br.kelvynn.mytodobasic.menu;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import br.kelvynn.mytodobasic.MainActivity;
import br.kelvynn.mytodobasic.R;
import br.kelvynn.mytodobasic.basictask.BasicTaskActivity;
import br.kelvynn.mytodobasic.faculdade.FaculdadeMain;
import br.kelvynn.mytodobasic.mercado.MercadoActivity;
import br.kelvynn.mytodobasic.task.TaskActivity;


public class MenuActivity extends AppCompatActivity {
    private ImageButton buttonVoltar;
    private RelativeLayout configuracao;
    private RelativeLayout faculdade;
    private RelativeLayout task;
    private RelativeLayout basicTask;
    private RelativeLayout mercado;
    private RelativeLayout home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        inicializarComponentes();
        buttonVoltar.setOnClickListener(view -> finish());

        configuracao.setOnClickListener(view -> {
            Intent it = new Intent(MenuActivity.this, ConfiguracaoActivity.class);
            startActivity(it);
        });

        faculdade.setOnClickListener(view -> {
            Intent it = new Intent(MenuActivity.this, FaculdadeMain.class);
            startActivity(it);
        });

        task.setOnClickListener(view -> {
            Intent it = new Intent(MenuActivity.this, TaskActivity.class);
            it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(it);
        });
        basicTask.setOnClickListener(view -> {
            Intent it = new Intent(MenuActivity.this, BasicTaskActivity.class);
            startActivity(it);
        });

        mercado.setOnClickListener(view -> {
            Intent it = new Intent(MenuActivity.this, MercadoActivity.class);
            startActivity(it);
        });

        home.setOnClickListener(view -> {
            Intent it = new Intent(MenuActivity.this, MainActivity.class);
            startActivity(it);
        });


    }

    private void inicializarComponentes() {
        buttonVoltar = findViewById(R.id.imageVoltar);
        home = findViewById(R.id.home);
        configuracao = findViewById(R.id.configuracao);
        faculdade = findViewById(R.id.faculdade);
        task = findViewById(R.id.task);
        basicTask = findViewById(R.id.basicTask);
        mercado = findViewById(R.id.mercado);
    }
}