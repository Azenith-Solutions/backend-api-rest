package com.azenithsolutions.backendapirest.v2.core.domain.repository;

public interface DatabaseQueryGateway {
    Object executeSelectQuery(String sql);
}
