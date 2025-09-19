package com.azenithsolutions.backendapirest.v2.infrastructure.persistence.repository.jpa;

import com.azenithsolutions.backendapirest.v2.infrastructure.persistence.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataCategoryRepository extends JpaRepository<CategoryEntity, Long> {

}
