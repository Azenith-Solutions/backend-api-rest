package com.azenithsolutions.backendapirest.v2.infrastructure.persistence.jpa.adapter;

import com.azenithsolutions.backendapirest.v2.core.domain.repository.CategoryRepositoryGateway;
import com.azenithsolutions.backendapirest.v2.core.domain.model.category.Category;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.mappers.CategoryEntityMapper;
import com.azenithsolutions.backendapirest.v2.infrastructure.persistence.jpa.entity.CategoryEntity;
import com.azenithsolutions.backendapirest.v2.infrastructure.persistence.jpa.repository.SpringDataCategoryRepository;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;



@Component
public class CategoryRepositoryAdapter implements CategoryRepositoryGateway {

    private final SpringDataCategoryRepository repository;

    public CategoryRepositoryAdapter(SpringDataCategoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Category> findAll(){
        List<CategoryEntity> categoryEntitiesList = repository.findAll();
        List<Category> categoryList = categoryEntitiesList.stream().map(CategoryEntityMapper::toDomain).collect(Collectors.toList());
        
        return categoryList;
    }

}
