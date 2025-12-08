package com.azenithsolutions.backendapirest.v2.core.usecase.components;

import com.azenithsolutions.backendapirest.v2.core.domain.repository.EletronicComponentGateway;
import com.azenithsolutions.backendapirest.v2.core.usecase.components.command.FilterComponentCommand;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.components.ComponentCatalogResponseDTO;

import java.util.List;

public class GetFilterComponentsUseCase {
    private final EletronicComponentGateway gateway;

    public GetFilterComponentsUseCase(EletronicComponentGateway gateway) {
        this.gateway = gateway;
    }

    public List<ComponentCatalogResponseDTO> execute(FilterComponentCommand command) {
        return gateway.findByFilters(command.filtros());
    }
}
