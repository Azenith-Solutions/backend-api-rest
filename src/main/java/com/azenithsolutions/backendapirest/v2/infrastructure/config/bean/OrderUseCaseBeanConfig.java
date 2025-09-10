package com.azenithsolutions.backendapirest.v2.infrastructure.config.bean;

import com.azenithsolutions.backendapirest.v2.core.domain.repository.OrderGateway;
import com.azenithsolutions.backendapirest.v2.core.usecase.order.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderUseCaseBeanConfig {

    @Bean
    CreateOrderUseCase createOrderUseCase(OrderGateway repository) { return new CreateOrderUseCase(repository); }

    @Bean
    UpdateOrderUseCase updateOrderUseCase(OrderGateway repository) { return new UpdateOrderUseCase(repository); }

    @Bean
    GetOrderByIdUseCase getOrderByIdUseCase(OrderGateway repository) { return new GetOrderByIdUseCase(repository); }

    @Bean
    ListOrdersUseCase listOrdersUseCase(OrderGateway repository) { return new ListOrdersUseCase(repository); }

    @Bean
    DeleteOrderUseCase deleteOrderUseCase(OrderGateway repository) { return new DeleteOrderUseCase(repository); }
}
