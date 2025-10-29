package com.azenithsolutions.backendapirest.v2.core.usecase.order;

import com.azenithsolutions.backendapirest.v2.core.domain.command.order.OrderRequestCommandDTO;
import com.azenithsolutions.backendapirest.v2.core.domain.model.order.Order;
import com.azenithsolutions.backendapirest.v2.core.domain.repository.OrderGateway;

public class CreateOrderUseCase {
    private final OrderGateway gateway;

    public CreateOrderUseCase(OrderGateway gateway) { this.gateway = gateway; }

    public Order execute(OrderRequestCommandDTO command) {
        return gateway.save(command);
    }
}
