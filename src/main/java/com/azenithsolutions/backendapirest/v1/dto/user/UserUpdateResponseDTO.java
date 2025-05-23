package com.azenithsolutions.backendapirest.v1.dto.user;

import java.time.LocalDate;

public record UserUpdateResponseDTO(
        String fullName,
        String email,
        String role,
        Boolean status,
        LocalDate updatedAt
) {}
