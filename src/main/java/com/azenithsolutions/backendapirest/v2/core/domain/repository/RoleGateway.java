package com.azenithsolutions.backendapirest.v2.core.domain.repository;

import com.azenithsolutions.backendapirest.v2.core.domain.model.role.Role;

public interface RoleGateway {
    Role getById(Long id);
}
