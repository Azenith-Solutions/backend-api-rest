package com.azenithsolutions.backendapirest.v1.dto.item;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestDTO {
    @NotNull(message = "Deve haver um componente para ser registrado")
    private Long fkComponente;

    @NotNull(message = "Deve haver um pedido para ser registrado")
    private Long fkPedido;

    @Min(value = 1, message = "Quantidade deve ser pelo menos 1")
    private Integer quantidadeCarrinho;
}
