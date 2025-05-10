package com.azenithsolutions.backendapirest.v1.service.order;

import com.azenithsolutions.backendapirest.v1.dto.order.OrderRequestDTO;
import com.azenithsolutions.backendapirest.v1.model.Company;
import com.azenithsolutions.backendapirest.v1.model.Order;
import com.azenithsolutions.backendapirest.v1.repository.CompanyRepository;
import com.azenithsolutions.backendapirest.v1.repository.ItemRepository;
import com.azenithsolutions.backendapirest.v1.repository.OrderRepository;
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
    private CompanyRepository companyRepository;

    @Autowired
    private ItemRepository itemRepository;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    public Order createOrder(OrderRequestDTO orderRequestDTO) {
        Order order = convertDtoToEntity(orderRequestDTO);

        if (order.getIdPedido() == null) {
            order.setCreatedAt(LocalDateTime.now());
        }

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

        order.setIdPedido(dto.getIdPedido());
        order.setCodigo((dto.getCodigo()));

        Company company = companyRepository.findById(dto.getFkEmpresa()).orElse(null);
        order.setFkEmpresa(company);
        order.setNomeComprador(dto.getNomeComprador());
        order.setEmailComprador(dto.getEmailComprador());
        order.setTelCelular(dto.getTelCelular());
        order.setStatus(dto.getStatus());
        order.setCreatedAt(dto.getCreatedAt());
        order.setUpdatedAt(dto.getUpdatedAt());

        return order;
    }
}
