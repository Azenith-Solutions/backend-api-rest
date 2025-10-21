package com.azenithsolutions.backendapirest.v2.core.usecase.components;

import com.azenithsolutions.backendapirest.v2.core.domain.model.category.Category;
import com.azenithsolutions.backendapirest.v2.core.domain.model.component.EletronicComponent;
import com.azenithsolutions.backendapirest.v2.core.domain.repository.EletronicComponentGateway;
import com.azenithsolutions.backendapirest.v2.core.usecase.components.command.UpdateEletronicComponentCommand;

import java.util.Optional;

public class UpdateEletronicComponentUseCase {
    private final EletronicComponentGateway gateway;

    public UpdateEletronicComponentUseCase(EletronicComponentGateway gateway) {
        this.gateway = gateway;
    }

    public EletronicComponent execute(Long id, UpdateEletronicComponentCommand command) {
        Optional<EletronicComponent> optionalComponent = gateway.findById(id);
        
        if (!optionalComponent.isPresent()) {
            throw new IllegalArgumentException("Componente com ID " + id + " não encontrado");
        }

        EletronicComponent componente = optionalComponent.get();
        Category categoria = Category.create(null, command.categoria());

        componente.update(
            command.nome(),
            categoria,
            command.partNumber(),
            command.quantidade()
        );

        return gateway.save(componente);
    }
}
