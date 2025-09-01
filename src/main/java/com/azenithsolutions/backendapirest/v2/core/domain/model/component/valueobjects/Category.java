package com.azenithsolutions.backendapirest.v2.core.domain.model.component.valueobjects;

public class Category {
    private final Long id;
    private String nome;
    private String descricao;

    private Category(Long id, String nome, String descricao) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
    }

    public static Category criar(String nome, String descricao) {
        validarNome(nome);
        return new Category(null, nome, descricao);
    }

    private static void validarNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome da categoria não pode ser vazio");
        }
    }


    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        validarNome(nome);
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
