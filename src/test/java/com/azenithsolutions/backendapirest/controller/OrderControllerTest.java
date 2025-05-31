package com.azenithsolutions.backendapirest.controller;

import com.azenithsolutions.backendapirest.v1.controller.order.OrderController;
import com.azenithsolutions.backendapirest.v1.dto.order.OrderRequestDTO;
import com.azenithsolutions.backendapirest.v1.dto.shared.ApiResponseDTO;
import com.azenithsolutions.backendapirest.v1.model.Order;
import com.azenithsolutions.backendapirest.v1.model.enums.OrderStatus;
import com.azenithsolutions.backendapirest.v1.service.order.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private OrderController orderController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetOrder_ReturnSuccess() {
        Order order1 = new Order(1L, "PED456",null,"Mariana", "mariana@example.com", "98765432000188",
                "250.00", OrderStatus.EM_ANALISE, "912345678",
                LocalDateTime.now(), LocalDateTime.now());

        Order order2 = new Order(2L, "PED456",null,"Kauan", "kauan@example.com", "98765432000188",
                "100.00", OrderStatus.CONCLUIDO, "912345678",
                LocalDateTime.now(), LocalDateTime.now());

        when(orderService.getAllOrders()).thenReturn(List.of(order1, order2));
        when(request.getRequestURI()).thenReturn("/orders");

        // Act
        ResponseEntity<ApiResponseDTO<?>> response = orderController.getAllOrders(request);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        ApiResponseDTO<?> body = response.getBody();
        assertNotNull(body);
        assertEquals("OK", body.getMessage());
        assertTrue(body.getData() instanceof List<?>);

        List<?> data = (List<?>) body.getData();
        assertEquals(2, data.size());
    }

    @Test
    public void testCreateOrder_ReturnSuccess() {
        // Arrange
        OrderRequestDTO requestDTO = new OrderRequestDTO();
        requestDTO.setCodigo("PED123");
        requestDTO.setNomeComprador("João");
        requestDTO.setEmailComprador("joao@example.com");
        requestDTO.setCNPJ("12345678000199");
        requestDTO.setValor("500.00");
        requestDTO.setStatus(OrderStatus.EM_ANALISE);
        requestDTO.setTelCelular("912345678");

        Order mockOrder = new Order();
        mockOrder.setIdPedido(1L);
        mockOrder.setCodigo(requestDTO.getCodigo());
        mockOrder.setNomeComprador(requestDTO.getNomeComprador());
        mockOrder.setEmailComprador(requestDTO.getEmailComprador());
        mockOrder.setCNPJ(requestDTO.getCNPJ());
        mockOrder.setValor(requestDTO.getValor());
        mockOrder.setStatus(requestDTO.getStatus());
        mockOrder.setTelCelular(requestDTO.getTelCelular());
        mockOrder.setCreatedAt(LocalDateTime.now());
        mockOrder.setUpdatedAt(LocalDateTime.now());

        when(orderService.createOrder(requestDTO)).thenReturn(mockOrder);
        when(request.getRequestURI()).thenReturn("/orders");

        // Act
        ResponseEntity<ApiResponseDTO<?>> response = orderController.createOrder(requestDTO, request);

        // Assert
        assertEquals(HttpStatus.CREATED.value(), response.getStatusCodeValue());
        ApiResponseDTO<?> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("Pedido criado com sucesso!", responseBody.getMessage());
        assertEquals("/orders", responseBody.getPath());
        assertEquals(HttpStatus.CREATED.value(), responseBody.getStatus());
        assertTrue(responseBody.getData() instanceof Order);

        Order createdOrder = (Order) responseBody.getData();
        assertEquals("PED123", createdOrder.getCodigo());
        assertEquals("João", createdOrder.getNomeComprador());
    }

    @Test
    public void testCreateOrder_ReturnBadRequestDueToInvalidInput() {
        // Arrange
        OrderRequestDTO requestDTO = new OrderRequestDTO();
        requestDTO.setCodigo(null); // Simulando dado inválido
        requestDTO.setNomeComprador("Carlos");
        requestDTO.setEmailComprador("carlos@example.com");
        requestDTO.setCNPJ("12345678000199");
        requestDTO.setValor("0.00");
        requestDTO.setStatus(OrderStatus.EM_ANALISE);
        requestDTO.setTelCelular("912345678");

        when(orderService.createOrder(requestDTO))
                .thenThrow(new IllegalArgumentException("Código do pedido é obrigatório"));
        when(request.getRequestURI()).thenReturn("/orders");

        // Act
        ResponseEntity<ApiResponseDTO<?>> response = orderController.createOrder(requestDTO, request);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatusCodeValue());
        ApiResponseDTO<?> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertTrue(responseBody.getMessage().contains("Erro interno"));
        assertTrue(responseBody.getMessage().contains("Código do pedido é obrigatório"));
        assertEquals("/orders", responseBody.getPath());
    }

    @Test
    public void testUpdateOrder_ReturnSuccess() {
        // Arrange
        Long orderId = 1L;

        OrderRequestDTO requestDTO = new OrderRequestDTO();
        requestDTO.setCodigo("PED999");
        requestDTO.setNomeComprador("Fernanda");
        requestDTO.setEmailComprador("fernanda@example.com");
        requestDTO.setCNPJ("98765432000177");
        requestDTO.setValor("300.00");
        requestDTO.setStatus(OrderStatus.CONCLUIDO);
        requestDTO.setTelCelular("998765432");

        Order updatedOrder = new Order();
        updatedOrder.setIdPedido(orderId);
        updatedOrder.setCodigo(requestDTO.getCodigo());
        updatedOrder.setNomeComprador(requestDTO.getNomeComprador());
        updatedOrder.setEmailComprador(requestDTO.getEmailComprador());
        updatedOrder.setCNPJ(requestDTO.getCNPJ());
        updatedOrder.setValor(requestDTO.getValor());
        updatedOrder.setStatus(requestDTO.getStatus());
        updatedOrder.setTelCelular(requestDTO.getTelCelular());
        updatedOrder.setCreatedAt(LocalDateTime.now().minusDays(1));
        updatedOrder.setUpdatedAt(LocalDateTime.now());

        when(orderService.updateOrder(orderId, requestDTO)).thenReturn(updatedOrder);
        when(request.getRequestURI()).thenReturn("/orders/" + orderId);

        // Act
        ResponseEntity<ApiResponseDTO<?>> response = orderController.updateOrder(orderId, requestDTO, request);

        // Assert
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        ApiResponseDTO<?> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("Pedido atualizado com sucesso!", responseBody.getMessage());
        assertTrue(responseBody.getData() instanceof Order);
        assertEquals(orderId, ((Order) responseBody.getData()).getIdPedido());
    }

    @Test
    public void testDeleteOrder_ReturnSuccess() {
        // Arrange
        Long orderId = 1L;

        Order existingOrder = new Order();
        existingOrder.setIdPedido(orderId);

        when(orderService.getOrderById(orderId)).thenReturn(Optional.of(existingOrder));
        when(request.getRequestURI()).thenReturn("/orders/" + orderId);

        // Act
        ResponseEntity<ApiResponseDTO<?>> response = orderController.deleteOrder(orderId, request);

        // Assert
        assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
        ApiResponseDTO<?> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("Pedido deletado com sucesso!", responseBody.getMessage());
        assertNull(responseBody.getData());
    }

}
