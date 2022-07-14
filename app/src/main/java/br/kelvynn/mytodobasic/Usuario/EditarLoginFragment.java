package br.kelvynn.mytodobasic.Usuario;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import br.kelvynn.mytodobasic.R;
import br.kelvynn.mytodobasic.dados.HelperFirebase;


@SuppressWarnings("ALL")
public class EditarLoginFragment extends Fragment {
    private String email;
    private boolean cadastroValido = true;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseUser user = auth.getCurrentUser();


    public EditarLoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layoutTask for this fragment
        View v = inflater.inflate(R.layout.fragment_editar_login, container, false);
        TextInputLayout textNome = v.findViewById(R.id.campoNome);
        TextInputLayout textEmail = v.findViewById(R.id.campoEmail);




        if (user != null) {
            this.email = user.getEmail();
        }

        HelperFirebase helperFirebase = new HelperFirebase();
        helperFirebase.GetByIdUsuario(textNome,textEmail);



        v.findViewById(R.id.deslogar).setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();

            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.loginFragment, new LoginFragment()).commit();
        });
        v.findViewById(R.id.mudarSenha).setOnClickListener(view -> {

            auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d("EDITAR LOGIN", "Enviado email");
                        }
                    });
            Toast.makeText(getContext(),"Verifique o seu e-mail!!",Toast.LENGTH_SHORT).show();
            FirebaseAuth.getInstance().signOut();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.loginFragment, new LoginFragment()).commit();
        });



        v.findViewById(R.id.buttonAtualizarConta).setOnClickListener(view -> {



            cadastroValido = true;

            if (textEmail.getEditText().getText().length() < 1) {
                textEmail.setError("Verificar Email!!");
                textEmail.requestFocus();
                cadastroValido = false;
            } else {
                textEmail.setErrorEnabled(false);
                textEmail.requestFocus();
            }

            if (textNome.getEditText().getText().length() < 1) {
                textNome.setError("Verificar Nome!!");
                textNome.requestFocus();
                cadastroValido = false;
            } else {
                textNome.setErrorEnabled(false);
                textNome.requestFocus();
            }


        });


        return v;
    }


}