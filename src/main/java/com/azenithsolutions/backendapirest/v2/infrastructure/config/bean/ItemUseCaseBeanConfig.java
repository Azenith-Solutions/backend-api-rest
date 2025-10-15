package com.azenithsolutions.backendapirest.v2.infrastructure.config.bean;

import com.azenithsolutions.backendapirest.v2.core.usecase.item.*;
import com.azenithsolutions.backendapirest.v2.infrastructure.persistence.jpa.adapter.EletronicComponentAdapter;
import com.azenithsolutions.backendapirest.v2.infrastructure.persistence.jpa.adapter.ItemRepositoryAdapter;
import com.azenithsolutions.backendapirest.v2.infrastructure.persistence.jpa.adapter.OrderAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ItemUseCaseBeanConfig {
    @Bean
    CreateItemUseCase createItemUseCase(ItemRepositoryAdapter itemRepositoryAdapter, EletronicComponentAdapter eletronicComponentAdapter, OrderAdapter orderAdapter){
        return new CreateItemUseCase(itemRepositoryAdapter, eletronicComponentAdapter, orderAdapter);
    }

    @Bean
    GetAllItemUseCase getAllItemUseCase(ItemRepositoryAdapter itemRepositoryAdapter){
        return new GetAllItemUseCase(itemRepositoryAdapter);
    }

    @Bean
    GetItemByIdUseCase getItemByIdUseCase(ItemRepositoryAdapter itemRepositoryAdapter){
        return new GetItemByIdUseCase(itemRepositoryAdapter);
    }

    @Bean
    UpdateItemUseCase updateItemUseCase(ItemRepositoryAdapter itemRepositoryAdapter, EletronicComponentAdapter eletronicComponentAdapter, OrderAdapter orderAdapter){
        return new UpdateItemUseCase(itemRepositoryAdapter, eletronicComponentAdapter, orderAdapter);
    }

    @Bean
    DeleteItemUseCase deleteItemUseCase(ItemRepositoryAdapter itemRepositoryAdapter){
        return new DeleteItemUseCase(itemRepositoryAdapter);
    }
}
