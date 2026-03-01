package com.azenithsolutions.backendapirest.v2.core.usecase.category;

import com.azenithsolutions.backendapirest.v2.core.domain.repository.CategoryRepositoryGateway;

public class DeleteCategoryUseCase {
    private final CategoryRepositoryGateway repository;

    public DeleteCategoryUseCase(CategoryRepositoryGateway repository) {
        this.repository = repository;
    }

    public void execute(Long id) {
        repository.deleteById(id);
    }
}
