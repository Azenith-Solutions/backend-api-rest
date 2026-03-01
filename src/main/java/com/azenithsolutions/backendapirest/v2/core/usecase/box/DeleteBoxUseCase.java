package com.azenithsolutions.backendapirest.v2.core.usecase.box;

import com.azenithsolutions.backendapirest.v2.core.domain.repository.BoxGateway;

public class DeleteBoxUseCase {
    private final BoxGateway repository;

    public DeleteBoxUseCase(BoxGateway repository) {
        this.repository = repository;
    }

    public void execute(Long id) {
        repository.deleteById(id);
    }
}
