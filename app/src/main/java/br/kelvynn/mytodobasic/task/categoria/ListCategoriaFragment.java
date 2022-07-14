package br.kelvynn.mytodobasic.task.categoria;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import br.kelvynn.mytodobasic.R;
import br.kelvynn.mytodobasic.dados.HelperFirebase;


public class ListCategoriaFragment extends Fragment {
    private RecyclerView recyclerView;


    public ListCategoriaFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list_categoria, container, false);
        recyclerView = v.findViewById(R.id.recyclerviewCategoria);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        onRefresh();


        return v;
    }

    @Override
    public void onResume() {
        onRefresh();
        super.onResume();
    }


    public void onRefresh() {

        HelperFirebase helperFirebase = new HelperFirebase();

        helperFirebase.getAllCategoria(getContext(), recyclerView);

    }
}