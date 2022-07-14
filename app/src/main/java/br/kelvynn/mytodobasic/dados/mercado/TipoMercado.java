package br.kelvynn.mytodobasic.dados.mercado;

@SuppressWarnings("ALL")
public class TipoMercado {

    private String titulo;

    public TipoMercado() {
    }

    public TipoMercado(String titulo) {
        this.titulo = titulo;
    }


    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
}
