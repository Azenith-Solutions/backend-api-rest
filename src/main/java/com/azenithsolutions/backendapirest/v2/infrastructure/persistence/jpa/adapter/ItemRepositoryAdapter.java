package com.azenithsolutions.backendapirest.v2.infrastructure.persistence.jpa.adapter;

import com.azenithsolutions.backendapirest.v2.core.domain.model.item.Item;
import com.azenithsolutions.backendapirest.v2.core.domain.repository.ItemGateway;
import com.azenithsolutions.backendapirest.v2.infrastructure.persistence.jpa.entity.ItemEntity;
import com.azenithsolutions.backendapirest.v2.infrastructure.persistence.jpa.repository.SpringDataItemRepository;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.mappers.ItemEntityMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemRepositoryAdapter implements ItemGateway {
    SpringDataItemRepository itemRepository;

    public ItemRepositoryAdapter(SpringDataItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public Item save(Item item) {
        ItemEntity itemEntity = ItemEntityMapper.toEntity(item);
        itemRepository.save(itemEntity);
        return item;
    }

    @Override
    public List<Item> saveAll(List<Item> item) {
        List<ItemEntity> itemsSalvos = item.stream().map(ItemEntityMapper::toEntity).toList();
        itemRepository.saveAll(itemsSalvos);
        return item;
    }

    @Override
    public List<Item> findAll() {
        List<ItemEntity> listItemsEntity = itemRepository.findAll();
        List<Item> listItem = listItemsEntity.stream().map(ItemEntityMapper::toDomain).toList();
        return listItem;
    }

    @Override
    public Item findById(Long id) {
        Optional<ItemEntity> itemEntity = itemRepository.findById(id);
        Item itemDomain = ItemEntityMapper.toDomain(itemEntity.get());
        return itemDomain;
    }

    @Override
    public void deleteById(Long id) {

    }
}
