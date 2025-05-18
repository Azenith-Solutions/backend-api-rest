package com.azenithsolutions.backendapirest.v1.jpaSpecification;

import com.azenithsolutions.backendapirest.v1.model.Component;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.criteria.Predicate;


public class ComponentSpecification {
    public static Specification<Component> filterBy(String descricao) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (descricao != null && !descricao.trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("descricao")),
                        "%" + descricao.toLowerCase() + "%"));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
