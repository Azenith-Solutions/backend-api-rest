package com.azenithsolutions.backendapirest.v1.model.enums;

public enum StatusPedido {
    EM_ANALISE("Em análise"),
    EM_ANDAMENTO("Em andamento"),
    CONCLUIDO("Concluído");

    private String formatedTitle;

    StatusPedido(String formatedTitle) {
        this.formatedTitle = formatedTitle;
    }

    public String getFormatedTitle() {
        return formatedTitle;
    }
}
