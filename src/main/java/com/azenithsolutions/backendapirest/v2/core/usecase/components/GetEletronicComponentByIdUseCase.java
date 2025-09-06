package com.azenithsolutions.backendapirest.v2.core.usecase.components;

import com.azenithsolutions.backendapirest.v2.core.domain.model.component.EletronicComponent;

import java.util.Optional;

public class GetEletronicComponentByIdUseCase {
    private final com.azenithsolutions.backendapirest.v2.core.domain.repository.EletronicComponentGateway gateway;

    public GetEletronicComponentByIdUseCase(com.azenithsolutions.backendapirest.v2.core.domain.repository.EletronicComponentGateway gateway) {
        this.gateway = gateway;
    }

    public Optional<EletronicComponent> execute(Long id) {
        return gateway.findById(id);
    }
}
