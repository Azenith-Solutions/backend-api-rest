package com.azenithsolutions.backendapirest.v2.core.usecase.components;

import com.azenithsolutions.backendapirest.v2.core.domain.model.box.Box;
import com.azenithsolutions.backendapirest.v2.core.domain.model.category.Category;
import com.azenithsolutions.backendapirest.v2.core.domain.model.component.EletronicComponent;
import com.azenithsolutions.backendapirest.v2.core.domain.repository.BoxGateway;
import com.azenithsolutions.backendapirest.v2.core.domain.repository.CategoryRepositoryGateway;
import com.azenithsolutions.backendapirest.v2.core.domain.repository.EletronicComponentGateway;
import com.azenithsolutions.backendapirest.v2.core.usecase.components.command.CreateEletronicComponentCommand;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.components.ComponentRequestDTO;

import java.util.List;

public class CreateEletronicComponentUseCase {
    private final EletronicComponentGateway gateway;
    private final CategoryRepositoryGateway categoryRepositoryGateway;
    private final BoxGateway boxGateway;

    public CreateEletronicComponentUseCase(EletronicComponentGateway gateway, CategoryRepositoryGateway categoryRepositoryGateway, BoxGateway boxGateway) {
        this.gateway = gateway;
        this.categoryRepositoryGateway = categoryRepositoryGateway;
        this. boxGateway = boxGateway;
    }

    public EletronicComponent execute(ComponentRequestDTO command) {
        List<Category> categorias = categoryRepositoryGateway.findAll();
        Category categoria = categorias.stream().filter(item -> item.getIdCategoria().equals(command.getFkCategoria())).findFirst().orElseThrow();

        List<Box> boxes = boxGateway.findAll();
        Box box = boxes.stream().filter(item -> item.getIdCaixa().equals(command.getFkCaixa())).findFirst().orElseThrow();

        EletronicComponent componente = EletronicComponent.criarNovo(
                command.getNomeComponente(),
                categoria,
                command.getPartNumber(),
                command.getQuantidade(),
                box
        );

        return gateway.save(componente);
    }

}
