package com.azenithsolutions.backendapirest.v1.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class ComponentRequestDTO {
    @NotBlank(message = "ID não pode estar vazio")
    private String idHardWareTech;

    @NotBlank(message = "Deve haver uma caixa para ser registrada")
    private String caixa;

    @NotBlank(message = "Deve haver um part number para ser registrado")
    private String partNumber;

    @Min(value = 0, message = "Quantidade não pode ser negativa")
    private int quantidade;

    private Boolean flagML;
    private String codigoML;
    private Boolean flagVerificado;
    private String condicao;
    private String observacao;
    private String descricao;
}
