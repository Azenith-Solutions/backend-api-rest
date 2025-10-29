package com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.order;

import com.azenithsolutions.backendapirest.v2.core.domain.model.enums.OrderStatus;

import java.time.LocalDateTime;

public record OrderRequestDTO(
        String codigo,
        String nomeComprador,
        String emailComprador,
        String cnpj,
        String valor,
        OrderStatus status,
        String telCelular,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
