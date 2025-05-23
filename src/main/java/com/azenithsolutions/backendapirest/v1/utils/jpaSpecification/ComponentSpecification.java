package com.azenithsolutions.backendapirest.v1.utils.jpaSpecification;

import com.azenithsolutions.backendapirest.v1.model.Component;
import jakarta.persistence.criteria.Path;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    public static Specification<Component> whereDinamicFilter(Map<String, Object> filtros) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            String campo = (String) filtros.get("where");
            Object valor = filtros.get("whereValue");

            if (campo == null || campo.isBlank()) return criteriaBuilder.conjunction();
            if (valor == null  ) return criteriaBuilder.conjunction();


            Path<?> path = root.get(campo);

            if (path.getJavaType().equals(String.class)) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(campo)), "%" + valor.toString().toLowerCase() + "%"));
            } else if (path.getJavaType().equals(Boolean.class)) {
                predicates.add(criteriaBuilder.equal(root.get(campo), Boolean.valueOf(valor.toString())));
            } else if (path.getJavaType().equals(Double.class)) {
                predicates.add(criteriaBuilder.equal(root.get(campo), Double.valueOf(valor.toString())));
            } else if (path.getJavaType().equals(LocalDateTime.class)) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(campo), LocalDateTime.parse(valor.toString())));
            }else if(path.getJavaType().equals(Integer.class)){
                predicates.add(criteriaBuilder.equal(root.get(campo), Integer.valueOf(valor.toString())));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
