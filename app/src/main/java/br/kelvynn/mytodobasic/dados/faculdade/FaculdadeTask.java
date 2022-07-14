package br.kelvynn.mytodobasic.dados.faculdade;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;
import java.util.Calendar;

public class FaculdadeTask implements Serializable {
    private String id;
    private String idMateria;
    private String nomeMateria;
    private String titulo;
    private String descricao;
    private Calendar dataVencimento;
    private Calendar dataConclusao;
    private int prioriedade;
    private boolean concluido;
    private boolean atrasado;

    public FaculdadeTask() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdMateria() {
        return idMateria;
    }

    public void setIdMateria(String idMateria) {
        this.idMateria = idMateria;
    }

    public String getNomeMateria() {
        return nomeMateria;
    }

    public void setNomeMateria(String nomeMateria) {
        this.nomeMateria = nomeMateria;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Calendar getDataVencimento() {
        return dataVencimento;
    }


    @Exclude
    public String getDataVencimentoStringShow() {

        String dia, mes;
        dia = String.valueOf(dataVencimento.get(Calendar.DAY_OF_MONTH));
        mes = String.valueOf(dataVencimento.get(Calendar.MONTH) + 1);

        if (dia.length() == 1) {
            dia = "0" + dia;
        }

        if (mes.length() == 1) {
            mes = "0" + mes;
        }
        return dia + "/" + mes + "/" + dataVencimento.get(Calendar.YEAR);
    }

    public void setDataVencimento(Calendar dataVencimento) {
        this.dataVencimento = dataVencimento;
    }


    @Exclude
    public String getDataConclusaoStringShow() {

        String dia, mes;
        dia = String.valueOf(dataConclusao.get(Calendar.DAY_OF_MONTH));
        mes = String.valueOf(dataConclusao.get(Calendar.MONTH) + 1);

        if (dia.length() == 1) {
            dia = "0" + dia;
        }

        if (mes.length() == 1) {
            mes = "0" + mes;
        }
        return dia + "/" + mes + "/" + dataConclusao.get(Calendar.YEAR);
    }

    public void setDataConclusao(Calendar dataConclusao) {
        this.dataConclusao = dataConclusao;
    }


    public int getPrioriedade() {
        return prioriedade;
    }

    public void setPrioriedade(int prioriedade) {
        this.prioriedade = prioriedade;
    }

    public boolean isConcluido() {
        return concluido;
    }

    public void setConcluido(boolean concluido) {
        this.concluido = concluido;
    }

    public boolean isAtrasado() {
        return atrasado;
    }

    public void setAtrasado(boolean atrasado) {
        this.atrasado = atrasado;
    }

    @Exclude
    public void delConcluido() {

        this.dataConclusao = null;
    }

    @Exclude
    public String getPrioriedadeString() {
        StringBuilder prioriedade = new StringBuilder("!");

        for (int i = 0; i < this.prioriedade; i++) {
            prioriedade.append("!");
        }

        return prioriedade.toString();
    }
}
