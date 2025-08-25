package com.azenithsolutions.backendapirest.v2.infrastructure.web.mappers;

import com.azenithsolutions.backendapirest.v2.core.domain.model.Order;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.OrderRest;

public class OrderRestMapper {
    public static OrderRest toRest(Order domain) {
        if (domain == null) return null;

        OrderRest orderRest = new OrderRest();
        orderRest.setId(domain.getId());
        orderRest.setCodigo(domain.getCodigo());
        orderRest.setNomeComprador(domain.getNomeComprador());
        orderRest.setEmailComprador(domain.getEmailComprador());
        orderRest.setCnpj(domain.getCnpj());
        orderRest.setValor(domain.getValor());
        orderRest.setStatus(domain.getStatus());
        orderRest.setTelCelular(domain.getTelCelular());
        orderRest.setCreatedAt(domain.getCreatedAt());
        orderRest.setUpdatedAt(domain.getUpdatedAt());

        return orderRest;
    }

    public static Order toDomain(OrderRest rest) {
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
}
