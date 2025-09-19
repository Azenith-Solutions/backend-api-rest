package com.azenithsolutions.backendapirest.v2.core.usecase.order;

import com.azenithsolutions.backendapirest.v2.core.domain.model.order.Order;
import com.azenithsolutions.backendapirest.v2.core.domain.repository.OrderRepositoryGateway;

import java.util.List;

public class ListOrdersUseCase {
    private final OrderRepositoryGateway repository;

    public ListOrdersUseCase(OrderRepositoryGateway repository) { this.repository = repository; }

    public List<Order> execute() { return repository.findAll(); }
}
