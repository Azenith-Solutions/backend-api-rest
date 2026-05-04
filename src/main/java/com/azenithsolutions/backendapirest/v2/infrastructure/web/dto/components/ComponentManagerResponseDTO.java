package com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.components;


import com.azenithsolutions.backendapirest.v2.infrastructure.persistence.jpa.entity.BoxEntity;
import com.azenithsolutions.backendapirest.v2.infrastructure.persistence.jpa.entity.CategoryEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComponentManagerResponseDTO {

    private Long idComponente;
    private String idHardWareTech;
    private String idQrcode;
    private String nomeComponente;
    private BoxEntity fkCaixa;
    private CategoryEntity fkCategoria;
    private String partNumber;
    private int quantidade;
    private Boolean flagML;
    private String codigoML;
    private Boolean flagVerificado;
    private String condicao;
    private String observacao;
    private String descricao;
    private String imagem;
    private Date dataUltimaVenda;
    private Date createdAt;
    private Date updatedAt;
    private Integer quantidadeVendido;
    private Boolean isVisibleCatalog;
}
