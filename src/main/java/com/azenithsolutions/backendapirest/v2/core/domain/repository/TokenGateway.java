package com.azenithsolutions.backendapirest.v2.core.domain.repository;

import com.azenithsolutions.backendapirest.v2.core.domain.model.user.User;

import java.time.Instant;

public interface TokenGateway {
    String generateToken(User user);
    String subjectToken(String token);
    Instant generateExpirationDate();
    boolean isTokenValid(String token);
}
