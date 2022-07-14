package br.kelvynn.mytodobasic.dados.task;

import androidx.annotation.NonNull;

import com.google.firebase.Timestamp;

@SuppressWarnings("unused")
public class SubTask {
    private String id;
    private String nome;
    private Boolean concluido;
    private Timestamp data;

    public SubTask() {
    }

    public SubTask(String id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public SubTask(String nome, Boolean concluido) {
        this.nome = nome;
        this.concluido = concluido;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Boolean getConcluido() {
        return concluido;
    }

    public void setConcluido(Boolean concluido) {
        this.concluido = concluido;
    }

    public Timestamp getData() {
        return data;
    }

    public void setData(Timestamp data) {
        this.data = data;
    }

    @NonNull
    @Override
    public String toString() {
        return "SubTask{" +
                "id='" + id + '\'' +
                ", nome='" + nome + '\'' +
                ", concluido=" + concluido +
                '}';
    }
}
