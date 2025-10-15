package com.azenithsolutions.backendapirest.v2.core.usecase.item;

import com.azenithsolutions.backendapirest.v2.core.domain.model.item.Item;
import com.azenithsolutions.backendapirest.v2.core.domain.repository.ItemGateway;
import jakarta.persistence.EntityNotFoundException;

public class DeleteItemUseCase {
    private ItemGateway itemGateway;

    public DeleteItemUseCase(ItemGateway itemGateway) {
        this.itemGateway = itemGateway;
    }

    public void execute(Long id){
        Item item = itemGateway.findById(id);

        if(item == null){
            throw new EntityNotFoundException("Usuário não encontrado");
        }

        itemGateway.deleteById(id);
    }
}
