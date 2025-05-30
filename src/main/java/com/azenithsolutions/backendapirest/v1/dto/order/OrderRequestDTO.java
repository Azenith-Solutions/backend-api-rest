package com.azenithsolutions.backendapirest.v1.dto.order;

import com.azenithsolutions.backendapirest.v1.model.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestDTO {
    private String codigo;
    private String nomeComprador;
    private String emailComprador;
    private String CNPJ;
    private String valor;
    private OrderStatus status;
    private String telCelular;
}
