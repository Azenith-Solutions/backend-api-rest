package com.azenithsolutions.backendapirest.v1.model.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum OrderStatus {
    PENDENTE("Pendente"),
    EM_ANDAMENTO("Em andamento"),
    CONCLUIDO("Concluído");

    private final String formatedTitle;
}
