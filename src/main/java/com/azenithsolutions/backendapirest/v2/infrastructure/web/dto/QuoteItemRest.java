package com.azenithsolutions.backendapirest.v2.infrastructure.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class QuoteItemRest {
    @NotBlank
    @Schema(example = "Resistor 10kΩ")
    private String nomeComponente;
    
    @Schema(example = "100")
    private Integer quantidadeCarrinho;
    
    @Schema(example = "Resistor de precisão 1% 1/4W")
    private String descricao;
}
