package com.azenithsolutions.backendapirest.v1.model.enums;

public enum StatusPedido {
    EM_ANALISE("EM_ANALISE"),
    EM_ANDAMENTO("EM_ANDAMENTO"),
    CONCLUIDO("CONCLUIDO");

    private String formatedTitle;

    StatusPedido(String formatedTitle) {
        this.formatedTitle = formatedTitle;
    }

    public String getFormatedTitle() {
        return formatedTitle;
    }
}
