package com.azenithsolutions.backendapirest.v2.core.usecase.box;

import com.azenithsolutions.backendapirest.v2.core.domain.model.box.Box;
import com.azenithsolutions.backendapirest.v2.core.domain.repository.BoxGateway;

import java.util.List;

public class ListBoxesUseCase {
    private final BoxGateway repository;

    public ListBoxesUseCase(BoxGateway repository) {
        this.repository = repository;
    }

    public List<Box> execute() { return repository.findAll(); }
}
