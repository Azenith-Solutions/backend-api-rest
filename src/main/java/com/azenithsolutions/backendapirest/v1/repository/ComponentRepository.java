package com.azenithsolutions.backendapirest.v1.repository;

import com.azenithsolutions.backendapirest.v1.dto.component.ComponentCatalogResponseDTO;
import com.azenithsolutions.backendapirest.v1.model.Component;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ComponentRepository extends JpaRepository<Component, Long> {
    @Query(value = """
    SELECT * FROM componente c
    WHERE c.descricao IS NOT NULL AND TRIM(c.descricao) <> ''
    """, nativeQuery = true)
    Page<Component> findAll(Pageable pageable);

}
