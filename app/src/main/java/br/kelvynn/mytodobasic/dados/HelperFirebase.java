package br.kelvynn.mytodobasic.dados;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import br.kelvynn.mytodobasic.R;
import br.kelvynn.mytodobasic.basictask.AdapterBasicTask;
import br.kelvynn.mytodobasic.dados.basicTask.BasicTask;
import br.kelvynn.mytodobasic.dados.faculdade.FaculdadeTask;
import br.kelvynn.mytodobasic.dados.faculdade.Materia;
import br.kelvynn.mytodobasic.dados.mercado.ItemMercado;
import br.kelvynn.mytodobasic.dados.mercado.SelecaoAutomatico;
import br.kelvynn.mytodobasic.dados.mercado.TipoMercado;
import br.kelvynn.mytodobasic.dados.task.Categoria;
import br.kelvynn.mytodobasic.dados.task.SubTask;
import br.kelvynn.mytodobasic.dados.task.Task;
import br.kelvynn.mytodobasic.faculdade.materia.AdapterMateria;
import br.kelvynn.mytodobasic.faculdade.task.list.AdapterTaskFaculdade;
import br.kelvynn.mytodobasic.faculdade.task.subtask.AdapterSubTaskFaculdade;
import br.kelvynn.mytodobasic.mercado.AdapterMercado;
import br.kelvynn.mytodobasic.task.categoria.AdapterCategoria;
import br.kelvynn.mytodobasic.task.task.list.AdapterTask;
import br.kelvynn.mytodobasic.task.task.subtask.AdapterSubTask;

@SuppressWarnings("ALL")
public class HelperFirebase {
    private FirebaseFirestore db;
    private String usuarioId;
    private final String tabelaUsuarios = "Usuarios";
    private final String tabelaCategorias = "Categorias";
    private final String tabelaTasks = "Tasks";
    private final String tabelaSubTasks = "SubTasks";
    private final String tabelaFaculdadeTask = "Faculdade";
    private final String tabelaMateria = "Materia";
    private final String tabelaBasicTask = "BasicTask";
    private final String tabelaTipoMercado = "TipoMercado";
    private final String tabelaItemMercado = "ItemMercado";
    private final String tabelaSelecaoAutomatica = "TabelaSelecaoAutomatica";

    public HelperFirebase() {
        FirebaseUser usuarioAtual = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        if (usuarioAtual == null) {
            usuarioId ="";

        } else {
            usuarioId = usuarioAtual.getUid();
        }
    }

    //crud Usuario

    public void CreateUsuario(Usuario usuario) {
        usuario.setId(usuarioId);
        DocumentReference documentReference = db.collection(tabelaUsuarios).document(usuarioId);
        documentReference.set(usuario).addOnSuccessListener(unused -> {
            Log.d("CreateUsuario/db", " Sucesso ao gravar os dados");
            createCategoriasPadroes();
            createMateriasPadroes();
            createTipoMercados();

        }).addOnFailureListener(e -> Log.d("CreateUsuario/db", " Falha ao gravar os dados" + e));

    }


    public void GetByIdUsuario(TextInputLayout textNome, TextInputLayout textEmail) {
        DocumentReference documentReference = db.collection(tabelaUsuarios).document(usuarioId);
        documentReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Usuario usuario = document.toObject(Usuario.class);
                    textNome.getEditText().setText(usuario.getNomeCompleto());
                    textEmail.getEditText().setText(usuario.geteMail());

                    Log.d(TAG, "DocumentSnapshot data: " + document.getString("nome"));
                } else {
                    Log.d(TAG, "No such document");
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });

    }


    //Crud  Categoria
    public void CreateCategoria(Categoria categoria) {
        DocumentReference documentReference = db.collection(tabelaUsuarios).document(usuarioId).collection(tabelaCategorias).document();
        categoria.setId(documentReference.getId());
        documentReference.set(categoria).addOnSuccessListener(unused -> Log.d("CreateCategoria/db", " Sucesso ao gravar os dados")).addOnFailureListener(e -> Log.d("CreateCategoria/db", " Falha ao gravar os dados" + e));
    }

    public void updateCategoriaNome(String id, String nome) {

        DocumentReference documentReference = db.collection(tabelaUsuarios).document(usuarioId).collection(tabelaCategorias).document(id);
        documentReference.update("nome",nome).addOnSuccessListener(unused -> Log.d("updateCategoriaNome/db", " Sucesso ao gravar os dados")).addOnFailureListener(e -> Log.d("updateCategoriaNome/db", " Falha ao gravar os dados" + e));

    }

    public void deleteCategoria(String id) {

        DocumentReference documentReference = db.collection(tabelaUsuarios).document(usuarioId).collection(tabelaCategorias).document(id);
        documentReference.delete().addOnSuccessListener(unused -> Log.d("DeleteCategoriaFragment/db", " Sucesso ao deletar os dados")).addOnFailureListener(e -> Log.d("DeleteCategoriaFragment/db", " Falha ao deletar os dados" + e));


    }

    public void createCategoriasPadroes(){
        ArrayList<Categoria> CategoriaPadrao = new ArrayList<>();
        CategoriaPadrao.add(new Categoria("Categoria 1"));
        CategoriaPadrao.add(new Categoria("Categoria 2"));
        CategoriaPadrao.add(new Categoria("Categoria 3"));
        for (int i = 0; i < CategoriaPadrao.size(); i++) {
            CreateCategoria(CategoriaPadrao.get(i));
        }
    }


    public void getAllCategoria(Context context, RecyclerView recyclerView) {
        ArrayList<Categoria> categorias = new ArrayList<>();
        db.collection(tabelaUsuarios).document(usuarioId).collection(tabelaCategorias).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {

                            Categoria categoria = queryDocumentSnapshot.toObject(Categoria.class);
                            categorias.add(categoria);
                        }

                        AdapterCategoria adapterCategoria = new AdapterCategoria(context, categorias);
                        recyclerView.setAdapter(adapterCategoria);


                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }

                });
    }

    public void getAllNameCategoria(Context context, ArrayList<String> listCategoriaId, ArrayList<String> listCategoriaName, Spinner spinnerCategoria, Task taskAtual) {
        db.collection(tabelaUsuarios).document(usuarioId).collection(tabelaCategorias).orderBy("nome").get()
                .addOnCompleteListener(task -> {
                    listCategoriaId.clear();
                    listCategoriaName.clear();

                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                        Categoria categoria = documentSnapshot.toObject(Categoria.class);
                        listCategoriaId.add(categoria.getId());
                        listCategoriaName.add(categoria.getNome());
                    }
                    ArrayAdapter<String> adapterCategoria = new ArrayAdapter<>(context, R.layout.spinner_item_categoria, listCategoriaName);
                    adapterCategoria.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerCategoria.setAdapter(adapterCategoria);
                    if (listCategoriaId.contains(taskAtual.getIdCategoriaTask())) {
                        spinnerCategoria.setSelection(listCategoriaId.indexOf(taskAtual.getIdCategoriaTask()));
                    }


                }).addOnFailureListener(e -> Log.d("editarTask teste", "onFailure"));


    }

    //Crud task
    public String CreateIdTASK() {
        return db.collection(tabelaUsuarios).document(usuarioId).collection(tabelaTasks).document().getId();
    }


    public void createTask(Task task) {

        DocumentReference documentReference = db.collection(tabelaUsuarios).document(usuarioId).collection(tabelaTasks).document(task.getId());
        Map<String, Object> docData = new HashMap<>();
        docData.put("id", documentReference.getId());
        docData.put("idCategoriaTask", task.getIdCategoriaTask());
        docData.put("nomeCategoria", task.getNomeCategoria());
        docData.put("titulo", task.getTitulo());
        docData.put("descricao", task.getDescricao());
        docData.put("dataVencimento", new Timestamp(task.getDataVencimento().getTime()));
        docData.put("prioriedade", task.getPrioriedade());
        docData.put("atrasado", task.isAtrasado());
        docData.put("concluido", task.isConcluido());
        docData.put("dataConclusao", new Timestamp(task.getDataVencimento().getTime()));
        documentReference.set(docData).addOnSuccessListener(unused -> Log.d("CreateTASK/db", " Sucesso ao gravar os dados")).addOnFailureListener(e -> Log.d("CreateTASK/db", " Falha ao gravar os dados" + e));


    }

    public void updateTASK(Task task) {

        DocumentReference documentReference = db.collection(tabelaUsuarios).document(usuarioId).collection(tabelaTasks).document(task.getId());
        Map<String, Object> docData = new HashMap<>();
        docData.put("id", documentReference.getId());
        docData.put("idCategoriaTask", task.getIdCategoriaTask());
        docData.put("nomeCategoria", task.getNomeCategoria());
        docData.put("titulo", task.getTitulo());
        docData.put("descricao", task.getDescricao());

        docData.put("dataVencimento", new Timestamp(task.getDataVencimento().getTime()));
        docData.put("dataConclusao", new Timestamp(task.getDataVencimento().getTime()));
        docData.put("prioriedade", task.getPrioriedade());
        docData.put("atrasado", task.isAtrasado());
        docData.put("concluido", task.isConcluido());
        documentReference.set(docData).addOnSuccessListener(unused -> Log.d("CreateTASK/db", " Sucesso ao gravar os dados")).addOnFailureListener(e -> Log.d("CreateTASK/db", " Falha ao gravar os dados" + e));

    }

    public void updateTaskConcluido(Task task) {

        DocumentReference documentReference = db.collection(tabelaUsuarios).document(usuarioId).collection(tabelaTasks).document(task.getId());

        documentReference.update("concluido",task.isConcluido()).addOnSuccessListener(unused -> Log.d("AddCategoriaFragment/db", " Sucesso ao gravar os dados")).addOnFailureListener(e -> Log.d("AddCategoriaFragment/db", " Falha ao gravar os dados" + e));
    }

    public void deleteTask(String id) {

        delateAllSubTask(id);
        DocumentReference documentReference = db.collection(tabelaUsuarios).document(usuarioId).collection(tabelaTasks).document(id);
        documentReference.delete().addOnSuccessListener(unused -> Log.d("deleteTask/db teste ", " Sucesso ao deletar os dados")).addOnFailureListener(e -> Log.d("deleteTask/db teste ", " Falha ao deletar os dados" + e));


    }


    public void getAllTasksHoje(Context context, RecyclerView recyclerView) {
        ArrayList<Task> tasks = new ArrayList<>();
        Calendar hoje = Calendar.getInstance();
        Timestamp hojeTime = new Timestamp(hoje.getTime());

        db.collection(tabelaUsuarios).document(usuarioId).collection(tabelaTasks).whereLessThan("dataVencimento", hojeTime).whereEqualTo("concluido", false).orderBy("dataVencimento").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {

                            Task taskAtual = new Task();
                            taskAtual.setId(queryDocumentSnapshot.getString("id"));
                            taskAtual.setIdCategoriaTask(queryDocumentSnapshot.getString("idCategoriaTask"));
                            taskAtual.setNomeCategoria(queryDocumentSnapshot.getString("nomeCategoria"));
                            taskAtual.setTitulo(queryDocumentSnapshot.getString("titulo"));
                            taskAtual.setDescricao(queryDocumentSnapshot.getString("descricao"));
                            taskAtual.setConcluido(queryDocumentSnapshot.getBoolean("concluido"));
                            Calendar calendarTemp = Calendar.getInstance();
                            calendarTemp.setTime(queryDocumentSnapshot.getTimestamp("dataVencimento").toDate());
                            taskAtual.setDataVencimento(calendarTemp);

                            taskAtual.setPrioriedade(Math.toIntExact(queryDocumentSnapshot.getLong("prioriedade")));
                            Calendar ontem = hoje;
                            ontem.add(Calendar.DAY_OF_MONTH, -1);
                            taskAtual.setAtrasado(ontem.after(taskAtual.getDataVencimento()));
                            tasks.add(taskAtual);

                        }

                        AdapterTask adapterTask = new AdapterTask(context, tasks);
                        recyclerView.setAdapter(adapterTask);


                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }

                });
    }

    public void getAllTasks(Context context, RecyclerView recyclerView) {
        ArrayList<Task> tasks = new ArrayList<>();


        db.collection(tabelaUsuarios).document(usuarioId).collection(tabelaTasks).whereEqualTo("concluido", false).orderBy("dataVencimento").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {

                            Task taskAtual = new Task();
                            taskAtual.setId(queryDocumentSnapshot.getString("id"));
                            taskAtual.setIdCategoriaTask(queryDocumentSnapshot.getString("idCategoriaTask"));
                            taskAtual.setNomeCategoria(queryDocumentSnapshot.getString("nomeCategoria"));
                            taskAtual.setTitulo(queryDocumentSnapshot.getString("titulo"));
                            taskAtual.setDescricao(queryDocumentSnapshot.getString("descricao"));
                            taskAtual.setConcluido(queryDocumentSnapshot.getBoolean("concluido"));
                            Calendar calendarTemp = Calendar.getInstance();
                            calendarTemp.setTime(queryDocumentSnapshot.getTimestamp("dataVencimento").toDate());
                            taskAtual.setDataVencimento(calendarTemp);
                            taskAtual.setPrioriedade(Math.toIntExact(queryDocumentSnapshot.getLong("prioriedade")));
                            Calendar ontem = Calendar.getInstance();
                            ontem.add(Calendar.DAY_OF_MONTH, -1);
                            taskAtual.setAtrasado(ontem.after(taskAtual.getDataVencimento()));
                            tasks.add(taskAtual);
                        }
                        AdapterTask adapterTask = new AdapterTask(context, tasks);
                        recyclerView.setAdapter(adapterTask);


                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }

                });
    }

    public void getAllTasksOrderByCategoria(Context context, RecyclerView recyclerView) {
        ArrayList<Task> tasks = new ArrayList<>();


        db.collection(tabelaUsuarios).document(usuarioId).collection(tabelaTasks).whereEqualTo("concluido", false).orderBy("nomeCategoria").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {

                            Task taskAtual = new Task();
                            taskAtual.setId(queryDocumentSnapshot.getString("id"));
                            taskAtual.setIdCategoriaTask(queryDocumentSnapshot.getString("idCategoriaTask"));
                            taskAtual.setNomeCategoria(queryDocumentSnapshot.getString("nomeCategoria"));
                            taskAtual.setTitulo(queryDocumentSnapshot.getString("titulo"));
                            taskAtual.setDescricao(queryDocumentSnapshot.getString("descricao"));
                            taskAtual.setConcluido(queryDocumentSnapshot.getBoolean("concluido"));
                            Calendar calendarTemp = Calendar.getInstance();
                            calendarTemp.setTime(queryDocumentSnapshot.getTimestamp("dataVencimento").toDate());
                            taskAtual.setDataVencimento(calendarTemp);
                            taskAtual.setPrioriedade(Math.toIntExact(queryDocumentSnapshot.getLong("prioriedade")));
                            Calendar ontem = Calendar.getInstance();
                            ontem.add(Calendar.DAY_OF_MONTH, -1);
                            taskAtual.setAtrasado(ontem.after(taskAtual.getDataVencimento()));
                            tasks.add(taskAtual);

                        }

                        AdapterTask adapterTask = new AdapterTask(context, tasks);
                        recyclerView.setAdapter(adapterTask);


                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }

                });

    }

    public void getAllTasksOrderByPrioriedade(Context context, RecyclerView recyclerView) {
        ArrayList<Task> tasks = new ArrayList<>();


        db.collection(tabelaUsuarios).document(usuarioId).collection(tabelaTasks).whereEqualTo("concluido", false).orderBy("prioriedade", Query.Direction.DESCENDING).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {

                            Task taskAtual = new Task();
                            taskAtual.setId(queryDocumentSnapshot.getString("id"));
                            taskAtual.setIdCategoriaTask(queryDocumentSnapshot.getString("idCategoriaTask"));
                            taskAtual.setNomeCategoria(queryDocumentSnapshot.getString("nomeCategoria"));
                            taskAtual.setTitulo(queryDocumentSnapshot.getString("titulo"));
                            taskAtual.setDescricao(queryDocumentSnapshot.getString("descricao"));
                            taskAtual.setConcluido(queryDocumentSnapshot.getBoolean("concluido"));
                            Calendar calendarTemp = Calendar.getInstance();
                            calendarTemp.setTime(queryDocumentSnapshot.getTimestamp("dataVencimento").toDate());
                            taskAtual.setDataVencimento(calendarTemp);

                            taskAtual.setPrioriedade(Math.toIntExact(queryDocumentSnapshot.getLong("prioriedade")));
                            Calendar ontem = Calendar.getInstance();
                            ontem.add(Calendar.DAY_OF_MONTH, -1);
                            taskAtual.setAtrasado(ontem.after(taskAtual.getDataVencimento()));

                            tasks.add(taskAtual);

                        }

                        AdapterTask adapterTask = new AdapterTask(context, tasks);
                        recyclerView.setAdapter(adapterTask);


                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }

                });
    }

    public void getAllTasksConcluido(Context context, RecyclerView recyclerView) {
        ArrayList<Task> tasks = new ArrayList<>();


        db.collection(tabelaUsuarios).document(usuarioId).collection(tabelaTasks).whereEqualTo("concluido", true).orderBy("dataVencimento").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {

                            Task taskAtual = new Task();
                            taskAtual.setId(queryDocumentSnapshot.getString("id"));
                            taskAtual.setIdCategoriaTask(queryDocumentSnapshot.getString("idCategoriaTask"));
                            taskAtual.setNomeCategoria(queryDocumentSnapshot.getString("nomeCategoria"));
                            taskAtual.setTitulo(queryDocumentSnapshot.getString("titulo"));
                            taskAtual.setDescricao(queryDocumentSnapshot.getString("descricao"));
                            taskAtual.setConcluido(queryDocumentSnapshot.getBoolean("concluido"));
                            Calendar calendarTemp = Calendar.getInstance();
                            calendarTemp.setTime(queryDocumentSnapshot.getTimestamp("dataVencimento").toDate());
                            taskAtual.setDataVencimento(calendarTemp);

                            calendarTemp.setTime(queryDocumentSnapshot.getTimestamp("dataConclusao").toDate());
                            taskAtual.setDataConclusao(calendarTemp);

                            taskAtual.setPrioriedade(Math.toIntExact(queryDocumentSnapshot.getLong("prioriedade")));
                            taskAtual.setAtrasado(false);

                            tasks.add(taskAtual);

                        }

                        AdapterTask adapterTask = new AdapterTask(context, tasks);
                        recyclerView.setAdapter(adapterTask);


                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }

                });
    }

    //Crud Subtask
    public void createSubTask(SubTask subTask, String idTask) {
        DocumentReference documentReference = db.collection(tabelaUsuarios).document(usuarioId).collection(tabelaTasks).document(idTask).collection(tabelaSubTasks).document();
        subTask.setId(documentReference.getId());
        Calendar data = Calendar.getInstance();
        subTask.setData(new Timestamp(data.getTime()));
        documentReference.set(subTask).addOnSuccessListener(unused -> Log.d("CreateSubTask/db", " Sucesso ao gravar os dados")).addOnFailureListener(e -> Log.d("CreateSubTask/db", " Falha ao gravar os dados" + e));
    }

    public void updateSubTaskNome(SubTask subTask, String idTask) {

        DocumentReference documentReference = db.collection(tabelaUsuarios).document(usuarioId).collection(tabelaTasks).document(idTask).collection(tabelaSubTasks).document(subTask.getId());
        documentReference.update("nome",subTask.getNome()).addOnSuccessListener(unused -> Log.d("updateSubTaskNome/db", " Sucesso ao gravar os dados")).addOnFailureListener(e -> Log.d("updateSubTaskNome/db", " Falha ao gravar os dados" + e));

    }

    public void updateSubTaskSetConcluido(SubTask subTask, String idTask) {

        DocumentReference documentReference = db.collection(tabelaUsuarios).document(usuarioId).collection(tabelaTasks).document(idTask).collection(tabelaSubTasks).document(subTask.getId());
        documentReference.update("concluido",subTask.getConcluido()).addOnSuccessListener(unused -> Log.d("updateSubTaskSetConcluido/db", " Sucesso ao gravar os dados")).addOnFailureListener(e -> Log.d("updateSubTaskSetConcluido/db", " Falha ao gravar os dados" + e));

    }

    public void delateSubTask(SubTask subTask, String idTask) {

        DocumentReference documentReference = db.collection(tabelaUsuarios).document(usuarioId).collection(tabelaTasks).document(idTask).collection(tabelaSubTasks).document(subTask.getId());
        documentReference.delete().addOnSuccessListener(unused -> Log.d("DelateSubTask/db", " Sucesso ao deletar os dados")).addOnFailureListener(e -> Log.d("DelateSubTask/db", " Falha ao deletar os dados" + e));
    }

    public void delateAllSubTask(String id) {
        db.collection(tabelaUsuarios).document(usuarioId).collection(tabelaTasks).document(id).collection(tabelaSubTasks).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {

                            SubTask subTask = queryDocumentSnapshot.toObject(SubTask.class);

                            delateSubTask(subTask, id);

                        }

                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }

                });
    }

    public void getAllSubTask(Context context, RecyclerView recyclerView, String idTask) {
        ArrayList<SubTask> subTasks = new ArrayList<>();
        db.collection(tabelaUsuarios).document(usuarioId).collection(tabelaTasks).document(idTask).collection(tabelaSubTasks).orderBy("data").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {

                            SubTask subTask = queryDocumentSnapshot.toObject(SubTask.class);
                            subTasks.add(subTask);

                        }

                        AdapterSubTask adapterSubTask = new AdapterSubTask(context, subTasks, idTask,2);
                        recyclerView.setAdapter(adapterSubTask);


                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }

                });
    }

    public void getConcluidoSubTask(Context context, RecyclerView recyclerView, String idTask) {
        ArrayList<SubTask> subTasks = new ArrayList<>();
        db.collection(tabelaUsuarios).document(usuarioId).collection(tabelaTasks).document(idTask).collection(tabelaSubTasks).whereEqualTo("concluido",true).orderBy("data").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {

                            SubTask subTask = queryDocumentSnapshot.toObject(SubTask.class);

                            subTasks.add(subTask);

                        }

                        AdapterSubTask adapterSubTask = new AdapterSubTask(context, subTasks, idTask,1);
                        recyclerView.setAdapter(adapterSubTask);


                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }

                });
    }

    public void getNotConcluidoSubTask(Context context, RecyclerView recyclerView, String idTask) {
        ArrayList<SubTask> subTasks = new ArrayList<>();
        db.collection(tabelaUsuarios).document(usuarioId).collection(tabelaTasks).document(idTask).collection(tabelaSubTasks).whereEqualTo("concluido",false).orderBy("data").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {

                            SubTask subTask = queryDocumentSnapshot.toObject(SubTask.class);

                            subTasks.add(subTask);

                        }

                        AdapterSubTask adapterSubTask = new AdapterSubTask(context, subTasks, idTask,0);
                        recyclerView.setAdapter(adapterSubTask);


                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }

                });
    }

    /*------------Crud Faculdade-----------*/

    //Crud Materia
    public void CreateMateria(Materia materia) {
        DocumentReference documentReference = db.collection(tabelaUsuarios).document(usuarioId).collection(tabelaMateria).document();
        materia.setId(documentReference.getId());
        documentReference.set(materia).addOnSuccessListener(unused -> Log.d("CreateMateria/db", " Sucesso ao gravar os dados")).addOnFailureListener(e -> Log.d("CreateMateria/db", " Falha ao gravar os dados" + e));
    }

    public void createMateriasPadroes(){
        ArrayList<Materia> materiasPadrao = new ArrayList<>();
        materiasPadrao.add(new Materia("MateriaExemplo 1"));
        materiasPadrao.add(new Materia("MateriaExemplo 2"));
        materiasPadrao.add(new Materia("MateriaExemplo 3"));
        for (int i = 0; i < materiasPadrao.size(); i++) {
            CreateMateria(materiasPadrao.get(i));
        }
    }


    public void updateMateriaNome(String id, String nome) {

        DocumentReference documentReference = db.collection(tabelaUsuarios).document(usuarioId).collection(tabelaMateria).document(id);
        documentReference.update("nome",nome).addOnSuccessListener(unused -> Log.d("updateMateriaNome/db", " Sucesso ao gravar os dados")).addOnFailureListener(e -> Log.d("updateMateriaNome/db", " Falha ao gravar os dados" + e));

    }



    public void deleteMateria(String id) {

        DocumentReference documentReference = db.collection(tabelaUsuarios).document(usuarioId).collection(tabelaMateria).document(id);
        documentReference.delete().addOnSuccessListener(unused -> Log.d("deleteMateria/db", " Sucesso ao deletar os dados")).addOnFailureListener(e -> Log.d("deleteMateria/db", " Falha ao deletar os dados" + e));


    }

    public void getAllNameMateria(Context context, ArrayList<String> listMateriaId, ArrayList<String> listMateriaName, Spinner spinnerMateria, FaculdadeTask taskAtual) {
        db.collection(tabelaUsuarios).document(usuarioId).collection(tabelaMateria).orderBy("nome").get()
                .addOnCompleteListener(task -> {
                    listMateriaId.clear();
                    listMateriaName.clear();

                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                        Categoria categoria = documentSnapshot.toObject(Categoria.class);
                        listMateriaId.add(categoria.getId());
                        listMateriaName.add(categoria.getNome());
                    }
                    ArrayAdapter<String> adapterCategoria = new ArrayAdapter<>(context, R.layout.spinner_item_categoria, listMateriaName);
                    adapterCategoria.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerMateria.setAdapter(adapterCategoria);
                    if (listMateriaId.contains(taskAtual.getIdMateria())) {
                        spinnerMateria.setSelection(listMateriaId.indexOf(taskAtual.getIdMateria()));
                    }


                }).addOnFailureListener(e -> Log.d("getAllNameMateria teste", "onFailure"));


    }

    public void getAllMateria(Context context, RecyclerView recyclerView) {
        ArrayList<Materia> materias = new ArrayList<>();
        db.collection(tabelaUsuarios).document(usuarioId).collection(tabelaMateria).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                            Materia materia = queryDocumentSnapshot.toObject(Materia.class);
                            materias.add(materia);
                        }

                        AdapterMateria adapterCategoria = new AdapterMateria(context, materias);
                        recyclerView.setAdapter(adapterCategoria);


                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }

                });
    }

    //Crud FaculdadeTask

    public String createIdFaculdadeTask() {
        return db.collection(tabelaUsuarios).document(usuarioId).collection(tabelaFaculdadeTask).document().getId();
    }
    public void createFaculdadeTask(FaculdadeTask faculdadetask) {

        DocumentReference documentReference = db.collection(tabelaUsuarios).document(usuarioId).collection(tabelaFaculdadeTask).document(faculdadetask.getId());
        Map<String, Object> docData = new HashMap<>();
        docData.put("id", documentReference.getId());
        docData.put("idMateria", faculdadetask.getIdMateria());
        docData.put("nomeMateria", faculdadetask.getNomeMateria());
        docData.put("titulo", faculdadetask.getTitulo());
        docData.put("descricao", faculdadetask.getDescricao());
        docData.put("dataVencimento", new Timestamp(faculdadetask.getDataVencimento().getTime()));
        docData.put("prioriedade", faculdadetask.getPrioriedade());
        docData.put("atrasado", faculdadetask.isAtrasado());
        docData.put("concluido", faculdadetask.isConcluido());
        docData.put("dataConclusao", new Timestamp(faculdadetask.getDataVencimento().getTime()));


        documentReference.set(docData).addOnSuccessListener(unused -> Log.d("createFaculdadeTask/db", " Sucesso ao gravar os dados")).addOnFailureListener(e -> Log.d("createFaculdadeTask/db", " Falha ao gravar os dados" + e));


    }

    public void updateFaculdadeTASK(FaculdadeTask faculdadetask) {
        DocumentReference documentReference = db.collection(tabelaUsuarios).document(usuarioId).collection(tabelaFaculdadeTask).document(faculdadetask.getId());
        Map<String, Object> docData = new HashMap<>();
        docData.put("id", documentReference.getId());
        docData.put("idMateria", faculdadetask.getIdMateria());
        docData.put("nomeMateria", faculdadetask.getNomeMateria());
        docData.put("titulo", faculdadetask.getTitulo());
        docData.put("descricao", faculdadetask.getDescricao());
        docData.put("dataVencimento", new Timestamp(faculdadetask.getDataVencimento().getTime()));

        docData.put("prioriedade", faculdadetask.getPrioriedade());
        docData.put("atrasado", faculdadetask.isAtrasado());
        docData.put("concluido", faculdadetask.isConcluido());
        docData.put("dataConclusao", new Timestamp(faculdadetask.getDataVencimento().getTime()));
        documentReference.set(docData).addOnSuccessListener(unused -> Log.d("updateFaculdadeTASK/db", " Sucesso ao gravar os dados")).addOnFailureListener(e -> Log.d("updateFaculdadeTASK/db", " Falha ao gravar os dados" + e));

    }

    public void updateFaculdadeTaskConcluido(FaculdadeTask faculdadetask) {

        DocumentReference documentReference = db.collection(tabelaUsuarios).document(usuarioId).collection(tabelaFaculdadeTask).document(faculdadetask.getId());

        documentReference.update("concluido",faculdadetask.isConcluido()).addOnSuccessListener(unused -> Log.d("updateFaculdadeTaskConcluido/db", " Sucesso ao gravar os dados")).addOnFailureListener(e -> Log.d("updateFaculdadeTaskConcluido/db", " Falha ao gravar os dados" + e));
    }

    public void deleteFaculdadeTask(FaculdadeTask faculdadetask) {


        delateAllSubTaskFaculdade(faculdadetask.getId());
        DocumentReference documentReference = db.collection(tabelaUsuarios).document(usuarioId).collection(tabelaFaculdadeTask).document(faculdadetask.getId());
        documentReference.delete().addOnSuccessListener(unused -> Log.d("deleteTask/db teste ", " Sucesso ao deletar os dados")).addOnFailureListener(e -> Log.d("deleteTask/db teste ", " Falha ao deletar os dados" + e));


    }


    public void getAllFaculdadeTasksHoje(Context context, RecyclerView recyclerView) {
        ArrayList<FaculdadeTask> tasks = new ArrayList<>();
        Calendar hoje = Calendar.getInstance();
        Timestamp hojeTime = new Timestamp(hoje.getTime());

        db.collection(tabelaUsuarios).document(usuarioId).collection(tabelaFaculdadeTask).whereLessThan("dataVencimento", hojeTime).whereEqualTo("concluido", false).orderBy("dataVencimento").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {

                            FaculdadeTask faculdadeTask = new FaculdadeTask();
                            faculdadeTask.setId(queryDocumentSnapshot.getString("id"));
                            faculdadeTask.setIdMateria(queryDocumentSnapshot.getString("idMateria"));
                            faculdadeTask.setNomeMateria(queryDocumentSnapshot.getString("nomeMateria"));
                            faculdadeTask.setTitulo(queryDocumentSnapshot.getString("titulo"));
                            faculdadeTask.setDescricao(queryDocumentSnapshot.getString("descricao"));
                            faculdadeTask.setConcluido(queryDocumentSnapshot.getBoolean("concluido"));
                            Calendar calendarTemp = Calendar.getInstance();
                            calendarTemp.setTime(queryDocumentSnapshot.getTimestamp("dataVencimento").toDate());
                            faculdadeTask.setDataVencimento(calendarTemp);
                            faculdadeTask.setPrioriedade(Math.toIntExact(queryDocumentSnapshot.getLong("prioriedade")));
                            Calendar ontem = hoje;
                            ontem.add(Calendar.DAY_OF_MONTH, -1);
                            faculdadeTask.setAtrasado(ontem.after(faculdadeTask.getDataVencimento()));
                            tasks.add(faculdadeTask);

                        }
                        AdapterTaskFaculdade adapterTask = new AdapterTaskFaculdade(context, tasks);
                        recyclerView.setAdapter(adapterTask);


                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }

                });
    }

    public void getAllFaculdadeTasks(Context context, RecyclerView recyclerView) {
        ArrayList<FaculdadeTask> tasks = new ArrayList<>();


        db.collection(tabelaUsuarios).document(usuarioId).collection(tabelaFaculdadeTask).whereEqualTo("concluido", false).orderBy("dataVencimento").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {

                            FaculdadeTask faculdadeTask = new FaculdadeTask();
                            faculdadeTask.setId(queryDocumentSnapshot.getString("id"));
                            faculdadeTask.setIdMateria(queryDocumentSnapshot.getString("idMateria"));
                            faculdadeTask.setNomeMateria(queryDocumentSnapshot.getString("nomeMateria"));
                            faculdadeTask.setTitulo(queryDocumentSnapshot.getString("titulo"));
                            faculdadeTask.setDescricao(queryDocumentSnapshot.getString("descricao"));
                            faculdadeTask.setConcluido(queryDocumentSnapshot.getBoolean("concluido"));
                            Calendar calendarTemp = Calendar.getInstance();
                            calendarTemp.setTime(queryDocumentSnapshot.getTimestamp("dataVencimento").toDate());
                            faculdadeTask.setDataVencimento(calendarTemp);
                            faculdadeTask.setPrioriedade(Math.toIntExact(queryDocumentSnapshot.getLong("prioriedade")));
                            Calendar ontem = Calendar.getInstance();
                            ontem.add(Calendar.DAY_OF_MONTH, -1);
                            faculdadeTask.setAtrasado(ontem.after(faculdadeTask.getDataVencimento()));
                            tasks.add(faculdadeTask);

                        }
                        AdapterTaskFaculdade adapterTask = new AdapterTaskFaculdade(context, tasks);
                        recyclerView.setAdapter(adapterTask);

                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }

                });
    }

    public void getAllFaculdadeTasksOrderByMateria(Context context, RecyclerView recyclerView) {
        ArrayList<FaculdadeTask> tasks = new ArrayList<>();


        db.collection(tabelaUsuarios).document(usuarioId).collection(tabelaFaculdadeTask).whereEqualTo("concluido", false).orderBy("nomeMateria").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {

                            FaculdadeTask faculdadeTask = new FaculdadeTask();
                            faculdadeTask.setId(queryDocumentSnapshot.getString("id"));
                            faculdadeTask.setIdMateria(queryDocumentSnapshot.getString("idMateria"));
                            faculdadeTask.setNomeMateria(queryDocumentSnapshot.getString("nomeMateria"));
                            faculdadeTask.setTitulo(queryDocumentSnapshot.getString("titulo"));
                            faculdadeTask.setDescricao(queryDocumentSnapshot.getString("descricao"));
                            faculdadeTask.setConcluido(queryDocumentSnapshot.getBoolean("concluido"));
                            Calendar calendarTemp = Calendar.getInstance();
                            calendarTemp.setTime(queryDocumentSnapshot.getTimestamp("dataVencimento").toDate());
                            faculdadeTask.setDataVencimento(calendarTemp);
                            faculdadeTask.setPrioriedade(Math.toIntExact(queryDocumentSnapshot.getLong("prioriedade")));
                            Calendar ontem = Calendar.getInstance();
                            ontem.add(Calendar.DAY_OF_MONTH, -1);
                            faculdadeTask.setAtrasado(ontem.after(faculdadeTask.getDataVencimento()));
                            tasks.add(faculdadeTask);

                        }
                        AdapterTaskFaculdade adapterTask = new AdapterTaskFaculdade(context, tasks);
                        recyclerView.setAdapter(adapterTask);
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }

                });

    }

    public void getAllFaculdadeTasksOrderByPrioriedade(Context context, RecyclerView recyclerView) {
        ArrayList<FaculdadeTask> tasks = new ArrayList<>();


        db.collection(tabelaUsuarios).document(usuarioId).collection(tabelaFaculdadeTask).whereEqualTo("concluido", false).orderBy("prioriedade", Query.Direction.DESCENDING).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {

                            FaculdadeTask faculdadeTask = new FaculdadeTask();
                            faculdadeTask.setId(queryDocumentSnapshot.getString("id"));
                            faculdadeTask.setIdMateria(queryDocumentSnapshot.getString("idMateria"));
                            faculdadeTask.setNomeMateria(queryDocumentSnapshot.getString("nomeMateria"));
                            faculdadeTask.setTitulo(queryDocumentSnapshot.getString("titulo"));
                            faculdadeTask.setDescricao(queryDocumentSnapshot.getString("descricao"));
                            faculdadeTask.setConcluido(queryDocumentSnapshot.getBoolean("concluido"));
                            Calendar calendarTemp = Calendar.getInstance();
                            calendarTemp.setTime(queryDocumentSnapshot.getTimestamp("dataVencimento").toDate());
                            faculdadeTask.setDataVencimento(calendarTemp);

                            faculdadeTask.setPrioriedade(Math.toIntExact(queryDocumentSnapshot.getLong("prioriedade")));
                            Calendar ontem = Calendar.getInstance();
                            ontem.add(Calendar.DAY_OF_MONTH, -1);
                            faculdadeTask.setAtrasado(ontem.after(faculdadeTask.getDataVencimento()));

                            tasks.add(faculdadeTask);

                        }
                        AdapterTaskFaculdade adapterTask = new AdapterTaskFaculdade(context, tasks);
                        recyclerView.setAdapter(adapterTask);
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }

                });
    }

    public void getAllFaculdadeTasksConcluido(Context context, RecyclerView recyclerView) {
        ArrayList<FaculdadeTask> tasks = new ArrayList<>();


        db.collection(tabelaUsuarios).document(usuarioId).collection(tabelaFaculdadeTask).whereEqualTo("concluido", true).orderBy("dataVencimento").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {

                            FaculdadeTask faculdadeTask = new FaculdadeTask();
                            faculdadeTask.setId(queryDocumentSnapshot.getString("id"));
                            faculdadeTask.setIdMateria(queryDocumentSnapshot.getString("idMateria"));
                            faculdadeTask.setNomeMateria(queryDocumentSnapshot.getString("nomeMateria"));
                            faculdadeTask.setTitulo(queryDocumentSnapshot.getString("titulo"));
                            faculdadeTask.setDescricao(queryDocumentSnapshot.getString("descricao"));
                            faculdadeTask.setConcluido(queryDocumentSnapshot.getBoolean("concluido"));
                            Calendar calendarTemp = Calendar.getInstance();
                            calendarTemp.setTime(queryDocumentSnapshot.getTimestamp("dataVencimento").toDate());
                            faculdadeTask.setDataVencimento(calendarTemp);
                            calendarTemp.setTime(queryDocumentSnapshot.getTimestamp("dataConclusao").toDate());
                            faculdadeTask.setDataConclusao(calendarTemp);
                            faculdadeTask.setPrioriedade(Math.toIntExact(queryDocumentSnapshot.getLong("prioriedade")));
                            Calendar ontem = Calendar.getInstance();
                            ontem.add(Calendar.DAY_OF_MONTH, -1);
                            faculdadeTask.setAtrasado(false);
                            tasks.add(faculdadeTask);

                        }
                        AdapterTaskFaculdade adapterTask = new AdapterTaskFaculdade(context, tasks);
                        recyclerView.setAdapter(adapterTask);
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }

                });
    }


    //Crud SubtaskFaculdade
    public void createSubTaskFaculdade(SubTask subTask, String idFaculdadeTask) {
        DocumentReference documentReference = db.collection(tabelaUsuarios).document(usuarioId).collection(tabelaFaculdadeTask).document(idFaculdadeTask).collection(tabelaSubTasks).document();
        subTask.setId(documentReference.getId());
        Calendar data = Calendar.getInstance();
        subTask.setData(new Timestamp(data.getTime()));
        documentReference.set(subTask).addOnSuccessListener(unused -> Log.d("createSubTaskFaculdade/db", " Sucesso ao gravar os dados")).addOnFailureListener(e -> Log.d("createSubTaskFaculdade/db", " Falha ao gravar os dados" + e));
    }

    public void updateSubTaskFaculdadeNome(SubTask subTask, String idFaculdadeTask) {

        DocumentReference documentReference = db.collection(tabelaUsuarios).document(usuarioId).collection(tabelaFaculdadeTask).document(idFaculdadeTask).collection(tabelaSubTasks).document(subTask.getId());
        documentReference.update("nome",subTask.getNome()).addOnSuccessListener(unused -> Log.d("updateSubTaskFaculdadeNome/db", " Sucesso ao gravar os dados")).addOnFailureListener(e -> Log.d("updateSubTaskFaculdadeNome/db", " Falha ao gravar os dados" + e));

    }

    public void updateSubTaskFaculdadeSetConcluido(SubTask subTask, String idFaculdadeTask) {

        DocumentReference documentReference = db.collection(tabelaUsuarios).document(usuarioId).collection(tabelaFaculdadeTask).document(idFaculdadeTask).collection(tabelaSubTasks).document(subTask.getId());
        documentReference.update("concluido",subTask.getConcluido()).addOnSuccessListener(unused -> Log.d("updateSubTaskFaculdadeSetConcluido/db", " Sucesso ao gravar os dados")).addOnFailureListener(e -> Log.d("updateSubTaskFaculdadeSetConcluido/db", " Falha ao gravar os dados" + e));

    }

    public void delateSubTaskFaculdade(SubTask subTask, String idFaculdadeTask) {

        DocumentReference documentReference = db.collection(tabelaUsuarios).document(usuarioId).collection(tabelaFaculdadeTask).document(idFaculdadeTask).collection(tabelaSubTasks).document(subTask.getId());
        documentReference.delete().addOnSuccessListener(unused -> Log.d("DelateSubTask/db", " Sucesso ao deletar os dados")).addOnFailureListener(e -> Log.d("DelateSubTask/db", " Falha ao deletar os dados" + e));
    }

    public void delateAllSubTaskFaculdade(String id) {
        db.collection(tabelaUsuarios).document(usuarioId).collection(tabelaFaculdadeTask).document(id).collection(tabelaSubTasks).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {

                            SubTask subTask = queryDocumentSnapshot.toObject(SubTask.class);
                            delateSubTaskFaculdade(subTask, id);

                        }

                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }

                });
    }

    public void getAllSubTaskFaculdade(Context context, RecyclerView recyclerView, String idFaculdadeTask) {
        ArrayList<SubTask> subTasks = new ArrayList<>();
        db.collection(tabelaUsuarios).document(usuarioId).collection(tabelaFaculdadeTask).document(idFaculdadeTask).collection(tabelaSubTasks).orderBy("data").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {

                            SubTask subTask = queryDocumentSnapshot.toObject(SubTask.class);
                            subTasks.add(subTask);

                        }
                        AdapterSubTaskFaculdade adapterSubTask = new AdapterSubTaskFaculdade(context, subTasks, idFaculdadeTask,2);
                        recyclerView.setAdapter(adapterSubTask);


                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }

                });
    }

    public void getConcluidoSubTaskFaculdade(Context context, RecyclerView recyclerView, String idFaculdadeTask) {
        ArrayList<SubTask> subTasks = new ArrayList<>();
        db.collection(tabelaUsuarios).document(usuarioId).collection(tabelaFaculdadeTask).document(idFaculdadeTask).collection(tabelaSubTasks).whereEqualTo("concluido",true).orderBy("data").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {

                            SubTask subTask = queryDocumentSnapshot.toObject(SubTask.class);
                            subTasks.add(subTask);

                        }
                        AdapterSubTaskFaculdade adapterSubTask = new AdapterSubTaskFaculdade(context, subTasks, idFaculdadeTask,1);
                        recyclerView.setAdapter(adapterSubTask);


                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }

                });
    }

    public void getNotConcluidoSubTaskFaculdade(Context context, RecyclerView recyclerView, String idFaculdadeTask) {
        ArrayList<SubTask> subTasks = new ArrayList<>();
        db.collection(tabelaUsuarios).document(usuarioId).collection(tabelaFaculdadeTask).document(idFaculdadeTask).collection(tabelaSubTasks).whereEqualTo("concluido",false).orderBy("data").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {

                            SubTask subTask = queryDocumentSnapshot.toObject(SubTask.class);
                            subTasks.add(subTask);

                        }
                        AdapterSubTaskFaculdade adapterSubTask = new AdapterSubTaskFaculdade(context, subTasks, idFaculdadeTask,1);
                        recyclerView.setAdapter(adapterSubTask);


                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }

                });
    }

    /*------------ BasicTask-----------*/

    //Crud SubtaskFaculdade
    public void createBasicTask(BasicTask basicTask) {
        DocumentReference documentReference = db.collection(tabelaUsuarios).document(usuarioId).collection(tabelaBasicTask).document();
        basicTask.setId(documentReference.getId());
        Calendar data = Calendar.getInstance();
        basicTask.setData(new Timestamp(data.getTime()));
        documentReference.set(basicTask).addOnSuccessListener(unused -> Log.d("createSubTaskFaculdade/db", " Sucesso ao gravar os dados")).addOnFailureListener(e -> Log.d("createSubTaskFaculdade/db", " Falha ao gravar os dados" + e));
    }

    public void updateBasicTaskNome(BasicTask basicTask) {

        DocumentReference documentReference = db.collection(tabelaUsuarios).document(usuarioId).collection(tabelaBasicTask).document(basicTask.getId());
        documentReference.update("nome",basicTask.getNome()).addOnSuccessListener(unused -> Log.d("updateSubTaskFaculdadeNome/db", " Sucesso ao gravar os dados")).addOnFailureListener(e -> Log.d("updateSubTaskFaculdadeNome/db", " Falha ao gravar os dados" + e));

    }

    public void updateBasicTaskSetConcluido(BasicTask basicTask) {

        DocumentReference documentReference = db.collection(tabelaUsuarios).document(usuarioId).collection(tabelaBasicTask).document(basicTask.getId());
        documentReference.update("concluido",basicTask.getConcluido()).addOnSuccessListener(unused -> Log.d("updateSubTaskFaculdadeSetConcluido/db", " Sucesso ao gravar os dados")).addOnFailureListener(e -> Log.d("updateSubTaskFaculdadeSetConcluido/db", " Falha ao gravar os dados" + e));

    }

    public void delateBasicTask(BasicTask basicTask) {
        DocumentReference documentReference = db.collection(tabelaUsuarios).document(usuarioId).collection(tabelaBasicTask).document(basicTask.getId());
        documentReference.delete().addOnSuccessListener(unused -> Log.d("DelateSubTask/db", " Sucesso ao deletar os dados")).addOnFailureListener(e -> Log.d("DelateSubTask/db", " Falha ao deletar os dados" + e));
    }

    public void getAllBasicTask(Context context, RecyclerView recyclerView) {
        ArrayList<BasicTask> basicTasks = new ArrayList<>();
        db.collection(tabelaUsuarios).document(usuarioId).collection(tabelaBasicTask).orderBy("data").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {

                            BasicTask basicTask = queryDocumentSnapshot.toObject(BasicTask.class);
                            basicTasks.add(basicTask);

                        }

                        AdapterBasicTask adapterSubTask = new AdapterBasicTask(context, basicTasks,2);
                        recyclerView.setAdapter(adapterSubTask);


                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }

                });
    }

    public void getConcluidoBasicTask(Context context, RecyclerView recyclerView) {
        ArrayList<BasicTask> basicTasks = new ArrayList<>();
        db.collection(tabelaUsuarios).document(usuarioId).collection(tabelaBasicTask).whereEqualTo("concluido",true).orderBy("data").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {

                            BasicTask basicTask = queryDocumentSnapshot.toObject(BasicTask.class);
                            basicTasks.add(basicTask);

                        }
                        AdapterBasicTask adapterSubTask = new AdapterBasicTask(context, basicTasks,1);
                        recyclerView.setAdapter(adapterSubTask);


                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }

                });
    }

    public void getNotConcluidoBasicTask(Context context, RecyclerView recyclerView) {
        ArrayList<BasicTask> basicTasks = new ArrayList<>();
        db.collection(tabelaUsuarios).document(usuarioId).collection(tabelaBasicTask).whereEqualTo("concluido",false).orderBy("data").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {

                            BasicTask basicTask = queryDocumentSnapshot.toObject(BasicTask.class);
                            basicTasks.add(basicTask);

                        }
                        AdapterBasicTask adapterSubTask = new AdapterBasicTask(context, basicTasks,1);
                        recyclerView.setAdapter(adapterSubTask);


                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }

                });
    }


    /*------------ Mercado-----------*/

    //Crud TipoMercado
    public void createTipoMercado(TipoMercado tipoMercado) {
        DocumentReference documentReference = db.collection(tabelaUsuarios).document(usuarioId).collection(tabelaTipoMercado).document(tipoMercado.getTitulo());


        documentReference.set(tipoMercado).addOnSuccessListener(unused -> Log.d("createTipoMercado/db", " Sucesso ao gravar os dados")).addOnFailureListener(e -> Log.d("createTipoMercado/db", " Falha ao gravar os dados" + e));
    }

    public void createTipoMercados(){
        ArrayList<TipoMercado> tipoMercados = new ArrayList<>();
        tipoMercados.add(new TipoMercado("Lanche"));
        tipoMercados.add(new TipoMercado("Doces"));
        tipoMercados.add(new TipoMercado("Temperos"));
        tipoMercados.add(new TipoMercado("Laticínios"));
        tipoMercados.add(new TipoMercado("Queijos"));
        tipoMercados.add(new TipoMercado("Congelados"));
        tipoMercados.add(new TipoMercado("Peixes e frutos do mar"));
        tipoMercados.add(new TipoMercado("Açougue"));
        tipoMercados.add(new TipoMercado("Diversos"));
        tipoMercados.add(new TipoMercado("Padaria"));
        tipoMercados.add(new TipoMercado("Frutas"));
        tipoMercados.add(new TipoMercado("Verdura"));
        for (int i = 0; i < tipoMercados.size(); i++) {
            createTipoMercado(tipoMercados.get(i));
        }
    }








    public void getAllNameTipoMercado(Context context, ArrayList<String> listTipoMercadoName, Spinner spinner) {
        db.collection(tabelaUsuarios).document(usuarioId).collection(tabelaTipoMercado).orderBy("titulo").get()
                .addOnCompleteListener(task -> {

                    listTipoMercadoName.clear();
                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                        TipoMercado tipoMercado = documentSnapshot.toObject(TipoMercado.class);
                        listTipoMercadoName.add(tipoMercado.getTitulo());
                    }
                    ArrayAdapter<String> adapterTipoMateria = new ArrayAdapter<>(context, R.layout.spinner_item_categoria, listTipoMercadoName);
                    adapterTipoMateria.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapterTipoMateria);



                }).addOnFailureListener(e -> Log.d("getAllNameMateria teste", "onFailure"));


    }

    //Crud ItemMercado
    public void createItemMercado(ItemMercado itemMercado) {
        DocumentReference documentReference = db.collection(tabelaUsuarios).document(usuarioId).collection(tabelaItemMercado).document(itemMercado.getNome());
        documentReference.set(itemMercado).addOnSuccessListener(unused -> Log.d("createItemMercado/db", " Sucesso ao gravar os dados")).addOnFailureListener(e -> Log.d("createItemMercado/db", " Falha ao gravar os dados" + e));
    }

    public void updateItemMercado(ItemMercado itemMercado) {

        DocumentReference documentReference = db.collection(tabelaUsuarios).document(usuarioId).collection(tabelaItemMercado).document(itemMercado.getNome());
        documentReference.set(itemMercado).addOnSuccessListener(unused -> Log.d("updateItemMercado/db", " Sucesso ao gravar os dados")).addOnFailureListener(e -> Log.d("updateItemMercado/db", " Falha ao gravar os dados" + e));

    }

    public void updateCompradoItemMercado(ItemMercado itemMercado) {

        DocumentReference documentReference = db.collection(tabelaUsuarios).document(usuarioId).collection(tabelaItemMercado).document(itemMercado.getNome());
        documentReference.update("comprado",itemMercado.isComprado()).addOnSuccessListener(unused -> Log.d("updateCompradoItemMercado/db", " Sucesso ao gravar os dados")).addOnFailureListener(e -> Log.d("updateCompradoItemMercado/db", " Falha ao gravar os dados" + e));

    }



    public void delateItemMercado(ItemMercado itemMercado) {
        DocumentReference documentReference = db.collection(tabelaUsuarios).document(usuarioId).collection(tabelaItemMercado).document(itemMercado.getNome());
        documentReference.delete().addOnSuccessListener(unused -> Log.d("delateItemMercado/db", " Sucesso ao deletar os dados")).addOnFailureListener(e -> Log.d("delateItemMercado/db", " Falha ao deletar os dados" + e));
    }

    public void delateAllItemMercado() {
        db.collection(tabelaUsuarios).document(usuarioId).collection(tabelaItemMercado).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {

                            ItemMercado itemMercado = queryDocumentSnapshot.toObject(ItemMercado.class);

                            delateItemMercado(itemMercado);

                        }

                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }

                });
    }

    public void delateAllItemMercadoComprado(Context context, RecyclerView recyclerView) {
        db.collection(tabelaUsuarios).document(usuarioId).collection(tabelaItemMercado).whereEqualTo("comprado",true).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {

                            ItemMercado itemMercado = queryDocumentSnapshot.toObject(ItemMercado.class);
                            delateItemMercado(itemMercado);
                        }
                        getAllItemMercado(context,recyclerView);


                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }

                });
    }

    public void getAllItemMercado(Context context, RecyclerView recyclerView) {
        ArrayList<ItemMercado> itemMercados = new ArrayList<>();
        db.collection(tabelaUsuarios).document(usuarioId).collection(tabelaItemMercado).orderBy("nomeCategoria").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {

                            ItemMercado itemMercado = queryDocumentSnapshot.toObject(ItemMercado.class);
                            itemMercados.add(itemMercado);

                        }

                        AdapterMercado adapterMercado = new AdapterMercado(context, itemMercados);
                        recyclerView.setAdapter(adapterMercado);


                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }

                });
    }

    //Crud SelecaoAutomatico
    public void createSelecaoAutomatico(SelecaoAutomatico selecaoAutomatico) {
        DocumentReference documentReference = db.collection(tabelaUsuarios).document(usuarioId).collection(tabelaSelecaoAutomatica).document(selecaoAutomatico.getNomeItem().toLowerCase(Locale.ROOT));


        documentReference.set(selecaoAutomatico).addOnSuccessListener(unused -> Log.d("createSelecaoAutomatico/db", " Sucesso ao gravar os dados")).addOnFailureListener(e -> Log.d("createSelecaoAutomatico/db", " Falha ao gravar os dados" + e));
    }

    public void getAllSelecaoAutomatica(Context context,ArrayList<String> nomeItem, ArrayList<String> categoriaItem, AutoCompleteTextView textNome) {

        db.collection(tabelaUsuarios).document(usuarioId).collection(tabelaSelecaoAutomatica).orderBy("nomeCategoria").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                            nomeItem.add(queryDocumentSnapshot.getString("nomeItem"));
                            categoriaItem.add(queryDocumentSnapshot.getString("nomeCategoria"));

                        }
                        ArrayAdapter<String> adapterNomes = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, nomeItem);
                        textNome.setAdapter(adapterNomes);


                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }

                });
    }



}