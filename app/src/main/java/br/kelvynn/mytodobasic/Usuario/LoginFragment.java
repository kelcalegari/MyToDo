package br.kelvynn.mytodobasic.Usuario;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import br.kelvynn.mytodobasic.R;
import br.kelvynn.mytodobasic.task.TaskActivity;


@SuppressWarnings("ALL")
public class LoginFragment extends Fragment {
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private TextInputLayout textEmail, textSenha;


    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        inicializarComponentes(v);

        v.findViewById(R.id.criarLogin).setOnClickListener(view -> getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.loginFragment, new CadastroFragment()).commit());

        v.findViewById(R.id.buttonLogin).setOnClickListener(view -> {

            reiniciarCampos();

            boolean loginValido = ValidarCampos();

            if (loginValido) {

                String email = textEmail.getEditText().getText().toString();
                String senha = textSenha.getEditText().getText().toString();


                auth.signInWithEmailAndPassword(email, senha).addOnCompleteListener(getActivity(), task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getActivity(), "Cadastro realizado com sucesso!!", Toast.LENGTH_SHORT).show();
                        Log.d("LoginFragment/Login", "Login realizado");
                        IniciarActivit();
                    } else {
                        Log.d("LoginFragment/Login", " Falha Login" + task.getException().toString());
                        try {
                            throw task.getException();
                        } catch (FirebaseAuthWeakPasswordException e) {
                            textSenha.setError("Verificar senha, a senha tem que ser maior de 6 caracteres!!");
                            textSenha.requestFocus();
                        } catch (FirebaseAuthInvalidCredentialsException e) {
                            textSenha.setError("Verificar senha, senha incorreta!!");
                            textSenha.requestFocus();

                        } catch (FirebaseAuthInvalidUserException e) {
                            textEmail.setError("Não foi localizado a conta, verifique o E-mail!!");
                            textEmail.requestFocus();
                        } catch (Exception e) {
                            Toast.makeText(getActivity(), "Falha a cadastrar o usuario!!", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }

        });


        return v;
    }


    private boolean ValidarCampos() {
        boolean loginValido = true;
        if (textEmail.getEditText().getText().length() < 1) {
            textEmail.setError("Verificar Email!!");
            textEmail.requestFocus();
            loginValido = false;
        }

        if (textSenha.getEditText().getText().length() < 6) {
            textSenha.setError("Verificar senha!!");
            textSenha.requestFocus();
            loginValido = false;
        }
        return loginValido;
    }

    private void reiniciarCampos() {
        textEmail.setErrorEnabled(false);
        textEmail.requestFocus();
        textSenha.setErrorEnabled(false);
        textSenha.requestFocus();
    }

    private void inicializarComponentes(View v) {
        textEmail = v.findViewById(R.id.campoEmail);
        textSenha = v.findViewById(R.id.campoSenha);
    }


    private void IniciarActivit() {
        Toast.makeText(getActivity(), "Cadastro realizado com sucesso!!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), TaskActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}