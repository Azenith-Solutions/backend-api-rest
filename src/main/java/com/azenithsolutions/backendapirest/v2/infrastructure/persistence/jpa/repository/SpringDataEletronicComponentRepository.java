package com.azenithsolutions.backendapirest.v2.infrastructure.persistence.jpa.repository;
import com.azenithsolutions.backendapirest.v2.infrastructure.persistence.jpa.entity.EletronicComponentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataEletronicComponentRepository extends JpaRepository<EletronicComponentEntity, Long> {
}
