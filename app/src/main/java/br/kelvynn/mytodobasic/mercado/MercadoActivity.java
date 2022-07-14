package br.kelvynn.mytodobasic.mercado;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import br.kelvynn.mytodobasic.R;
import br.kelvynn.mytodobasic.dados.HelperFirebase;
import br.kelvynn.mytodobasic.dados.mercado.ItemMercado;
import br.kelvynn.mytodobasic.dados.mercado.SelecaoAutomatico;
import br.kelvynn.mytodobasic.menu.MenuActivity;

public class MercadoActivity extends AppCompatActivity {
    private Spinner spinnerCategoria;
    private ImageButton menu, newTask, diminuir, aumentar, delete, deletarconcluidos;
    private View view;
    private RelativeLayout layoutNewTask;
    private AutoCompleteTextView campoNome;
    private EditText quantidade;
    private Button salvar;
    private HelperFirebase helperFirebase;
    private RecyclerView recyclerView;
    private ArrayList<String> nomesTipoMercado, nomeAutomatico, tipoAutomatico;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mercado);
        inicializarComponentes();
        inicializarListAutomatica();
        inicializarSpinnerList();
        inicializarRecyclerView();

        menu.setOnClickListener(view -> {
            Intent it = new Intent(MercadoActivity.this, MenuActivity.class);
            startActivity(it);
        });

        newTask.setOnClickListener(view -> mudarContext(true, View.VISIBLE));

        view.setOnLongClickListener(view -> {
            mudarContext(false, View.GONE);
            esconderTeclado();
            return false;
        });


        salvar.setOnClickListener(view -> {
            if (campoNome.getText().toString().equals("")) {
                campoNome.setError("Verificar Nome!!");
                campoNome.requestFocus();
            } else {
                esconderTeclado();

                String nome = campoNome.getText().toString();
                String categoria = (String) spinnerCategoria.getSelectedItem();
                int quantidad = Integer.parseInt(quantidade.getText().toString());


                helperFirebase.createItemMercado(new ItemMercado(nome, categoria, quantidad, false));
                helperFirebase.createSelecaoAutomatico(new SelecaoAutomatico(nome, categoria));
                mudarContext(false, View.GONE);
                inicializarRecyclerView();

                int posicao = nomeAutomatico.indexOf(nome);
                if (posicao > 0) {
                    nomeAutomatico.remove(posicao);
                    tipoAutomatico.remove(posicao);
                }
                nomeAutomatico.add(nome);
                tipoAutomatico.add(categoria);
            }
        });


        aumentar.setOnClickListener(view -> {
            int quantidad = Integer.parseInt(quantidade.getText().toString());
            if (quantidad < 99) {
                quantidad++;
            }
            quantidade.setText(String.valueOf(quantidad));
        });


        diminuir.setOnClickListener(view -> {
            int quantidad = Integer.parseInt(quantidade.getText().toString());
            if (quantidad > 1) {
                quantidad--;
            }
            quantidade.setText(String.valueOf(quantidad));
        });


        delete.setOnClickListener(view -> {
            helperFirebase.delateAllItemMercado();
            ArrayList<ItemMercado> itemMercados = new ArrayList<>();
            AdapterMercado adapterMercado = new AdapterMercado(MercadoActivity.this, itemMercados);
            recyclerView.setAdapter(adapterMercado);
        });

        deletarconcluidos.setOnClickListener(view -> helperFirebase.delateAllItemMercadoComprado(MercadoActivity.this, recyclerView));

        campoNome.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (nomeAutomatico.contains(editable.toString())) {
                    spinnerCategoria.setSelection(nomesTipoMercado.indexOf(tipoAutomatico.get(nomeAutomatico.indexOf(editable.toString()))));
                }
            }
        });


    }

    private void inicializarListAutomatica() {
        helperFirebase.getAllSelecaoAutomatica(this, nomeAutomatico, tipoAutomatico, campoNome);
    }

    private void inicializarRecyclerView() {
        helperFirebase.getAllItemMercado(this, recyclerView);
    }

    private void mudarContext(boolean b, int visibilidade) {
        layoutNewTask.setEnabled(b);
        campoNome.setText("");
        quantidade.setText("1");
        layoutNewTask.setVisibility(visibilidade);
    }

    private void esconderTeclado() {
        InputMethodManager imm = (InputMethodManager) getBaseContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(campoNome.getWindowToken(), 0);
    }

    private void inicializarComponentes() {
        menu = findViewById(R.id.menu);
        newTask = findViewById(R.id.imageAddTask);

        helperFirebase = new HelperFirebase();
        nomesTipoMercado = new ArrayList<>();
        view = findViewById(R.id.viewSair);
        layoutNewTask = findViewById(R.id.layoutAddBasicTask);
        campoNome = findViewById(R.id.campoNomeItemMercado);
        salvar = findViewById(R.id.buttonSalvar);
        quantidade = findViewById(R.id.editQuantidade);
        spinnerCategoria = findViewById(R.id.spinnerCategoria);
        diminuir = findViewById(R.id.menos);
        aumentar = findViewById(R.id.mais);
        recyclerView = findViewById(R.id.recyclerviewMercado);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        delete = findViewById(R.id.delete);
        deletarconcluidos = findViewById(R.id.deleteConcluidos);
        nomeAutomatico = new ArrayList<>();
        tipoAutomatico = new ArrayList<>();

    }

    private void inicializarSpinnerList() {
        helperFirebase.getAllNameTipoMercado(this, nomesTipoMercado, spinnerCategoria);


    }


}