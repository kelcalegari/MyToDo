package br.kelvynn.mytodobasic.dados.task;


import androidx.annotation.NonNull;

public class Categoria {
    private String id;
    private String nome;

    public Categoria(String nome) {
        this.nome = nome;
    }

    public Categoria(String id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public Categoria() {
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


    @NonNull
    @Override
    public String toString() {
        return "Categoria{" +
                "id='" + id + '\'' +
                ", nome='" + nome + '\'' +
                '}';
    }
}
