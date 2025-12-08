package com.azenithsolutions.backendapirest.v2.core.domain.repository;

import com.azenithsolutions.backendapirest.v2.core.domain.model.component.EletronicComponent;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.components.ComponentCatalogResponseDTO;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.components.ComponentManagerResponseDTO;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.components.ComponentObservationDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public interface EletronicComponentGateway {
    EletronicComponent save(EletronicComponent componenteEletronico);
    Optional<EletronicComponent> findById(Long id);
    List<ComponentManagerResponseDTO> findAll();
    void deleteById(Long id);
    boolean existsById(Long id);
    
    Page<ComponentCatalogResponseDTO> findPageable(Pageable pageable, String nome, Long categoriaId);
    List<ComponentCatalogResponseDTO> findByFilters(HashMap<String, Object> filtros);
    Optional<ComponentCatalogResponseDTO> findDetailsById(Long id);
    List<EletronicComponent> findLowStockComponents();
    List<ComponentObservationDTO> findComponentsInObservation();
    List<EletronicComponent> findIncompleteComponents();
    List<EletronicComponent> findComponentsOutOfLastSaleSLA();
    List<Integer> countTrueAndFalseFlagML();
    EletronicComponent updateVisibility(Long id, Boolean isVisibleCatalog);
    EletronicComponent updateImagePath(Long id, String imagePath);
}
