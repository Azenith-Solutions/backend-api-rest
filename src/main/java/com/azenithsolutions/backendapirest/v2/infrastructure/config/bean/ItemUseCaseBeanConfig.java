package com.azenithsolutions.backendapirest.v2.infrastructure.config.bean;

import com.azenithsolutions.backendapirest.v2.core.usecase.item.CreateItemUseCase;
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
}
