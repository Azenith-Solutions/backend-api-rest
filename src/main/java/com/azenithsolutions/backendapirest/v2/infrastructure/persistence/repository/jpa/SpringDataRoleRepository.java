package com.azenithsolutions.backendapirest.v2.infrastructure.persistence.repository.jpa;

import com.azenithsolutions.backendapirest.v2.infrastructure.persistence.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataRoleRepository extends JpaRepository<RoleEntity, Long> {
}
