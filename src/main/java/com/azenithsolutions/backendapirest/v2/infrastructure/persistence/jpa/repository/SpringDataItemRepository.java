package com.azenithsolutions.backendapirest.v2.infrastructure.persistence.jpa.repository;

import com.azenithsolutions.backendapirest.v2.infrastructure.persistence.jpa.entity.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataItemRepository extends JpaRepository<ItemEntity, Long> {
}
