package com.azenithsolutions.backendapirest.v2.core.usecase.order;

import com.azenithsolutions.backendapirest.v2.core.domain.model.Order;
import com.azenithsolutions.backendapirest.v2.core.domain.repository.order.OrderRepositoryPort;

import java.util.NoSuchElementException;

public class GetOrderByIdUseCase {
    private final OrderRepositoryPort repository;

    public GetOrderByIdUseCase(OrderRepositoryPort repository) { this.repository = repository; }

    public Order execute(Long id) {
        return repository.findById(id).orElseThrow(() -> new NoSuchElementException("Pedido não encontrado"));
    }
}
