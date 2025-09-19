package com.azenithsolutions.backendapirest.v2.core.usecase.order;

import com.azenithsolutions.backendapirest.v2.core.domain.model.order.Order;
import com.azenithsolutions.backendapirest.v2.core.domain.repository.OrderRepositoryGateway;

import java.util.NoSuchElementException;

public class GetOrderByIdUseCase {
    private final OrderRepositoryGateway repository;

    public GetOrderByIdUseCase(OrderRepositoryGateway repository) { this.repository = repository; }

    public Order execute(Long id) {
        return repository.findById(id).orElseThrow(() -> new NoSuchElementException("Pedido não encontrado"));
    }
}
