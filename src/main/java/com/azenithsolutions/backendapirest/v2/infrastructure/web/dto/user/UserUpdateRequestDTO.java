package com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.user;

public record UserUpdateRequestDTO(
    String fullName,
    String email,
    String password,
    Boolean status,
    Long role
){}
