package com.azenithsolutions.backendapirest.v2.core.usecase.components;

import com.azenithsolutions.backendapirest.v2.core.domain.model.component.EletronicComponent;
import com.azenithsolutions.backendapirest.v2.core.domain.repository.EletronicComponentGateway;

import java.util.List;

public class GetEletronicComponentUseCase {
        private final EletronicComponentGateway gateway;

        public GetEletronicComponentUseCase(EletronicComponentGateway gateway) {
            this.gateway = gateway;
        }

    public List<EletronicComponent> execute() {
        return gateway.findAll();
    }
}
