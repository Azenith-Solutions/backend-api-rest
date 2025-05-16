package com.azenithsolutions.backendapirest.v1.dto.item;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestDTO {
    @NotBlank(message = "Deve haver um componente para ser registrado")
    private Long fkComponente;

    @NotBlank(message = "Deve haver um pedido para ser registrado")
    private Long fkPedido;
    private Integer quantidade;
}
