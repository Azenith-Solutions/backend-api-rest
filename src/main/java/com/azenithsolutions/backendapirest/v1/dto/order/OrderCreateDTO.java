package com.azenithsolutions.backendapirest.v1.dto.order;

import com.azenithsolutions.backendapirest.v1.model.enums.StatusPedido;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreateDTO {
    private String codigo;
    private Long fkEmpresa;
    private String nomeComprador;
    private String emailComprador;
    private String telCelular;
    private StatusPedido status;
}
