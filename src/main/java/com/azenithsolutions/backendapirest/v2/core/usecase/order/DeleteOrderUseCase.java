package com.azenithsolutions.backendapirest.v2.core.usecase.order;

import com.azenithsolutions.backendapirest.v2.core.domain.repository.OrderGateway;

public class DeleteOrderUseCase {
    private final OrderGateway gateway;

    public DeleteOrderUseCase(OrderGateway gateway) {
        this.gateway = gateway;
    }

    public void execute(Long id) {
        this.gateway.deleteById(id);
    }
}
