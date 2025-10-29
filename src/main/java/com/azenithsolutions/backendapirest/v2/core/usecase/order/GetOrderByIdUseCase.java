package com.azenithsolutions.backendapirest.v2.core.usecase.order;

import com.azenithsolutions.backendapirest.v2.core.domain.model.order.Order;
import com.azenithsolutions.backendapirest.v2.core.domain.repository.OrderGateway;

public class GetOrderByIdUseCase {
    private final OrderGateway gateway;

    public GetOrderByIdUseCase(OrderGateway gateway) {
        this.gateway = gateway;
    }

    public Order execute(Long id) {
        return this.gateway.findById(id);
    }
}
