package com.azenithsolutions.backendapirest.v2.core.usecase.order;

import com.azenithsolutions.backendapirest.v2.core.domain.command.order.OrderRequestCommandDTO;
import com.azenithsolutions.backendapirest.v2.core.domain.model.order.Order;
import com.azenithsolutions.backendapirest.v2.core.domain.repository.OrderGateway;

public class UpdateOrderUseCase {
    private final OrderGateway gateway;

    public UpdateOrderUseCase(OrderGateway gateway) {
        this.gateway = gateway;
    }

    public Order execute(Long id, OrderRequestCommandDTO command) {
        return this.gateway.update(id, command);
    }
}
