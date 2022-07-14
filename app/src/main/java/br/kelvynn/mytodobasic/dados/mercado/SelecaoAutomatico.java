package br.kelvynn.mytodobasic.dados.mercado;

@SuppressWarnings("ALL")
public class SelecaoAutomatico {
    private String nomeItem;
    private String nomeCategoria;

    public SelecaoAutomatico() {
    }

    public SelecaoAutomatico(String nomeItem, String nomeCategoria) {
        this.nomeItem = nomeItem;
        this.nomeCategoria = nomeCategoria;
    }

    public String getNomeItem() {
        return nomeItem;
    }

    public void setNomeItem(String nomeItem) {
        this.nomeItem = nomeItem;
    }

    public void setNomeCategoria(String nomeCategoria) {
        this.nomeCategoria = nomeCategoria;
    }

    @SuppressWarnings("unused")
    public String getNomeCategoria() {
        return nomeCategoria;
    }

}
