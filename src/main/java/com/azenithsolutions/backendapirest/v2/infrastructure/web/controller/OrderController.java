package com.azenithsolutions.backendapirest.v2.infrastructure.web.controller;

import com.azenithsolutions.backendapirest.v2.core.domain.model.order.Order;
import com.azenithsolutions.backendapirest.v2.core.usecase.order.*;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.OrderRest;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.shared.ApiResponseDTO;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.mappers.OrderRestMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @GetMapping
    @Operation(summary = "List orders", description = "Returns all orders (v2 clean architecture)")
    public ResponseEntity<ApiResponseDTO<?>> getAll(HttpServletRequest request) {
        try {
            List<OrderRest> orders = list.execute().stream().map(OrderRestMapper::toRest).toList();
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
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Order found"),
        @ApiResponse(responseCode = "404", description = "Order not found", content = @Content(schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<ApiResponseDTO<?>> getById(@PathVariable Long id, HttpServletRequest request) {
        try {
            OrderRest orderRest = OrderRestMapper.toRest(getById.execute(id));

            if (orderRest == null) {
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
                                    orderRest,
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
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Order created"),
        @ApiResponse(responseCode = "400", description = "Validation error", content = @Content(schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<ApiResponseDTO<?>> create(@Valid @RequestBody OrderRest orderRest, HttpServletRequest httpServletRequest) {
        try{
            Order created = create.execute(OrderRestMapper.toDomain(orderRest));

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
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Order updated"),
        @ApiResponse(responseCode = "404", description = "Order not found", content = @Content(schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody OrderRest orderRest, HttpServletRequest httpServletRequest) {
        try {
            Order updated = update.execute(id, OrderRestMapper.toDomain(orderRest));

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
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Order deleted"),
        @ApiResponse(responseCode = "404", description = "Order not found", content = @Content(schema = @Schema(implementation = String.class)))
    })
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
}
