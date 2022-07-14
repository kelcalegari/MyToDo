package br.kelvynn.mytodobasic.faculdade.materia;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;

import br.kelvynn.mytodobasic.R;
import br.kelvynn.mytodobasic.dados.HelperFirebase;
import br.kelvynn.mytodobasic.dados.faculdade.Materia;


@SuppressWarnings("ALL")
public class AddMateriaFragment extends Fragment {

    public AddMateriaFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_materia, container, false);
        TextInputLayout nome = v.findViewById(R.id.campoMateria);
        v.findViewById(R.id.buttonSalvar).setOnClickListener(view -> {
            HelperFirebase helperFirebase = new HelperFirebase();

            if (nome.getEditText().getText().toString().equals("")) {
                nome.getEditText().setError("Verificar título!!");
                nome.getEditText().requestFocus();

            } else {
                Materia materia = new Materia();
                materia.setNome(nome.getEditText().getText().toString());
                helperFirebase.CreateMateria(materia);

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentMateria, new ListMateriaFragment()).commit();
            }

        });

        v.findViewById(R.id.buttonEditarLista).setOnClickListener(view -> getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentMateria, new ListMateriaFragment()).commit());

        return v;
    }
}