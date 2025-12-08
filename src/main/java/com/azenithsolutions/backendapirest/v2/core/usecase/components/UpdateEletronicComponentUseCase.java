package com.azenithsolutions.backendapirest.v2.core.usecase.components;

import com.azenithsolutions.backendapirest.v2.core.domain.model.box.Box;
import com.azenithsolutions.backendapirest.v2.core.domain.model.category.Category;
import com.azenithsolutions.backendapirest.v2.core.domain.model.component.EletronicComponent;
import com.azenithsolutions.backendapirest.v2.core.domain.model.component.valueobjects.Status;
import com.azenithsolutions.backendapirest.v2.core.domain.repository.BoxGateway;
import com.azenithsolutions.backendapirest.v2.core.domain.repository.CategoryRepositoryGateway;
import com.azenithsolutions.backendapirest.v2.core.domain.repository.EletronicComponentGateway;
import com.azenithsolutions.backendapirest.v2.core.usecase.components.command.UpdateEletronicComponentCommand;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.components.ComponentRequestDTO;

import java.util.List;
import java.util.Optional;

public class UpdateEletronicComponentUseCase {
    private final EletronicComponentGateway gateway;
    private final CategoryRepositoryGateway categoryRepositoryGateway;
    private final BoxGateway boxGateway;

    public UpdateEletronicComponentUseCase(EletronicComponentGateway gateway, CategoryRepositoryGateway categoryRepositoryGateway, BoxGateway boxGateway) {
        this.gateway = gateway;
        this.categoryRepositoryGateway = categoryRepositoryGateway;
        this.boxGateway = boxGateway;
    }

    public EletronicComponent execute(Long id, ComponentRequestDTO command) {
        Optional<EletronicComponent> optionalComponent = gateway.findById(id);
        
        if (!optionalComponent.isPresent()) {
            throw new IllegalArgumentException("Componente com ID " + id + " não encontrado");
        }

        EletronicComponent component = optionalComponent.get();


        List<Category> categorias = categoryRepositoryGateway.findAll();
        Category categoria = categorias.stream().filter(item -> item.getIdCategoria().equals(command.getFkCategoria())).findFirst().orElseThrow();

        List<Box> boxes = boxGateway.findAll();
        Box box = boxes.stream().filter(item -> item.getIdCaixa().equals(command.getFkCaixa())).findFirst().orElseThrow();

        component = component.update(
                command.getIdHardWareTech(),
                command.getNomeComponente(),
                box,
                categoria,
                command.getPartNumber(),
                command.getQuantidade(),
                command.getIsVisibleCatalog(),
                command.getCodigoML(),
                Status.recriar(command.getFlagVerificado(), command.getCondicao().toString(), command.getObservacao())
        );

        return gateway.save(component);
    }
}
