package com.azenithsolutions.backendapirest.v2.infrastructure.web.mappers;

import com.azenithsolutions.backendapirest.v2.core.domain.model.box.Box;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.box.BoxListRest;

public class BoxRestMapper {
    public static BoxListRest toList(Box box) {
        return new BoxListRest(box.getIdCaixa(), box.getNomeCaixa());
    }
}
