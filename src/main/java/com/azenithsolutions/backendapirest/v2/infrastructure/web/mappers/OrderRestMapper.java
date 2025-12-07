package com.azenithsolutions.backendapirest.v2.infrastructure.web.mappers;

import com.azenithsolutions.backendapirest.v2.core.domain.command.order.OrderRequestCommandDTO;
import com.azenithsolutions.backendapirest.v2.core.domain.model.order.Order;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.order.OrderDTO;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.order.OrderRequestDTO;

public class OrderRestMapper {
    public static OrderDTO toDto(Order domain) {
        if (domain == null) return null;

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setIdPedido(domain.getIdPedido());
        orderDTO.setCodigo(domain.getCodigo());
        orderDTO.setNomeComprador(domain.getNomeComprador());
        orderDTO.setEmailComprador(domain.getEmailComprador());
        orderDTO.setCnpj(domain.getCnpj());
        orderDTO.setValor(domain.getValor());
        orderDTO.setStatus(domain.getStatus());
        orderDTO.setTelCelular(domain.getTelCelular());
        orderDTO.setCreatedAt(domain.getCreatedAt());
        orderDTO.setUpdatedAt(domain.getUpdatedAt());

        return orderDTO;
    }
    
    public static OrderDTO commandToDto(OrderRequestCommandDTO command) {
        if (command == null) return null;

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setCodigo(command.codigo());
        orderDTO.setNomeComprador(command.nomeComprador());
        orderDTO.setEmailComprador(command.emailComprador());
        orderDTO.setCnpj(command.cnpj());
        orderDTO.setValor(command   .valor());
        orderDTO.setStatus(command.status());
        orderDTO.setTelCelular(command.telCelular());
        orderDTO.setItems(command.items());
        orderDTO.setCreatedAt(command.createdAt());
        orderDTO.setUpdatedAt(command.updatedAt());

        return orderDTO;
    }

    public static Order toDomain(OrderDTO rest) {
        if (rest == null) return null;

        Order domain = new Order();
        domain.setIdPedido(rest.getIdPedido());
        domain.setCodigo(rest.getCodigo());
        domain.setNomeComprador(rest.getNomeComprador());
        domain.setEmailComprador(rest.getEmailComprador());
        domain.setCnpj(rest.getCnpj());
        domain.setValor(rest.getValor());
        domain.setStatus(rest.getStatus());
        domain.setTelCelular(rest.getTelCelular());
        domain.setCreatedAt(rest.getCreatedAt());
        domain.setUpdatedAt(rest.getUpdatedAt());

        return domain;
    }

    public static OrderRequestCommandDTO toCommand(OrderRequestDTO rest) {
        if (rest == null) return null;

        return new OrderRequestCommandDTO(
                rest.codigo(),
                rest.nomeComprador(),
                rest.emailComprador(),
                rest.cnpj(),
                rest.valor(),
                rest.status(),
                rest.telCelular(),
                rest.items(),
                rest.createdAt(),
                rest.updatedAt()
        );
    }
}
