package com.azenithsolutions.backendapirest.v2.core.usecase.components;

import com.azenithsolutions.backendapirest.v2.core.domain.model.component.EletronicComponent;
import com.azenithsolutions.backendapirest.v2.core.domain.model.component.valueobjects.Category;
import com.azenithsolutions.backendapirest.v2.core.domain.repository.EletronicComponentGateway;
import com.azenithsolutions.backendapirest.v2.core.usecase.components.command.CreateComponentWithFileCommand;

public class CreateComponentWithFileUseCase {
    private final EletronicComponentGateway gateway;

    public CreateComponentWithFileUseCase(EletronicComponentGateway gateway) {
        this.gateway = gateway;
    }

    public EletronicComponent execute(CreateComponentWithFileCommand command, String imagePath) {
        Category categoria = Category.criar(command.categoria(), null);

        EletronicComponent componente = EletronicComponent.criarNovo(
                command.nome(),
                categoria,
                command.partNumber(),
                command.quantidade()
        );
        
        // In a real implementation, you would save the image first
        if (imagePath != null && !imagePath.isEmpty()) {
            componente = gateway.updateImagePath(null, imagePath);
        }

        return gateway.save(componente);
    }
}
