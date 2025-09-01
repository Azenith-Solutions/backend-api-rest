package com.azenithsolutions.backendapirest.v2.core.usecase.order;

import com.azenithsolutions.backendapirest.v2.core.domain.model.Order;
import com.azenithsolutions.backendapirest.v2.core.domain.repository.order.OrderRepositoryPort;

public class CreateOrderUseCase {
    private final OrderRepositoryPort repository;

    public CreateOrderUseCase(OrderRepositoryPort repository) { this.repository = repository; }

    public Order execute(Order order) {
        return repository.save(order);
    }
}
