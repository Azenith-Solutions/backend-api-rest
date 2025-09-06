package com.azenithsolutions.backendapirest.v2.core.usecase.components;

import com.azenithsolutions.backendapirest.v2.core.domain.model.component.EletronicComponent;
import com.azenithsolutions.backendapirest.v2.core.domain.model.component.valueobjects.Category;
import com.azenithsolutions.backendapirest.v2.core.domain.repository.EletronicComponentGateway;
import com.azenithsolutions.backendapirest.v2.core.usecase.components.command.CreateEletronicComponentCommand;

public class CreateEletronicComponentUseCase {
    private final EletronicComponentGateway gateway;

    public CreateEletronicComponentUseCase(EletronicComponentGateway gateway) {
        this.gateway = gateway;
    }

    public EletronicComponent execute(CreateEletronicComponentCommand command) {
        Category categoria = Category.criar(command.categoria(), null);

        EletronicComponent componente = EletronicComponent.criarNovo(
                command.nome(),
                categoria,
                command.partNumber(),
                command.quantidade()
        );

        return gateway.save(componente);
    }

}
