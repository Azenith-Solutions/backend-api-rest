package com.azenithsolutions.backendapirest.v1.controller.order;

import com.azenithsolutions.backendapirest.v1.dto.order.OrderCreateDTO;
import com.azenithsolutions.backendapirest.v1.dto.order.OrderRequestDTO;
import com.azenithsolutions.backendapirest.v1.dto.shared.ApiResponseDTO;
import com.azenithsolutions.backendapirest.v1.model.Order;
import com.azenithsolutions.backendapirest.v1.service.order.OrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "Order Management - v1", description = "Endpoints to manage orders")
@RestController
@RequestMapping("/v1/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping
    public ResponseEntity<ApiResponseDTO<?>> getAllOrders(HttpServletRequest request) {
        try {
            List<Order> orders = orderService.getAllOrders();

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
    public ResponseEntity<ApiResponseDTO<?>> getOrderById(HttpServletRequest request, @PathVariable Long id) {
        try {
            Order orderWithId = orderService.getOrderById(id).orElse(null);

            if (orderWithId == null) {
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
                                    orderWithId,
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

    @PostMapping
    public ResponseEntity<ApiResponseDTO<?>> createOrder(@Valid @RequestBody OrderCreateDTO orderCreateDTO, HttpServletRequest httpServletRequest) {
        try {
            Order order = orderService.createOrder(orderCreateDTO);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(
                            new ApiResponseDTO<>(
                                    LocalDateTime.now(),
                                    HttpStatus.CREATED.value(),
                                    "Pedido criado com sucesso!",
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
                                    "Erro interno: " + e.getMessage(),
                                    null,
                                    httpServletRequest.getRequestURI()
                            )
                    );
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<?>> updateOrder(@PathVariable Long id, @Valid @RequestBody OrderRequestDTO orderRequestDTO, HttpServletRequest httpServletRequest) {
        try {
            Order updatedOrder = orderService.updateOrder(id, orderRequestDTO);

            if (updatedOrder == null) {
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
                                    updatedOrder,
                                    httpServletRequest.getRequestURI()
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
                                    httpServletRequest.getRequestURI()
                            )
                    );
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<?>> deleteOrder(@PathVariable Long id, HttpServletRequest httpServletRequest) {
        try {
            Order existingOrder = orderService.getOrderById(id).orElse(null);

            if (existingOrder == null) {
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

            orderService.deleteOrder(id);

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
        } catch (Exception e) {
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
