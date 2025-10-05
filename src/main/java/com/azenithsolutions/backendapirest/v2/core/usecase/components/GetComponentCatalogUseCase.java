package com.azenithsolutions.backendapirest.v2.core.usecase.components;

import com.azenithsolutions.backendapirest.v2.core.domain.model.component.EletronicComponent;
import com.azenithsolutions.backendapirest.v2.core.domain.repository.EletronicComponentGateway;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class GetComponentCatalogUseCase {
    private final EletronicComponentGateway gateway;

    public GetComponentCatalogUseCase(EletronicComponentGateway gateway) {
        this.gateway = gateway;
    }

    public Page<EletronicComponent> execute(Pageable pageable, String nome, Long categoriaId) {
        return gateway.findPageable(pageable, nome, categoriaId);
    }
}
