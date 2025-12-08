package com.azenithsolutions.backendapirest.v2.core.domain.repository;

import com.azenithsolutions.backendapirest.v2.core.domain.model.item.Item;

import java.util.List;

public interface ItemGateway {
    Item save(Item item);
    List<Item> saveAll(List<Item> item);
    List<Item> findAll();
    Item findById(Long id);
    void deleteById(Long id);
}
