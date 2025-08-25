package com.azenithsolutions.backendapirest.v2.infrastructure.web.controller;

import com.azenithsolutions.backendapirest.v2.core.domain.model.Order;
import com.azenithsolutions.backendapirest.v2.core.usecase.order.*;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.OrderRest;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.mappers.OrderRestMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<List<OrderRest>> getAll() {
        List<OrderRest> orders = list.execute().stream().map(OrderRestMapper::toRest).toList();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get order by id", description = "Returns one order if it exists")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Order found"),
        @ApiResponse(responseCode = "404", description = "Order not found", content = @Content(schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            OrderRest orderRest = OrderRestMapper.toRest(getById.execute(id));

            return ResponseEntity.ok(orderRest);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping
    @Operation(summary = "Create order", description = "Creates a new order")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Order created"),
        @ApiResponse(responseCode = "400", description = "Validation error", content = @Content(schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<OrderRest> create(@Valid @RequestBody OrderRest orderRest) {
        Order created = create.execute(OrderRestMapper.toDomain(orderRest));

        return ResponseEntity.status(HttpStatus.CREATED).body(OrderRestMapper.toRest(created));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update order", description = "Updates an existing order")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Order updated"),
        @ApiResponse(responseCode = "404", description = "Order not found", content = @Content(schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody OrderRest orderRest) {
        try {
            Order updated = update.execute(id, OrderRestMapper.toDomain(orderRest));

            return ResponseEntity.ok(OrderRestMapper.toRest(updated));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete order", description = "Deletes an order by id")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Order deleted"),
        @ApiResponse(responseCode = "404", description = "Order not found", content = @Content(schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            delete.execute(id);
            return ResponseEntity.ok().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
