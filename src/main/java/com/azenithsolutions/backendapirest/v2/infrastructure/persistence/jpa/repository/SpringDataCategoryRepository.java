package com.azenithsolutions.backendapirest.v2.infrastructure.persistence.jpa.repository;

import com.azenithsolutions.backendapirest.v2.infrastructure.persistence.jpa.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataCategoryRepository extends JpaRepository<CategoryEntity, Long> {

}
