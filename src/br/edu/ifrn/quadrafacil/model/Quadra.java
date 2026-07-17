package br.edu.ifrn.quadrafacil.model;

public class Quadra {
    private int id;
    private String nome;
    private String endereco;
    private String situacao;

    public Quadra() {
    }

    public Quadra(int id, String nome, String endereco, String situacao) {
        this.id = id;
        this.nome = nome;
        this.endereco = endereco;
        this.situacao = situacao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    @Override
    public String toString() {
        return id + " | " + nome + " | " + endereco + " | " + situacao;
    }
}
