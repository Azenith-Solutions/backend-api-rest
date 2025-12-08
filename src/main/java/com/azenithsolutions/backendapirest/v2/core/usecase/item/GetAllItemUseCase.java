package com.azenithsolutions.backendapirest.v2.core.usecase.item;

import com.azenithsolutions.backendapirest.v2.core.domain.model.item.Item;
import com.azenithsolutions.backendapirest.v2.core.domain.repository.ItemGateway;

import java.util.List;

public class GetAllItemUseCase {
    private ItemGateway itemGateway;

    public GetAllItemUseCase(ItemGateway itemGateway) {
        this.itemGateway = itemGateway;
    }

    public List<Item> execute(){
        return itemGateway.findAll();
    }
}


