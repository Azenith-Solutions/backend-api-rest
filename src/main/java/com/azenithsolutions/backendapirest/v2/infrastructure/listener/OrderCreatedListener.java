package com.azenithsolutions.backendapirest.v2.infrastructure.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderCreatedListener {
    @RabbitListener(
        queues = "${broker.order.event.queue}",
        containerFactory = "orderEventListenerContainerFactory"
    )
    public void onMessage(String message) {
        try {
            System.out.println("Listening Created");
            System.out.println("Response Created: " + message);
        } catch (Exception exception) {
            System.out.println("Error Listening Created Order");
            throw exception;
        }
    }
}
