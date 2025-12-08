package com.azenithsolutions.backendapirest.v2.core.usecase.components;

import com.azenithsolutions.backendapirest.v2.core.domain.model.component.EletronicComponent;
import com.azenithsolutions.backendapirest.v2.core.domain.repository.EletronicComponentGateway;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.components.ComponentManagerResponseDTO;

import java.util.List;

public class GetEletronicComponentUseCase {
        private final EletronicComponentGateway gateway;

        public GetEletronicComponentUseCase(EletronicComponentGateway gateway) {
            this.gateway = gateway;
        }

    public List<ComponentManagerResponseDTO> execute() {
        return gateway.findAll();
    }
}
