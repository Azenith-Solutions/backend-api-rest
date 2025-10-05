package com.azenithsolutions.backendapirest.v2.core.usecase.components;

import com.azenithsolutions.backendapirest.v2.core.domain.model.component.EletronicComponent;
import com.azenithsolutions.backendapirest.v2.core.domain.repository.EletronicComponentGateway;

import java.util.Optional;

public class GetComponentDetailsUseCase {
    private final EletronicComponentGateway gateway;

    public GetComponentDetailsUseCase(EletronicComponentGateway gateway) {
        this.gateway = gateway;
    }

    public Optional<EletronicComponent> execute(Long id) {
        return gateway.findDetailsById(id);
    }
}
