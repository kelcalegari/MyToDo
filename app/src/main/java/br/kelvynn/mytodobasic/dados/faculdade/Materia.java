package br.kelvynn.mytodobasic.dados.faculdade;

import androidx.annotation.NonNull;

public class Materia {
    private String Id;
    private String nome;


    public Materia() {
    }

    public Materia(String nome) {
        this.nome = nome;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
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
        return "Materia{" +
                "Id='" + Id + '\'' +
                ", nome='" + nome + '\'' +
                '}';
    }
}
