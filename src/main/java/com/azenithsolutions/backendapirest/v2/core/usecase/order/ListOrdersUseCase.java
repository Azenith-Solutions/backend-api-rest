package com.azenithsolutions.backendapirest.v2.core.usecase.order;

import com.azenithsolutions.backendapirest.v2.core.domain.model.order.Order;
import com.azenithsolutions.backendapirest.v2.core.domain.repository.OrderGateway;

import java.util.List;

public class ListOrdersUseCase {
    private final OrderGateway repository;

    public ListOrdersUseCase(OrderGateway repository) { this.repository = repository; }

    public List<Order> execute() { return repository.findAll(); }
}
