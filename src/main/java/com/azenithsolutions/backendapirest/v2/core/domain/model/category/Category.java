package com.azenithsolutions.backendapirest.v2.core.domain.model.category;


public class Category {
    private Long idCategoria;

    private String nomeCategoria;

    public Category(Long idCategoria, String nomeCategoria) {
        this.idCategoria = idCategoria;
        this.nomeCategoria = nomeCategoria;
    }

    public static Category create(Long categoria, String nomeCategoria) {
        return new Category(categoria, nomeCategoria);
    }

    public Long getIdCategoria() {
        return idCategoria;
    }

    public String getNomeCategoria() {
        return nomeCategoria;
    }

}