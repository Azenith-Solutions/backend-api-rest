package com.azenithsolutions.backendapirest.v2.infrastructure.web.mappers;

import com.azenithsolutions.backendapirest.v2.core.domain.model.category.Category;
import com.azenithsolutions.backendapirest.v2.infrastructure.persistence.jpa.entity.CategoryEntity;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.category.CategoryListResponseDTO;

import java.util.Objects;

public class CategoryEntityMapper {
    public static Category toDomain(CategoryEntity categoryEntity) {
        return Category.create(
                categoryEntity.getId(),
                categoryEntity.getNome()
        );
    }

    public static CategoryListResponseDTO toListResponseDTO(Category category){
        if(Objects.isNull(category)) return null;

        CategoryListResponseDTO categoryListResponseDTO = new CategoryListResponseDTO(
                category.getIdCategoria(),
                category.getNomeCategoria()
        );

        return  categoryListResponseDTO;
    }


}
