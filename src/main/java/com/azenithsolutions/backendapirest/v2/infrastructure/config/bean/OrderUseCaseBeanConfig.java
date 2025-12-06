package com.azenithsolutions.backendapirest.v2.infrastructure.config.bean;

import com.azenithsolutions.backendapirest.v2.core.domain.repository.OrderEmailCacheGateway;
import com.azenithsolutions.backendapirest.v2.core.domain.repository.OrderGateway;
import com.azenithsolutions.backendapirest.v2.core.usecase.order.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderUseCaseBeanConfig {

    @Bean
    CreateOrderUseCase createOrderUseCase(OrderGateway gateway) { return new CreateOrderUseCase(gateway); }

    @Bean
    UpdateOrderUseCase updateOrderUseCase(OrderGateway gateway) { return new UpdateOrderUseCase(gateway); }

    @Bean
    GetOrderByIdUseCase getOrderByIdUseCase(OrderGateway gateway) { return new GetOrderByIdUseCase(gateway); }

    @Bean
    ListOrdersUseCase listOrdersUseCase(OrderGateway gateway) { return new ListOrdersUseCase(gateway); }

    @Bean
    DeleteOrderUseCase deleteOrderUseCase(OrderGateway gateway) { return new DeleteOrderUseCase(gateway); }
    
    @Bean
    PublishOrderWithQuoteUseCase publishOrderWithQuoteUseCase(OrderEmailCacheGateway emailCacheGateway) { 
        return new PublishOrderWithQuoteUseCase(emailCacheGateway); 
    }
}
