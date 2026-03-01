package com.azenithsolutions.backendapirest.v2.core.usecase.category;

import com.azenithsolutions.backendapirest.v2.core.domain.model.category.Category;
import com.azenithsolutions.backendapirest.v2.core.domain.repository.CategoryRepositoryGateway;

public class CreateCategoryUseCase {
    private final CategoryRepositoryGateway repository;

    public CreateCategoryUseCase(CategoryRepositoryGateway repository) {
        this.repository = repository;
    }

    public Category execute(String nome) {
        Category category = Category.create(null, nome);
        return repository.save(category);
    }
}
