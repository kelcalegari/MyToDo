package br.kelvynn.mytodobasic.dados.mercado;

public class ItemMercado {
    private String nome;
    private String nomeCategoria;
    private int quantidade;
    private boolean comprado;

    public ItemMercado() {
    }

    public ItemMercado(String nome, String nomeCategoria, int quantidade, boolean comprado) {

        this.nome = nome;
        this.nomeCategoria = nomeCategoria;
        this.quantidade = quantidade;
        this.comprado = comprado;
    }

    public String getNome() {
        return nome;
    }

    public String getNomeCategoria() {
        return nomeCategoria;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public boolean isComprado() {
        return comprado;
    }

    public void setComprado(boolean comprado) {
        this.comprado = comprado;
    }
}
