package com.azenithsolutions.backendapirest.v2.core.domain.repository;

import com.azenithsolutions.backendapirest.v2.core.domain.model.category.Category; // CORRIGIDO
import java.util.List;

public interface CategoryRepositoryGateway {
    List<Category> findAll();
}