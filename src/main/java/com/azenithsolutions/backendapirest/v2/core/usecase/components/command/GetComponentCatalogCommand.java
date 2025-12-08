package com.azenithsolutions.backendapirest.v2.core.usecase.components.command;

public record GetComponentCatalogCommand(
    int page,
    int size,
    String nomeComponente,
    Long categoria
) { }
