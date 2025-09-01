package com.azenithsolutions.backendapirest.v2.core.usecase.order;

import com.azenithsolutions.backendapirest.v2.core.domain.model.order.Order;
import com.azenithsolutions.backendapirest.v2.core.domain.repository.OrderGateway;

public class CreateOrderUseCase {
    private final OrderGateway repository;

    public CreateOrderUseCase(OrderGateway repository) { this.repository = repository; }

    public Order execute(Order order) {
        return repository.save(order);
    }
}
