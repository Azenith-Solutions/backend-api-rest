package com.azenithsolutions.backendapirest.v2.core.usecase.order;

import com.azenithsolutions.backendapirest.v2.core.domain.model.Order;
import com.azenithsolutions.backendapirest.v2.core.domain.repository.order.OrderRepositoryPort;

import java.util.List;

public class ListOrdersUseCase {
    private final OrderRepositoryPort repository;

    public ListOrdersUseCase(OrderRepositoryPort repository) { this.repository = repository; }

    public List<Order> execute() { return repository.findAll(); }
}
