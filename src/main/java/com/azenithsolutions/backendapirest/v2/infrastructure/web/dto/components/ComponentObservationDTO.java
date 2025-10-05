package com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.components;

public class ComponentObservationDTO {
    private Long id;
    private String nome;
    private String categoria;
    private String observacao;

    public ComponentObservationDTO() {}

    public ComponentObservationDTO(Long id, String nome, String categoria, String observacao) {
        this.id = id;
        this.nome = nome;
        this.categoria = categoria;
        this.observacao = observacao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
}
