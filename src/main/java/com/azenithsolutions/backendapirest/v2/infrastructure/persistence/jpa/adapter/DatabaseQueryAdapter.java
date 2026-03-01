package com.azenithsolutions.backendapirest.v2.infrastructure.persistence.jpa.adapter;

import com.azenithsolutions.backendapirest.v2.core.domain.repository.DatabaseQueryGateway;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DatabaseQueryAdapter implements DatabaseQueryGateway {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional(readOnly = true)
    public Object executeSelectQuery(String sql) {
        Query nativeQuery = entityManager.createNativeQuery(sql);
        List<?> resultList = nativeQuery.getResultList();

        if (resultList.isEmpty()) {
            return "Nenhum resultado encontrado.";
        } else if (resultList.size() == 1) {
            Object item = resultList.get(0);
            if (item instanceof Object[] arr && arr.length == 1) {
                return arr[0];
            }
            return item;
        } else {
            return resultList;
        }
    }
}
