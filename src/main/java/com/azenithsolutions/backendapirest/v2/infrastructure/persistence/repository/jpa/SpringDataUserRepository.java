package com.azenithsolutions.backendapirest.v2.infrastructure.persistence.repository.jpa;

import com.azenithsolutions.backendapirest.v2.infrastructure.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataUserRepository extends JpaRepository<UserEntity, Integer> {
    boolean existsByEmail(String email);
}
