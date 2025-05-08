package com.azenithsolutions.backendapirest.v1.repository;

import com.azenithsolutions.backendapirest.v1.model.Box;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoxRepository extends JpaRepository<Box, Long> {
}
