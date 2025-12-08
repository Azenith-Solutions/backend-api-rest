package com.azenithsolutions.backendapirest.v2.core.domain.command.order;

import com.azenithsolutions.backendapirest.v2.core.domain.model.enums.OrderStatus;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.item.ItemRequestDTO;

import java.time.LocalDateTime;
import java.util.List;

public record OrderRequestCommandDTO(
        String codigo,
        String nomeComprador,
        String emailComprador,
        String cnpj,
        String valor,
        OrderStatus status,
        String telCelular,
        List<ItemRequestDTO> items,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}