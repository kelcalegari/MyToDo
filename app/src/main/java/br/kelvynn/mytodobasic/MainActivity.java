package br.kelvynn.mytodobasic;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import br.kelvynn.mytodobasic.Usuario.LoginActivity;
import br.kelvynn.mytodobasic.basictask.BasicTaskActivity;
import br.kelvynn.mytodobasic.faculdade.FaculdadeMain;
import br.kelvynn.mytodobasic.menu.MenuActivity;
import br.kelvynn.mytodobasic.mercado.MercadoActivity;
import br.kelvynn.mytodobasic.task.TaskActivity;

public class MainActivity extends AppCompatActivity {

    private RelativeLayout faculdade;
    private RelativeLayout task;
    private RelativeLayout basicTask;
    private RelativeLayout mercado;
    private ImageButton menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        inicializarComponentes();

        menu.setOnClickListener(view -> {
            Intent it = new Intent(MainActivity.this, MenuActivity.class);
            startActivity(it);
        });

        faculdade.setOnClickListener(view -> {
            Intent it = new Intent(MainActivity.this, FaculdadeMain.class);
            startActivity(it);
        });

        task.setOnClickListener(view -> {
            Intent it = new Intent(MainActivity.this, TaskActivity.class);
            it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(it);
        });
        basicTask.setOnClickListener(view -> {
            Intent it = new Intent(MainActivity.this, BasicTaskActivity.class);
            startActivity(it);
        });

        mercado.setOnClickListener(view -> {
            Intent it = new Intent(MainActivity.this, MercadoActivity.class);
            startActivity(it);
        });
    }

    private void inicializarComponentes() {
        faculdade = findViewById(R.id.faculdade);
        task = findViewById(R.id.task);
        basicTask = findViewById(R.id.basicTask);
        mercado = findViewById(R.id.mercado);
        menu = findViewById(R.id.menu);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser usuarioAtual = FirebaseAuth.getInstance().getCurrentUser();
        if (usuarioAtual == null) {
            Intent it = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(it);
        }
    }
}