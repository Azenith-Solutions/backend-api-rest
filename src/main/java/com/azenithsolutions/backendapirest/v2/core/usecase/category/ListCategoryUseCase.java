package com.azenithsolutions.backendapirest.v2.core.usecase.category;

import com.azenithsolutions.backendapirest.v2.core.domain.model.category.Category;
import com.azenithsolutions.backendapirest.v2.core.domain.repository.CategoryRepositoryGateway;

public class ListCategoryUseCase {
    private final CategoryRepositoryGateway repository;

    public ListCategoryUseCase(CategoryRepositoryGateway repository) { 
        this.repository = repository; 
    }

    public java.util.List<Category> execute() { 
        return repository.findAll(); 
    }
}
