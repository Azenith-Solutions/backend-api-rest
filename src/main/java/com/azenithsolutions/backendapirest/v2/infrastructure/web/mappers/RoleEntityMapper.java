package com.azenithsolutions.backendapirest.v2.infrastructure.web.mappers;

import com.azenithsolutions.backendapirest.v2.core.domain.model.role.Role;
import com.azenithsolutions.backendapirest.v2.infrastructure.persistence.jpa.entity.RoleEntity;

import java.util.Objects;

public class RoleEntityMapper {
    public static RoleEntity toEntity(Role role){
        if(Objects.isNull(role)){
            return null;
        }
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setIdFuncao(role.getIdFuncao());
        roleEntity.setFuncao(role.getFuncao());

        return roleEntity;
    }

    public static Role toDomain(RoleEntity roleEntity){
        if(roleEntity == null){
            return null;
        }

        Role role = Role.create(
                roleEntity.getIdFuncao(),
                roleEntity.getFuncao()
        );

        return role;
    }

}
