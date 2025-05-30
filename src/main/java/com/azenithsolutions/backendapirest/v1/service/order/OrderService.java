package com.azenithsolutions.backendapirest.v1.service.order;

import com.azenithsolutions.backendapirest.v1.dto.order.OrderRequestDTO;
import com.azenithsolutions.backendapirest.v1.model.Order;
import com.azenithsolutions.backendapirest.v1.repository.ItemRepository;
import com.azenithsolutions.backendapirest.v1.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    public Order createOrder(OrderRequestDTO orderRequestDTO) {
        Order order = convertDtoToEntity(orderRequestDTO);
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        return orderRepository.save(order);
    }

    public Order updateOrder(Long id, OrderRequestDTO orderRequestDTO) {
        Optional<Order> existingOrderOpt = orderRepository.findById(id);

        if (existingOrderOpt.isEmpty()) {
            return null;
        }

        Order existingOrder = existingOrderOpt.get();
        Order updatedOrder = convertDtoToEntity(orderRequestDTO);

        updatedOrder.setIdPedido(id);
        updatedOrder.setCreatedAt(existingOrder.getCreatedAt());
        updatedOrder.setUpdatedAt(LocalDateTime.now());

        return orderRepository.save(updatedOrder);
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    private Order convertDtoToEntity(OrderRequestDTO dto) {
        Order order = new Order();

        order.setCodigo(dto.getCodigo());
        order.setNomeComprador(dto.getNomeComprador());
        order.setEmailComprador(dto.getEmailComprador());

        if (dto.getCNPJ() != null) {
            order.setCNPJ(dto.getCNPJ());
        }

        order.setStatus(dto.getStatus());
        order.setDDD(dto.getDDD());
        order.setTelCelular(dto.getTelCelular());
        order.setValor(dto.getValor());

        return order;
    }
}
