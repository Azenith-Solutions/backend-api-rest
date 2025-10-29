package com.azenithsolutions.backendapirest.v2.infrastructure.web.mappers;

import com.azenithsolutions.backendapirest.v2.core.domain.command.order.OrderRequestCommandDTO;
import com.azenithsolutions.backendapirest.v2.core.domain.model.order.Order;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.order.OrderDTO;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.order.OrderRequestDTO;

public class OrderRestMapper {
    public static OrderDTO toDto(Order domain) {
        if (domain == null) return null;

        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(domain.getId());
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

    public static Order toDomain(OrderDTO rest) {
        if (rest == null) return null;

        Order domain = new Order();
        domain.setId(rest.getId());
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
                rest.createdAt(),
                rest.updatedAt()
        );
    }
}
