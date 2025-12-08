package com.azenithsolutions.backendapirest.v2.core.usecase.item.command;


public record ItemCreateCommand(Long fkComponente, Long fkPedido, Integer quantidadeCarrinho) {
}
