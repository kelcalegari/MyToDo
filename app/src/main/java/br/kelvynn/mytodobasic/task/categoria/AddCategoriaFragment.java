package br.kelvynn.mytodobasic.task.categoria;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;

import br.kelvynn.mytodobasic.R;
import br.kelvynn.mytodobasic.dados.HelperFirebase;
import br.kelvynn.mytodobasic.dados.task.Categoria;


@SuppressWarnings("ALL")
public class AddCategoriaFragment extends Fragment {

    public AddCategoriaFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_categoria, container, false);
        TextInputLayout nome = v.findViewById(R.id.campoCategoria);
        v.findViewById(R.id.buttonSalvar).setOnClickListener(view -> {
            HelperFirebase helperFirebase = new HelperFirebase();

            if (nome.getEditText().getText().toString().equals("")) {
                nome.getEditText().setError("Verificar título!!");
                nome.getEditText().requestFocus();

            } else {
                Categoria categoria = new Categoria();
                categoria.setNome(nome.getEditText().getText().toString());
                helperFirebase.CreateCategoria(categoria);

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentCategoria, new ListCategoriaFragment()).commit();
            }

        });

        v.findViewById(R.id.buttonEditarLista).setOnClickListener(view -> getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentCategoria, new ListCategoriaFragment()).commit());

        return v;
    }


}
