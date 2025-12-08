package com.azenithsolutions.backendapirest.v2.infrastructure.web.controller;

import com.azenithsolutions.backendapirest.v2.core.domain.command.order.OrderRequestCommandDTO;
import com.azenithsolutions.backendapirest.v2.core.domain.model.email.QuoteEmailData;
import com.azenithsolutions.backendapirest.v2.core.domain.model.order.Order;
import com.azenithsolutions.backendapirest.v2.core.usecase.order.*;
import com.azenithsolutions.backendapirest.v2.infrastructure.producer.CreateOrderCommandPublisher;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.order.OrderDTO;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.order.OrderRequestDTO;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.order.OrderWithQuoteRequestDTO;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.shared.ApiResponseDTO;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.mappers.OrderRestMapper;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.mappers.QuoteEmailRestMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@RestController("orderControllerV2")
@RequestMapping("/v2/orders")
@Tag(name = "Order Management - v2", description = "Clean architecture endpoint for Orders")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class OrderController {
    private final CreateOrderUseCase create;
    private final UpdateOrderUseCase update;
    private final GetOrderByIdUseCase getById;
    private final ListOrdersUseCase list;
    private final DeleteOrderUseCase delete;
    private final CreateOrderCommandPublisher createOrderCommandPublisher;
    private final PublishOrderWithQuoteUseCase publishOrderWithQuote;

    @GetMapping
    @Operation(summary = "List orders", description = "Returns all orders (v2 clean architecture)")
    public ResponseEntity<ApiResponseDTO<?>> getAll(HttpServletRequest request) {
        try {
            List<OrderDTO> orders = list.execute().stream().map(OrderRestMapper::toDto).toList();
            return ResponseEntity.status(HttpStatus.OK)
                    .body(
                            new ApiResponseDTO<>(
                                    LocalDateTime.now(),
                                    HttpStatus.OK.value(),
                                    "OK",
                                    orders,
                                    request.getRequestURI()
                            )
                    );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            new ApiResponseDTO<>(
                                    LocalDateTime.now(),
                                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                    "Erro interno: " + e.getMessage(),
                                    null,
                                    request.getRequestURI()
                            )
                    );
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get order by id", description = "Returns one order if it exists")
    public ResponseEntity<ApiResponseDTO<?>> getById(@PathVariable Long id, HttpServletRequest request) {
        try {
            OrderDTO orderDTO = OrderRestMapper.toDto(getById.execute(id));

            if (orderDTO == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(
                                new ApiResponseDTO<>(
                                        LocalDateTime.now(),
                                        HttpStatus.NOT_FOUND.value(),
                                        "Pedido não encontrado!",
                                        null,
                                        request.getRequestURI()
                                )
                        );
            }

            return ResponseEntity.status(HttpStatus.OK)
                    .body(
                            new ApiResponseDTO<>(
                                    LocalDateTime.now(),
                                    HttpStatus.OK.value(),
                                    "OK",
                                    orderDTO,
                                    request.getRequestURI()
                            )
                    );
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            new ApiResponseDTO<>(
                                    LocalDateTime.now(),
                                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                    "Erro interno: " + e.getMessage(),
                                    null,
                                    request.getRequestURI()
                            )
                    );
        }
    }

    @PostMapping
    @Operation(summary = "Create order", description = "Creates a new order")
    public ResponseEntity<ApiResponseDTO<?>> create(@Valid @RequestBody OrderRequestDTO order, HttpServletRequest httpServletRequest) {
        try{
            Order created = create.execute(OrderRestMapper.toCommand(order));

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(
                            new ApiResponseDTO<>(
                                    LocalDateTime.now(),
                                    HttpStatus.CREATED.value(),
                                    "Pedido criado com sucesso!",
                                    created,
                                    httpServletRequest.getRequestURI()
                            )
                    );
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            new ApiResponseDTO<>(
                                    LocalDateTime.now(),
                                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                    "Erro interno: " + e.getMessage(),
                                    null,
                                    httpServletRequest.getRequestURI()
                            )
                    );
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update order", description = "Updates an existing order")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody OrderRequestDTO order, HttpServletRequest httpServletRequest) {
        try {
            Order updated = update.execute(id, OrderRestMapper.toCommand(order));

            if (updated == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(
                                new ApiResponseDTO<>(
                                        LocalDateTime.now(),
                                        HttpStatus.NOT_FOUND.value(),
                                        "Pedido não encontrado!",
                                        null,
                                        httpServletRequest.getRequestURI()
                                )
                        );
            }

            return ResponseEntity.status(HttpStatus.OK)
                    .body(
                            new ApiResponseDTO<>(
                                    LocalDateTime.now(),
                                    HttpStatus.OK.value(),
                                    "Pedido atualizado com sucesso!",
                                    updated,
                                    httpServletRequest.getRequestURI()
                            )
                    );
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            new ApiResponseDTO<>(
                                    LocalDateTime.now(),
                                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                    "Erro interno: " + e.getMessage(),
                                    null,
                                    httpServletRequest.getRequestURI()
                            )
                    );
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete order", description = "Deletes an order by id")
    public ResponseEntity<?> delete(@PathVariable Long id, HttpServletRequest httpServletRequest) {
        try {
            delete.execute(id);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(
                            new ApiResponseDTO<>(
                                    LocalDateTime.now(),
                                    HttpStatus.OK.value(),
                                    "Pedido deletado com sucesso!",
                                    null,
                                    httpServletRequest.getRequestURI()
                            )
                    );
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            new ApiResponseDTO<>(
                                    LocalDateTime.now(),
                                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                    "Erro interno: " + e.getMessage(),
                                    null,
                                    httpServletRequest.getRequestURI()
                            )
                    );
        }
    }

    @PostMapping("/publish")
    @Operation(summary = "Publish create order command", description = "Publishes a create order command to RabbitMQ")
    public ResponseEntity<ApiResponseDTO<?>> publishCreateOrderCommand(@Valid @RequestBody OrderDTO order, HttpServletRequest httpServletRequest) {
        try {
            createOrderCommandPublisher.publish(order);

            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .body(
                            new ApiResponseDTO<>(
                                    LocalDateTime.now(),
                                    HttpStatus.ACCEPTED.value(),
                                    "Comando de criação de pedido publicado com sucesso!",
                                    order,
                                    httpServletRequest.getRequestURI()
                            )
                    );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            new ApiResponseDTO<>(
                                    LocalDateTime.now(),
                                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                    "Erro ao publicar comando: " + e.getMessage(),
                                    null,
                                    httpServletRequest.getRequestURI()
                            )
                    );
        }
    }
    
    @PostMapping("/publish-with-quote")
    @Operation(
        summary = "Publish order with quote email caching", 
        description = "Caches email data in Redis and publishes order creation command to RabbitMQ. " +
                     "The email will be sent automatically when the order creation event is received from the microservice."
    )
    public ResponseEntity<ApiResponseDTO<?>> publishOrderWithQuote(
            @Valid @RequestBody OrderWithQuoteRequestDTO request, 
            HttpServletRequest httpServletRequest) {
        try {
            OrderRequestCommandDTO orderCommand = OrderRestMapper.toCommand(request.getOrder());
            QuoteEmailData emailData = QuoteEmailRestMapper.toDomain(request.getEmailData());
            OrderRequestCommandDTO enrichedCommand = publishOrderWithQuote.execute(orderCommand, emailData);
            
            OrderDTO orderDTO = OrderRestMapper.commandToDto(enrichedCommand);
            createOrderCommandPublisher.publish(orderDTO);
            
            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .body(
                            new ApiResponseDTO<>(
                                    LocalDateTime.now(),
                                    HttpStatus.ACCEPTED.value(),
                                    "Pedido publicado e dados de email cacheados com sucesso! O email será enviado após confirmação do microserviço.",
                                    orderDTO,
                                    httpServletRequest.getRequestURI()
                            )
                    );
        } catch (Exception e) {
            System.err.println("Error in publishOrderWithQuote: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            new ApiResponseDTO<>(
                                    LocalDateTime.now(),
                                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                    "Erro ao publicar pedido com cotação: " + e.getMessage(),
                                    null,
                                    httpServletRequest.getRequestURI()
                            )
                    );
        }
    }
}
