package com.azenithsolutions.backendapirest.v1.repository;

import com.azenithsolutions.backendapirest.v1.model.Component;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ComponentRepository extends JpaRepository<Component, Long> {
    @Query("SELECT c FROM Component c WHERE c.quantidade <= 1")
    List<Component> findByQuantidadeLessThanTen();
}
