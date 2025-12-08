package com.azenithsolutions.backendapirest.v2.core.usecase.components;

import com.azenithsolutions.backendapirest.v2.core.domain.repository.EletronicComponentGateway;

public class DeleteEletronicComponentUseCase {
    private final EletronicComponentGateway gateway;

    public DeleteEletronicComponentUseCase(EletronicComponentGateway gateway) {
        this.gateway = gateway;
    }

    public void execute(Long id) {
        gateway.deleteById(id);
    }
}
