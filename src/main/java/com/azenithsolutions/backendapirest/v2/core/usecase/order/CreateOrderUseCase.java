package com.azenithsolutions.backendapirest.v2.core.usecase.order;

import com.azenithsolutions.backendapirest.v2.core.domain.model.order.Order;
import com.azenithsolutions.backendapirest.v2.core.domain.repository.OrderRepositoryGateway;

public class CreateOrderUseCase {
    private final OrderRepositoryGateway repository;

    public CreateOrderUseCase(OrderRepositoryGateway repository) { this.repository = repository; }

    public Order execute(Order order) {
        return repository.save(order);
    }
}
