package com.azenithsolutions.backendapirest.v2.core.usecase.auth.dto;


public record RegisterUserRequest(String fullName, String email, String password, Long role) {
}
