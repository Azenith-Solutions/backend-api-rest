package com.azenithsolutions.backendapirest.v2.core.usecase.components;

import com.azenithsolutions.backendapirest.v2.core.domain.model.component.EletronicComponent;
import com.azenithsolutions.backendapirest.v2.core.domain.model.component.valueobjects.Category;
import com.azenithsolutions.backendapirest.v2.core.domain.repository.EletronicComponentGateway;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.components.CreateEletronicComponentDTO;

public class CreateEletronicComponentUseCase {
    private final EletronicComponentGateway gateway;

    public CreateEletronicComponentUseCase(EletronicComponentGateway gateway) {
        this.gateway = gateway;
    }

    public EletronicComponent execute(CreateEletronicComponentDTO dto) {
        Category categoria = Category.criar(dto.getCategoria(), null);

        EletronicComponent componente = EletronicComponent.criarNovo(
                dto.getNome(),
                categoria,
                dto.getPartNumber(),
                dto.getQuantidade()
        );

        return gateway.save(componente);
    }

}
