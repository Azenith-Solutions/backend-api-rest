package com.azenithsolutions.backendapirest.v1.dto.component;

import com.azenithsolutions.backendapirest.v1.model.enums.ComponentCondition;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComponentObservationDTO {
    private String idHardWareTech;
    private String partNumber;
    private String descricao;
    private String observacao;
}