package com.azenithsolutions.backendapirest.v2.core.domain.repository;

import com.azenithsolutions.backendapirest.v2.core.domain.model.box.Box;

import java.util.List;

public interface BoxGateway {
    List<Box> findAll();
    List<Box> findBoxesGreaterOrAlmostOrInLimitOfComponents();
    int countComponentsInBoxes(Long boxId);
}
