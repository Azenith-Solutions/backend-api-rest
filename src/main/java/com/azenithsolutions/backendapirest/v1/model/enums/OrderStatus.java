package com.azenithsolutions.backendapirest.v1.model.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum OrderStatus {
    EM_ANALISE("Em análise"),
    EM_ANDAMENTO("Em andamento"),
    CONCLUIDO("Concluído");

    private final String formatedTitle;
}
