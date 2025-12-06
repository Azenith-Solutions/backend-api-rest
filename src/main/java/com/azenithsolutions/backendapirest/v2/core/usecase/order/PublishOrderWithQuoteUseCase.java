package com.azenithsolutions.backendapirest.v2.core.usecase.order;

import com.azenithsolutions.backendapirest.v2.core.domain.command.order.OrderRequestCommandDTO;
import com.azenithsolutions.backendapirest.v2.core.domain.model.email.QuoteEmailData;
import com.azenithsolutions.backendapirest.v2.core.domain.repository.OrderEmailCacheGateway;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@RequiredArgsConstructor
public class PublishOrderWithQuoteUseCase {
    private final OrderEmailCacheGateway emailCacheGateway;

    public OrderRequestCommandDTO execute(OrderRequestCommandDTO orderCommand, QuoteEmailData emailData) {
        try {
            String orderCode = orderCommand.codigo();
            if (orderCode == null || orderCode.isEmpty()) {
                orderCode = generateOrderCode();
                orderCommand = new OrderRequestCommandDTO(
                    orderCode,
                    orderCommand.nomeComprador(),
                    orderCommand.emailComprador(),
                    orderCommand.cnpj(),
                    orderCommand.valor(),
                    orderCommand.status(),
                    orderCommand.telCelular(),
                    orderCommand.createdAt(),
                    orderCommand.updatedAt()
                );
            }
            
            emailData.setQuoteId(orderCode);
            
            if (emailData.getCurrentDate() == null || emailData.getCurrentDate().isEmpty()) {
                emailData.setCurrentDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            }
            if (emailData.getCurrentTime() == null || emailData.getCurrentTime().isEmpty()) {
                emailData.setCurrentTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
            }
            
            emailCacheGateway.save(orderCode, emailData);
            System.out.println("Email data cached for order: " + orderCode);
            
            return orderCommand;
            
        } catch (Exception e) {
            System.err.println("Error in PublishOrderWithQuoteUseCase: " + e.getMessage());
            throw new RuntimeException("Failed to prepare order for publishing", e);
        }
    }

    private String generateOrderCode() {
        int year = LocalDateTime.now().getYear();
        int randomNumber = 1000 + new Random().nextInt(9000); // 1000-9999
        return String.format("AZT-%d-%04d", year, randomNumber);
    }
}
