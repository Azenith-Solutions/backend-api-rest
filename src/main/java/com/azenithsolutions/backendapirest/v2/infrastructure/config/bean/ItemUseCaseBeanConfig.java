package com.azenithsolutions.backendapirest.v2.infrastructure.config.bean;

import com.azenithsolutions.backendapirest.v2.core.usecase.item.*;
import com.azenithsolutions.backendapirest.v2.infrastructure.external.order.OrderGatewayAdapter;
import com.azenithsolutions.backendapirest.v2.infrastructure.persistence.jpa.adapter.EletronicComponentAdapter;
import com.azenithsolutions.backendapirest.v2.infrastructure.persistence.jpa.adapter.ItemRepositoryAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ItemUseCaseBeanConfig {
    @Bean
    CreateItemUseCase createItemUseCase(ItemRepositoryAdapter itemRepositoryAdapter, EletronicComponentAdapter eletronicComponentAdapter, OrderGatewayAdapter orderGatewayAdapter) {
        return new CreateItemUseCase(itemRepositoryAdapter, eletronicComponentAdapter, orderGatewayAdapter);
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
    UpdateItemUseCase updateItemUseCase(ItemRepositoryAdapter itemRepositoryAdapter, EletronicComponentAdapter eletronicComponentAdapter, OrderGatewayAdapter orderGatewayAdapter){
        return new UpdateItemUseCase(itemRepositoryAdapter, eletronicComponentAdapter, orderGatewayAdapter);
    }

    @Bean
    DeleteItemUseCase deleteItemUseCase(ItemRepositoryAdapter itemRepositoryAdapter){
        return new DeleteItemUseCase(itemRepositoryAdapter);
    }
}
