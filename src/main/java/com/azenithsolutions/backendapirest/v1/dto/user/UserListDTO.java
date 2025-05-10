package com.azenithsolutions.backendapirest.v1.dto.user;

import java.time.LocalDate;

public record UserListDTO(
        int id,
        String fullName,
        String email,
        String role,
        boolean status,
        LocalDate createdAt
) {}
