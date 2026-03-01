package com.azenithsolutions.backendapirest.v2.core.usecase.box;

import com.azenithsolutions.backendapirest.v2.core.domain.model.box.Box;
import com.azenithsolutions.backendapirest.v2.core.domain.repository.BoxGateway;

import java.util.Collections;

public class CreateBoxUseCase {
    private final BoxGateway repository;

    public CreateBoxUseCase(BoxGateway repository) {
        this.repository = repository;
    }

    public Box execute(String nome) {
        Box box = Box.criarNovo(nome, Collections.emptyList());
        return repository.save(box);
    }
}
