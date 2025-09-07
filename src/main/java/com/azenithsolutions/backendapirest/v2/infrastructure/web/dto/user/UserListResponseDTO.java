package com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.user;

import java.time.LocalDate;

public record UserListResponseDTO(
        int id,
        String fullName,
        String email,
        String role,
        String profilePicture,
        Boolean status,
        LocalDate createdAt
) {}
