package br.kelvynn.mytodobasic.faculdade.task.list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import br.kelvynn.mytodobasic.R;
import br.kelvynn.mytodobasic.dados.HelperFirebase;


public class MateriaOrderFragment extends Fragment {
    private RecyclerView recyclerView;

    public MateriaOrderFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_materia_order, container, false);
        recyclerView = v.findViewById(R.id.tarefasOrderMateria);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return v;
    }

    @Override
    public void onResume() {
        onRefresh();
        super.onResume();

    }


    public void onRefresh() {
        HelperFirebase helperFirebase = new HelperFirebase();
        helperFirebase.getAllFaculdadeTasksOrderByMateria(getContext(), recyclerView);
    }
}