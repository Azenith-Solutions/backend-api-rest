package com.azenithsolutions.backendapirest.v2.core.usecase.components;

import com.azenithsolutions.backendapirest.v2.core.domain.model.component.EletronicComponent;
import com.azenithsolutions.backendapirest.v2.core.domain.repository.EletronicComponentGateway;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.components.ComponentObservationDTO;

import java.util.List;

public class GetComponentsInObservationUseCase {
    private final EletronicComponentGateway gateway;

    public GetComponentsInObservationUseCase(EletronicComponentGateway gateway) {
        this.gateway = gateway;
    }

    public List<ComponentObservationDTO> execute() {
        return gateway.findComponentsInObservation();
    }
}
