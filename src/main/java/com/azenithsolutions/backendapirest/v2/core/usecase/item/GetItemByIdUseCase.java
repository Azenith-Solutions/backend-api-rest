package com.azenithsolutions.backendapirest.v2.core.usecase.item;

import com.azenithsolutions.backendapirest.v2.core.domain.model.item.Item;
import com.azenithsolutions.backendapirest.v2.core.domain.repository.ItemGateway;
import jakarta.persistence.EntityNotFoundException;

public class GetItemByIdUseCase {
    private ItemGateway itemGateway;

    public GetItemByIdUseCase(ItemGateway itemGateway) {
        this.itemGateway = itemGateway;
    }

    public Item execute(Long id){
        Item item = itemGateway.findById(id);
        if(item.equals(null)){
            throw new EntityNotFoundException("Usuário não encontrado");
        }

        return item;
    }
}
