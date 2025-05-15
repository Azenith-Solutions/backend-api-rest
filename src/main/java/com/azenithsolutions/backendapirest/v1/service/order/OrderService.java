package com.azenithsolutions.backendapirest.v1.service.order;

import com.azenithsolutions.backendapirest.v1.dto.order.OrderCreateDTO;
import com.azenithsolutions.backendapirest.v1.dto.order.OrderRequestDTO;
import com.azenithsolutions.backendapirest.v1.model.Order;
import com.azenithsolutions.backendapirest.v1.repository.ItemRepository;
import com.azenithsolutions.backendapirest.v1.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ItemRepository itemRepository;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    public Order createOrder(OrderCreateDTO createDTO) {
        Order order = convertCreateDtoToEntity(createDTO);
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
        Order updatedOrder = convertRequestDtoToEntity(orderRequestDTO);

        updatedOrder.setIdPedido(id);
        updatedOrder.setCreatedAt(existingOrder.getCreatedAt());
        updatedOrder.setUpdatedAt(LocalDateTime.now());

        return orderRepository.save(updatedOrder);
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    private Order convertCreateDtoToEntity(OrderCreateDTO dto) {
        Order order = new Order();

        // Não define ID, pois será gerado pelo banco
        order.setCodigo(dto.getCodigo());
        order.setNomeComprador(dto.getNomeComprador());
        order.setEmailComprador(dto.getEmailComprador());
        order.setTelCelular(dto.getTelCelular());
        order.setStatus(dto.getStatus());

        return order;
    }

    private Order convertRequestDtoToEntity(OrderRequestDTO dto) {
        Order order = new Order();

        if (dto.getIdPedido() != null) {
            order.setIdPedido(dto.getIdPedido());
        }

        order.setCodigo((dto.getCodigo()));
        order.setNomeComprador(dto.getNomeComprador());
        order.setEmailComprador(dto.getEmailComprador());
        order.setTelCelular(dto.getTelCelular());
        order.setStatus(dto.getStatus());

        return order;
    }
}
