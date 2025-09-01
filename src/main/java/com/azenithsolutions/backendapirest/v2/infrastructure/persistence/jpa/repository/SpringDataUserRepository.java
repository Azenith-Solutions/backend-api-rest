package com.azenithsolutions.backendapirest.v2.infrastructure.persistence.jpa.repository;

import com.azenithsolutions.backendapirest.v2.infrastructure.persistence.jpa.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataUserRepository extends JpaRepository<UserEntity, Integer> {
    boolean existsByEmail(String email);
}
