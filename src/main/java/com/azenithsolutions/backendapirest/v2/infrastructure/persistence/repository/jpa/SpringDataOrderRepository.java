package com.azenithsolutions.backendapirest.v2.infrastructure.persistence.repository.jpa;

import com.azenithsolutions.backendapirest.v2.infrastructure.persistence.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataOrderRepository extends JpaRepository<OrderEntity, Long> { }
