package br.kelvynn.mytodobasic.dados.basicTask;

import androidx.annotation.NonNull;

import com.google.firebase.Timestamp;

@SuppressWarnings("unused")
public class BasicTask {
    private String id;
    private String nome;
    private Boolean concluido;
    private Timestamp data;

    public BasicTask() {
    }

    public BasicTask(String id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public BasicTask(String nome, Boolean concluido) {
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

    public Timestamp getData() {
        return data;
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

    public void setData(Timestamp data) {
        this.data = data;
    }

    @NonNull
    @Override
    public String toString() {
        return "BasicTask{" +
                "id='" + id + '\'' +
                ", nome='" + nome + '\'' +
                ", concluido=" + concluido +
                '}';
    }
}


