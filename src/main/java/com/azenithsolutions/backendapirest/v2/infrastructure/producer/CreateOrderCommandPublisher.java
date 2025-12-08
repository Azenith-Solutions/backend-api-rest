package com.azenithsolutions.backendapirest.v2.infrastructure.producer;

import com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.order.OrderDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateOrderCommandPublisher {
    private final RabbitTemplate rabbitTemplate;

    @Value("${broker.order.command.exchange}")
    private String orderExchange;

    @Value("${broker.order.command.routing-key}")
    private String orderRoutingKey;

    public void publish(OrderDTO order) {
        try {
            System.out.println("Publishing create order command for order: " + order.getCodigo());
            rabbitTemplate.convertAndSend(orderExchange, orderRoutingKey, order);
            System.out.println("Order command published successfully for order: " + order.getCodigo());
        } catch (Exception e) {
            System.out.println("Error publishing create order command for order: " + order.getCodigo() + e);
            throw new RuntimeException("Failed to publish create order command", e);
        }
    }
}
