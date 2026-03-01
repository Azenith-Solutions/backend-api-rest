package com.azenithsolutions.backendapirest.v2.infrastructure.config.bean;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import com.azenithsolutions.backendapirest.v2.core.domain.repository.CategoryRepositoryGateway;
import com.azenithsolutions.backendapirest.v2.core.usecase.category.CreateCategoryUseCase;
import com.azenithsolutions.backendapirest.v2.core.usecase.category.DeleteCategoryUseCase;
import com.azenithsolutions.backendapirest.v2.core.usecase.category.ListCategoryUseCase;


@Configuration
public class CategoryUseCaseConfig {

    @Bean
    public ListCategoryUseCase listCategoryUseCase(CategoryRepositoryGateway repository){
        return new ListCategoryUseCase(repository);
    }

    @Bean
    public CreateCategoryUseCase createCategoryUseCase(CategoryRepositoryGateway repository){
        return new CreateCategoryUseCase(repository);
    }

    @Bean
    public DeleteCategoryUseCase deleteCategoryUseCase(CategoryRepositoryGateway repository){
        return new DeleteCategoryUseCase(repository);
    }
}
