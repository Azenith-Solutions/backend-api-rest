package com.azenithsolutions.backendapirest.v2.infrastructure.persistence.jpa.adapter;

import com.azenithsolutions.backendapirest.v2.core.domain.model.role.Role;
import com.azenithsolutions.backendapirest.v2.core.domain.repository.RoleGateway;
import com.azenithsolutions.backendapirest.v2.infrastructure.persistence.jpa.entity.RoleEntity;
import com.azenithsolutions.backendapirest.v2.infrastructure.persistence.jpa.repository.SpringDataRoleRepository;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.mappers.RoleEntityMapper;
import org.springframework.stereotype.Service;

@Service
public class RoleRepositoryAdapter implements RoleGateway {

    private final SpringDataRoleRepository repository;

    public RoleRepositoryAdapter(SpringDataRoleRepository repository) {
        this.repository = repository;
    }

    @Override
    public Role getById(Long id) {
        RoleEntity roleEntity = repository.getById(id);
        Role role = RoleEntityMapper.toDomain(roleEntity);
        return role;
    }
}
