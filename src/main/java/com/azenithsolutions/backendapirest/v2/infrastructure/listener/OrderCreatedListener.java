package com.azenithsolutions.backendapirest.v2.infrastructure.listener;

import com.azenithsolutions.backendapirest.v2.core.domain.model.email.EmailBudget;
import com.azenithsolutions.backendapirest.v2.core.domain.model.email.EmailTemplate;
import com.azenithsolutions.backendapirest.v2.core.domain.model.email.QuoteEmailData;
import com.azenithsolutions.backendapirest.v2.core.domain.model.email.QuoteEmailTemplateBuilder;
import com.azenithsolutions.backendapirest.v2.core.domain.repository.OrderEmailCacheGateway;
import com.azenithsolutions.backendapirest.v2.core.usecase.email.SendEmailUseCase;
import com.azenithsolutions.backendapirest.v2.core.usecase.item.CreateItemUseCase;
import com.azenithsolutions.backendapirest.v2.core.usecase.item.command.ItemCreateCommand;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.order.OrderDTO;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.mappers.ItemRestMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OrderCreatedListener {
    private final CreateItemUseCase createItem;
    private final OrderEmailCacheGateway emailCacheGateway;
    private final SendEmailUseCase sendEmailUseCase;
    private final QuoteEmailTemplateBuilder templateBuilder;

    @RabbitListener(
        queues = "${broker.order.event.queue}",
        containerFactory = "orderEventListenerContainerFactory"
    )
    public void onOrderCreatedEvent(OrderDTO order) {
        try {
            System.out.println("=== ORDER CREATED EVENT RECEIVED ===");
            System.out.println("Order Code: " + order.getCodigo());

            List<ItemCreateCommand> items = ItemRestMapper.toCommands(order.getItems());
            createItem.execute(items);
            System.out.println("Items created successfully");

            String orderCode = order.getCodigo();
            Optional<QuoteEmailData> emailDataOpt = emailCacheGateway.findByOrderCode(orderCode);
            
            if (emailDataOpt.isEmpty()) {
                System.err.println("WARNING: Email data not found in cache for order: " + orderCode);
                return;
            }

            QuoteEmailData emailData = emailDataOpt.get();
            System.out.println("Email data retrieved from cache for order: " + orderCode);

            sendQuoteEmail(emailData);
            
            emailCacheGateway.delete(orderCode);
            System.out.println("Email sent and cache cleared for order: " + orderCode);
            
        } catch (Exception exception) {
            System.err.println("ERROR processing order created event: " + exception.getMessage());
            exception.printStackTrace();
            throw exception;
        }
    }
    
    private void sendQuoteEmail(QuoteEmailData emailData) {
        try {
            EmailTemplate template = templateBuilder.buildQuoteEmailTemplate(emailData);
            
            EmailBudget emailBudget = new EmailBudget(
                emailData.getEmail(),
                emailData.getName(),
                "Cotação de Pedido - " + emailData.getQuoteId(),
                template.getHtmlContent()
            );
            
            String result = sendEmailUseCase.execute(emailBudget);
            System.out.println("Email sent result: " + result);
            
        } catch (Exception e) {
            System.err.println("ERROR sending quote email: " + e.getMessage());
            throw new RuntimeException("Failed to send quote email", e);
        }
    }
}
