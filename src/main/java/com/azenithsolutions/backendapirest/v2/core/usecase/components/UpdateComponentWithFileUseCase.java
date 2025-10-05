package com.azenithsolutions.backendapirest.v2.core.usecase.components;

import com.azenithsolutions.backendapirest.v2.core.domain.model.component.EletronicComponent;
import com.azenithsolutions.backendapirest.v2.core.domain.model.component.valueobjects.Category;
import com.azenithsolutions.backendapirest.v2.core.domain.repository.EletronicComponentGateway;
import com.azenithsolutions.backendapirest.v2.core.usecase.components.command.UpdateComponentWithFileCommand;

import java.util.Optional;

public class UpdateComponentWithFileUseCase {
    private final EletronicComponentGateway gateway;

    public UpdateComponentWithFileUseCase(EletronicComponentGateway gateway) {
        this.gateway = gateway;
    }

    public EletronicComponent execute(Long id, UpdateComponentWithFileCommand command, String imagePath) {
        Optional<EletronicComponent> componentOpt = gateway.findById(id);
        
        if (componentOpt.isEmpty()) {
            throw new RuntimeException("Componente com ID %d não encontrado".formatted(id));
        }
        
        EletronicComponent component = componentOpt.get();
        Category categoria = Category.criar(command.categoria(), null);
        
        component = component.update(command.nome(), categoria, command.partNumber(), command.quantidade());
        
        // Update image path if provided
        if (imagePath != null && !imagePath.isEmpty()) {
            component = gateway.updateImagePath(id, imagePath);
        }
        
        return gateway.save(component);
    }
}
