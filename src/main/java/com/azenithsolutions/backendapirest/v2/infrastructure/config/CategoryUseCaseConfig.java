package com.azenithsolutions.backendapirest.v2.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import com.azenithsolutions.backendapirest.v2.infrastructure.persistence.adapter.CategoryRepositoryAdapter;
import com.azenithsolutions.backendapirest.v2.core.usecase.category.ListCategoryUseCase;


@Configuration
public class CategoryUseCaseConfig {

    @Bean
    public ListCategoryUseCase listCategoryUseCase(CategoryRepositoryAdapter categoryRepositoryAdapter){
        return new ListCategoryUseCase(categoryRepositoryAdapter);
    }
}
