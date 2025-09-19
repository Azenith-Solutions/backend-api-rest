package com.azenithsolutions.backendapirest.v2.core.usecase.auth.dto;

import com.azenithsolutions.backendapirest.v2.core.domain.model.user.User;

public record RegisteredUserResponse(User user, String token) {
}
