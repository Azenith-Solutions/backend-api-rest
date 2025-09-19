package com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.user;


import com.azenithsolutions.backendapirest.v2.infrastructure.persistence.entity.RoleEntity;

public record LoginResponseDTO(String email, String name, RoleEntity fkFuncao, String token) {
}
