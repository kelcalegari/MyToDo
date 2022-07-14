package br.kelvynn.mytodobasic.Usuario;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import br.kelvynn.mytodobasic.R;
import br.kelvynn.mytodobasic.dados.HelperFirebase;
import br.kelvynn.mytodobasic.dados.Usuario;
import br.kelvynn.mytodobasic.task.TaskActivity;

@SuppressWarnings("ALL")
public class CadastroFragment extends Fragment {
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private TextInputLayout textNome, textEmail, textSenha, textNSenha;

    private Usuario usuario;

    public CadastroFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        usuario = new Usuario();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layoutTask for this fragment
        View v = inflater.inflate(R.layout.fragment_cadastro, container, false);

        IniciarComponentes(v);


        v.findViewById(R.id.Login).setOnClickListener(view -> getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.loginFragment, new LoginFragment()).commit());

        v.findViewById(R.id.buttonCriarConta).setOnClickListener(view -> {
            ReiniciarCampos();


            boolean cadastroValido = ValidarDados();

            if (cadastroValido) {
                String email = textEmail.getEditText().getText().toString();
                String senha = textSenha.getEditText().getText().toString();
                usuario.seteMail(email);
                usuario.setNomeCompleto(textNome.getEditText().getText().toString());


                auth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(getActivity(), this::criarUsuario);

            }

        });


        return v;
    }

    private void criarUsuario(@NonNull Task<AuthResult> task) {
        if (task.isSuccessful()) {
            HelperFirebase helperFirebase = new HelperFirebase();
            helperFirebase.CreateUsuario(usuario);


            IniciarActivit();


        } else {

            try {
                throw task.getException();
            } catch (FirebaseAuthWeakPasswordException e) {
                textSenha.setError("Verificar senha, a senha tem que ser maior de 6 caracteres!!");
                textSenha.requestFocus();

            } catch (FirebaseAuthUserCollisionException e) {
                textEmail.setError("Essa conta já está cadastrado!!");
                textEmail.requestFocus();
            } catch (FirebaseAuthInvalidCredentialsException e) {
                textEmail.setError("O e-mail não é válido!!");
                textEmail.requestFocus();
            } catch (Exception e) {
                Toast.makeText(getActivity(), "Falha a cadastrar o usuario!!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void ReiniciarCampos() {
        textEmail.setErrorEnabled(false);
        textEmail.requestFocus();
        textSenha.setErrorEnabled(false);
        textSenha.requestFocus();
        textNome.setErrorEnabled(false);
        textNome.requestFocus();
        textNSenha.setErrorEnabled(false);
        textNSenha.requestFocus();
    }

    private boolean ValidarDados() {
        boolean cadastroValido = true;

        if (textNome.getEditText().getText().length() < 1) {
            textNome.setError("Verificar Email!!");
            textNome.requestFocus();
            cadastroValido = false;
        }

        if (textEmail.getEditText().getText().length() < 1) {
            textEmail.setError("Verificar Email!!");
            textEmail.requestFocus();
            cadastroValido = false;
        }

        if (textSenha.getEditText().getText().length() < 6) {
            textSenha.setError("Verificar senha, a senha tem que ser maior de 6 caracteres!!");
            textSenha.requestFocus();
            cadastroValido = false;
        } else if (!textSenha.getEditText().getText().toString().equals(textNSenha.getEditText().getText().toString())) {
            textNSenha.setError("Verificar senha, não confere!!");
            textNSenha.requestFocus();
            cadastroValido = false;
            textSenha.setError("Verificar senha, não confere!!");
            textSenha.requestFocus();
        }
        return cadastroValido;
    }


    private void IniciarComponentes(View v) {
        textNome = v.findViewById(R.id.campoNome);
        textEmail = v.findViewById(R.id.campoEmail);
        textSenha = v.findViewById(R.id.campoSenha);
        textNSenha = v.findViewById(R.id.campoNSenha);
    }

    private void IniciarActivit() {
        Toast.makeText(getActivity(), "Cadastro realizado com sucesso!!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), TaskActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}