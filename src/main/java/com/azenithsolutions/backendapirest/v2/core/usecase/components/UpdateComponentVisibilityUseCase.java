package com.azenithsolutions.backendapirest.v2.core.usecase.components;

import com.azenithsolutions.backendapirest.v2.core.domain.model.component.EletronicComponent;
import com.azenithsolutions.backendapirest.v2.core.domain.repository.EletronicComponentGateway;
import com.azenithsolutions.backendapirest.v2.core.usecase.components.command.UpdateComponentVisibilityCommand;

import java.util.Optional;

public class UpdateComponentVisibilityUseCase {
    private final EletronicComponentGateway gateway;

    public UpdateComponentVisibilityUseCase(EletronicComponentGateway gateway) {
        this.gateway = gateway;
    }

    public EletronicComponent execute(Long id, UpdateComponentVisibilityCommand command) {
        Optional<EletronicComponent> componentOpt = gateway.findById(id);
        
        if (componentOpt.isEmpty()) {
            throw new RuntimeException("Componente com ID %d não encontrado".formatted(id));
        }
        
        EletronicComponent component = componentOpt.get();
        component = gateway.updateVisibility(id, command.anunciado());
        
        return component;
    }
}
