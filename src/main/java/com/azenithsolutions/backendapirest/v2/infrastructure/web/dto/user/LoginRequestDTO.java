package com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.user;

public record LoginRequestDTO(
        String email,
        String password
) {
}
