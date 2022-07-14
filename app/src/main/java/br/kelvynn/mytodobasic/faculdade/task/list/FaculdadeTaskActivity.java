package br.kelvynn.mytodobasic.faculdade.task.list;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.List;
import java.util.Objects;

import br.kelvynn.mytodobasic.R;
import br.kelvynn.mytodobasic.dados.faculdade.FaculdadeTask;
import br.kelvynn.mytodobasic.faculdade.task.VisualizarTaskFaculdadeFragment;

public class FaculdadeTaskActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculdade_task);
        FaculdadeTask faculdadeTask = (FaculdadeTask) getIntent().getSerializableExtra("task");
        ImageButton voltar = findViewById(R.id.imageVoltar);
        VisualizarTaskFaculdadeFragment visualizarTask = new VisualizarTaskFaculdadeFragment();
        Bundle extra2 = new Bundle();
        extra2.putSerializable("task", faculdadeTask);
        visualizarTask.setArguments(extra2);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragmentTaskPrincipal, visualizarTask, "visualizarTask").commit();

        voltar.setOnClickListener(view -> {
            List<Fragment> fragments = fragmentManager.getFragments();
            Fragment visibleFragment = fragments.get(fragments.size() - 1);
            if (Objects.equals(visibleFragment.getTag(), "visualizarTask")) {
                finish();
            } else {
                fragmentManager.beginTransaction().replace(R.id.fragmentTaskPrincipal, visualizarTask, "visualizarTask").commit();
            }


        });
    }
}