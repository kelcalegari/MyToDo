package br.kelvynn.mytodobasic.dados.task;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Calendar;

@SuppressWarnings("unused")
public class Task implements Serializable {
    private String id;
    private String idCategoriaTask;
    private String nomeCategoria;
    private String titulo;
    private String descricao;
    private Calendar dataVencimento;
    private Calendar dataConclusao;
    private int prioriedade;
    private boolean concluido;
    private boolean atrasado; // 0- concluido 1 - concluido 2 - atrasado

    public Task() {

    }

    public Task(String idCategoriaTask, String titulo, String descricao, Calendar dataVencimento, int prioriedade, int status) {
        this.idCategoriaTask = idCategoriaTask;
        this.titulo = titulo;
        this.descricao = descricao;

        this.dataVencimento = dataVencimento;
        this.prioriedade = prioriedade;

    }

    public Task(String id, String idCategoriaTask, String titulo, String descricao, Calendar dataVencimento, int prioriedade, int status) {
        this.id = id;
        this.idCategoriaTask = idCategoriaTask;
        this.titulo = titulo;
        this.descricao = descricao;
        this.dataVencimento = dataVencimento;
        this.prioriedade = prioriedade;

    }


    public boolean isAtrasado() {
        return atrasado;
    }

    public void setAtrasado(boolean atrasado) {
        this.atrasado = atrasado;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdCategoriaTask() {
        return idCategoriaTask;
    }

    public Calendar getDataConclusao() {
        return dataConclusao;
    }

    public void setDataConclusao(Calendar dataConclusao) {
        this.dataConclusao = dataConclusao;
    }

    public void setIdCategoriaTask(String idCategoriaTask) {
        this.idCategoriaTask = idCategoriaTask;
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

    public String getNomeCategoria() {
        return nomeCategoria;
    }

    public void setNomeCategoria(String nomeCategoria) {
        this.nomeCategoria = nomeCategoria;
    }

    public Calendar getDataVencimento() {
        return dataVencimento;
    }

    public String getDataVencimentoString() {
        String dia, mes;
        dia = String.valueOf(dataVencimento.get(Calendar.DAY_OF_MONTH));
        mes = String.valueOf(dataVencimento.get(Calendar.MONTH));

        if (dia.length() == 1) {
            dia = "0" + dia;
        }

        if (mes.length() == 1) {
            mes = "0" + mes;
        }
        return dia + "/" + mes + "/" + dataVencimento.get(Calendar.YEAR);
    }

    public boolean isConcluido() {
        return concluido;
    }

    public void setConcluido(boolean concluido) {

        this.concluido = concluido;
    }

    public void delConcluido() {

        this.dataConclusao = null;
    }


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


    public int getPrioriedade() {
        return prioriedade;
    }

    public String getPrioriedadeString() {
        StringBuilder prioriedade = new StringBuilder("!");

        for (int i = 0; i < this.prioriedade; i++) {
            prioriedade.append("!");
        }

        return prioriedade.toString();
    }


    public void setPrioriedade(int prioriedade) {
        this.prioriedade = prioriedade;
    }


    @NonNull
    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", idCategoriaTask=" + idCategoriaTask +
                ", titulo='" + titulo + '\'' +
                ", descricao='" + descricao + '\'' +
                ", dataVencimento=" + getDataVencimentoString() +
                ", prioriedade=" + prioriedade +
                ", nomeCategoria=" + nomeCategoria +
                '}';
    }


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
}
