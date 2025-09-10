package com.azenithsolutions.backendapirest.v2.core.domain.repository;

import com.azenithsolutions.backendapirest.v2.core.domain.model.user.User;

import java.util.List;
import java.util.Optional;

public interface UserGateway {
    User save(User user);
    boolean existsByEmail(String email);
    List<User> findAll();
    void deleteById(Integer id);
    User findById(Integer id);
    User findByEmail(String email);
}
