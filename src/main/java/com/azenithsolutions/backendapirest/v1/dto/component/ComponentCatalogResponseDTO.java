package com.azenithsolutions.backendapirest.v1.dto.component;

import com.azenithsolutions.backendapirest.v1.model.Category;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComponentCatalogResponseDTO {
    private Long idComponente;

    private Category categoria;

    private int quantidade;

    private String descricao;
}
