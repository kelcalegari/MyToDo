package br.kelvynn.mytodobasic.Usuario;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import br.kelvynn.mytodobasic.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ImageButton buttonVoltar = findViewById(R.id.imageVoltar);
        buttonVoltar.setOnClickListener(view -> finish());

        FirebaseUser usuarioAtual = FirebaseAuth.getInstance().getCurrentUser();
        if (usuarioAtual != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.loginFragment, new EditarLoginFragment()).commit();
        }
    }
}