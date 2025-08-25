package com.azenithsolutions.backendapirest.v2.infrastructure.web.dto;

import com.azenithsolutions.backendapirest.v2.core.domain.model.enums.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(name = "Order", description = "Order representation for API v2")
public class OrderRest {
    @Schema(description = "Unique identifier", example = "123")
    private Long id;
    @Schema(description = "Order code", example = "ORD-2025-0001")
    private String codigo;
    @Schema(description = "Buyer name", example = "Joao Silva")
    private String nomeComprador;
    @Schema(description = "Buyer email", example = "joao.silva@example.com")
    private String emailComprador;
    @Schema(description = "Buyer CNPJ", example = "12.345.678/0001-99")
    private String cnpj;
    @Schema(description = "Order value", example = "1500.00")
    private String valor;
    @Schema(description = "Current status", example = "PENDENTE")
    private OrderStatus status;
    @Schema(description = "Buyer phone", example = "+55 11 98877-6655")
    private String telCelular;
    @Schema(description = "Creation timestamp", example = "2025-05-25T10:15:30")
    private LocalDateTime createdAt;
    @Schema(description = "Last update timestamp", example = "2025-05-26T12:00:00")
    private LocalDateTime updatedAt;
}
