package com.azenithsolutions.backendapirest.v2.core.domain.repository;

import com.azenithsolutions.backendapirest.v2.core.domain.command.order.OrderRequestCommandDTO;
import com.azenithsolutions.backendapirest.v2.core.domain.model.order.Order;

import java.util.List;

public interface OrderGateway {
    List<Order> findAll();
    Order findById(Long id);
    Order save(OrderRequestCommandDTO command);
    Order update(Long id, OrderRequestCommandDTO command);
    void deleteById(Long id);
}
