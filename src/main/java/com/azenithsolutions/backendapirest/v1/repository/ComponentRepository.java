package com.azenithsolutions.backendapirest.v1.repository;

import com.azenithsolutions.backendapirest.v1.dto.component.ComponentCatalogResponseDTO;
import com.azenithsolutions.backendapirest.v1.model.Component;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComponentRepository extends JpaRepository<Component, Long> {
    Page<Component> findAll(Pageable pageable);
}
