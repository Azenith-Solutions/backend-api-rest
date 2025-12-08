package com.azenithsolutions.backendapirest.v2.infrastructure.external.order;

import com.azenithsolutions.backendapirest.v2.core.domain.command.order.OrderRequestCommandDTO;
import com.azenithsolutions.backendapirest.v2.core.domain.model.order.Order;
import com.azenithsolutions.backendapirest.v2.core.domain.repository.OrderGateway;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class OrderGatewayAdapter implements OrderGateway {
    private final RestClient orderClient;

    public OrderGatewayAdapter(@Qualifier("orderRestClient") RestClient orderClient) {
        this.orderClient = orderClient;
    }

    @Override
    public List<Order> findAll() {
        try {
            return orderClient.get()
                    .uri("/orders")
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<Order>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Failed to return the orders", e);
        }
    }

    @Override
    public Order findById(Long id) {
        try {
            return orderClient.get()
                    .uri("/orders/{id}", id)
                    .retrieve()
                    .body(Order.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to return the order", e);
        }
    }

    @Override
    public Order save(OrderRequestCommandDTO order) {
        try {
            return orderClient.post()
                    .uri("/orders")
                    .body(order)
                    .retrieve()
                    .body(Order.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save the order", e);
        }
    }

    @Override
    public Order update(Long id, OrderRequestCommandDTO order) {
        try {
            return orderClient.put()
                    .uri("/orders/{id}", id)
                    .body(order)
                    .retrieve()
                    .body(Order.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update the order", e);
        }
    }

    @Override
    public void deleteById(Long id) {
        try {
            orderClient.delete()
                    .uri("/orders/{id}", id)
                    .retrieve()
                    .toBodilessEntity();
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete the order", e);
        }
    }
}
