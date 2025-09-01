package com.azenithsolutions.backendapirest.v2.core.domain.repository;

import com.azenithsolutions.backendapirest.v2.core.domain.model.component.EletronicComponent;

import java.util.List;
import java.util.Optional;


public interface EletronicComponentGateway {
    EletronicComponent save(EletronicComponent componenteEletronico);
    Optional<EletronicComponent> findById(Long id);
    List<EletronicComponent> findAll();
    void deleteById(Long id);
    boolean existsById(Long id);
}
