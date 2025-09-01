package com.azenithsolutions.backendapirest.v2.infrastructure.web.dto;

import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class EmailBudgetRest {
    @NotBlank
    @Schema(example = "azenithsolutions@gmail.com")
    private String toEmail;

    @NotBlank
    @Schema(example = "Kauan")
    private String toName;

    @NotBlank
    @Schema(example = "Orçamento de Componente")
    private String subject;

    @NotBlank
    @Schema(example = "Olá, preciso de um orçamento para os seguintes componentes industriais:\n- Motor trifásico 2HP WEG (quantidade: 3)\n- Sensor indutivo M12 PNP NO (quantidade: 10)\n- CLP Siemens S7-1200 CPU 1212C (quantidade: 1)\n- Inversor de frequência 2CV 220V (quantidade: 2)\n- Contator 9A bobina 24VCA (quantidade: 5)\nFavor retornar com preços unitários, prazo de entrega e condições de pagamento.\nObrigado.")
    private String content;
}
