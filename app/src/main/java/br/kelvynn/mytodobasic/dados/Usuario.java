package br.kelvynn.mytodobasic.dados;


import androidx.annotation.NonNull;

public class Usuario {
    private String id;
    private String nomeCompleto;
    private String eMail;

    public Usuario() {
    }

    public Usuario(String id, String nomeCompleto, String eMail) {
        this.id = id;
        this.nomeCompleto = nomeCompleto;
        this.eMail = eMail;
    }

    @SuppressWarnings("unused")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }


    @NonNull
    @Override
    public String toString() {
        return "Usuario{" +
                "id='" + id + '\'' +
                ", nomeCompleto='" + nomeCompleto + '\'' +
                ", eMail='" + eMail + '\'' +
                '}';
    }
}
