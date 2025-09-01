package com.azenithsolutions.backendapirest.v2.infrastructure.config;

import com.azenithsolutions.backendapirest.v2.core.domain.repository.OrderRepositoryGateway;
import com.azenithsolutions.backendapirest.v2.core.usecase.order.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderUseCaseConfig {

    @Bean
    CreateOrderUseCase createOrderUseCase(OrderRepositoryGateway repository) { return new CreateOrderUseCase(repository); }

    @Bean
    UpdateOrderUseCase updateOrderUseCase(OrderRepositoryGateway repository) { return new UpdateOrderUseCase(repository); }

    @Bean
    GetOrderByIdUseCase getOrderByIdUseCase(OrderRepositoryGateway repository) { return new GetOrderByIdUseCase(repository); }

    @Bean
    ListOrdersUseCase listOrdersUseCase(OrderRepositoryGateway repository) { return new ListOrdersUseCase(repository); }

    @Bean
    DeleteOrderUseCase deleteOrderUseCase(OrderRepositoryGateway repository) { return new DeleteOrderUseCase(repository); }
}
