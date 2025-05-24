package com.azenithsolutions.backendapirest.v1.model.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ComponentCondition {
    BOM_ESTADO("Bom estado"),
    OBSERVACAO("Observação"),
    NULL("Null");

    public final String descricao;
}
