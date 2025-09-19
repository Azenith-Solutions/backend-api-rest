package com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.user;

public record UserRegisterRequestDTO(String fullName, String email, String password, Long role) {
}
