package com.azenithsolutions.backendapirest.v1.dto.order;

import com.azenithsolutions.backendapirest.v1.model.Item;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestDTO {
    @NotNull(message = "ID do pedido não pode ser vazio!")
    private Long idPedido;
    private String codigo;
    private Long fkEmpresa;
    private String nomeComprador;
    private String emailComprador;
    private String telCelular;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
