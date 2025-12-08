package com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.components;

import com.azenithsolutions.backendapirest.v2.infrastructure.persistence.jpa.entity.CategoryEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComponentCatalogResponseDTO {
    private Long idComponente;

    private String nomeComponente;

    private CategoryEntity categoria;

    private int quantidade;

    private String descricao;
    
    private String imagem;
    
    public ComponentCatalogResponseDTO(Long idComponente, String nomeComponente, CategoryEntity categoria, int quantidade, String descricao) {
        this.idComponente = idComponente;
        this.nomeComponente = nomeComponente;
        this.categoria = categoria;
        this.quantidade = quantidade;
        this.descricao = descricao;
    }
}
