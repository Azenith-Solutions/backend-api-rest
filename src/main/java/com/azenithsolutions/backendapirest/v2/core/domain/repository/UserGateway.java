package com.azenithsolutions.backendapirest.v2.core.domain.repository;

import com.azenithsolutions.backendapirest.v2.core.domain.model.user.User;

public interface UserGateway {
    User save(User user);
    boolean existsByEmail(String email);
}
