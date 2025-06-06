package com.azenithsolutions.backendapirest.v1.dto.component;

import com.azenithsolutions.backendapirest.v1.model.enums.ComponentCondition;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComponentRequestDTO {
    @NotBlank(message = "ID não pode estar vazio")
    private String idHardWareTech;

    @NotBlank(message = "O componente deve ter nome")
    private String nomeComponente;

    @NotNull(message = "Deve haver uma caixa para ser registrada")
    private Long fkCaixa;

    private Long fkCategoria;

    @NotBlank(message = "Deve haver um part number para ser registrado")
    private String partNumber;

    @Min(value = 0, message = "Quantidade não pode ser negativa")
    private int quantidade;

    private Boolean flagML;
    private String codigoML;
    private Boolean flagVerificado;
    private Boolean isVisibleCatalog;
    private ComponentCondition condicao;
    private String observacao;
    private String descricao;
    private String imagem;
}
