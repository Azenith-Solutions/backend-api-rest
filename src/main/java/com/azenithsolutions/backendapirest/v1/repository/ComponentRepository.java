package com.azenithsolutions.backendapirest.v1.repository;

import com.azenithsolutions.backendapirest.v1.model.Component;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ComponentRepository extends JpaRepository<Component, Long>, JpaSpecificationExecutor<Component> {

}
