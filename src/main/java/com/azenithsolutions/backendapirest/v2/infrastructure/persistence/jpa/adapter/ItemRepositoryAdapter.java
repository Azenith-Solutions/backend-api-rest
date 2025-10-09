package com.azenithsolutions.backendapirest.v2.infrastructure.persistence.jpa.adapter;

import com.azenithsolutions.backendapirest.v2.core.domain.model.item.Item;
import com.azenithsolutions.backendapirest.v2.core.domain.repository.ItemGateway;

import java.util.List;

public class ItemRepositoryAdapter implements ItemGateway {

    @Override
    public Item save(Item item) {
        return null;
    }

    @Override
    public List<Item> saveAll(List<Item> item) {

        return List.of();
    }

    @Override
    public List<Item> findAll() {
        return List.of();
    }

    @Override
    public Item findById(Long id) {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }
}
