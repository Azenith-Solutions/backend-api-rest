package com.azenithsolutions.backendapirest.v2.infrastructure.persistence.jpa.repository;

import com.azenithsolutions.backendapirest.v2.infrastructure.persistence.jpa.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataOrderRepository extends JpaRepository<OrderEntity, Long> { }
