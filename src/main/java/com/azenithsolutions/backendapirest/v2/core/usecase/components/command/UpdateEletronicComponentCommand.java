package com.azenithsolutions.backendapirest.v2.core.usecase.components.command;

public record UpdateEletronicComponentCommand(
    String nome,
    String categoria,
    String partNumber,
    int quantidade
) { }
