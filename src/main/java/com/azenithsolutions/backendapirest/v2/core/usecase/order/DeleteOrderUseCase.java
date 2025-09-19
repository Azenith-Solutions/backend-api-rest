package com.azenithsolutions.backendapirest.v2.core.usecase.order;

import com.azenithsolutions.backendapirest.v2.core.domain.repository.OrderRepositoryGateway;

import java.util.NoSuchElementException;

public class DeleteOrderUseCase {
    private final OrderRepositoryGateway repository;

    public DeleteOrderUseCase(OrderRepositoryGateway repository) { this.repository = repository; }

    public void execute(Long id) {
        if (!repository.existsById(id)) {
            throw new NoSuchElementException("Pedido não encontrado");
        }
        repository.deleteById(id);
    }
}
