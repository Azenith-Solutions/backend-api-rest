package com.azenithsolutions.backendapirest.v2.infrastructure.config;

import com.azenithsolutions.backendapirest.v2.core.domain.repository.order.OrderRepositoryPort;
import com.azenithsolutions.backendapirest.v2.core.usecase.order.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderUseCaseConfig {

    @Bean
    CreateOrderUseCase createOrderUseCase(OrderRepositoryPort repositoryPort) { return new CreateOrderUseCase(repositoryPort); }

    @Bean
    UpdateOrderUseCase updateOrderUseCase(OrderRepositoryPort repositoryPort) { return new UpdateOrderUseCase(repositoryPort); }

    @Bean
    GetOrderByIdUseCase getOrderByIdUseCase(OrderRepositoryPort repositoryPort) { return new GetOrderByIdUseCase(repositoryPort); }

    @Bean
    ListOrdersUseCase listOrdersUseCase(OrderRepositoryPort repositoryPort) { return new ListOrdersUseCase(repositoryPort); }

    @Bean
    DeleteOrderUseCase deleteOrderUseCase(OrderRepositoryPort repositoryPort) { return new DeleteOrderUseCase(repositoryPort); }
}
