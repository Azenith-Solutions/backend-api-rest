package com.azenithsolutions.backendapirest.v1.model.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ComponentCondition {
    BOM_ESTADO("Bom estado"),
    OBSERVACAO("Observação"),
    CRITICO("Crítico");

    public final String descricao;

}
