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
    public static Specification<Component> filterBy(String nomeComponente, Long categoria) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (nomeComponente != null && !nomeComponente.trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("nomeComponente")),
                        "%" + nomeComponente.toLowerCase() + "%"));
            }

            // Filtro por categoria, se o ID for informado
            if (categoria != null && categoria != 0) {
                predicates.add(criteriaBuilder.equal(
                        root.get("fkCategoria").get("id"), categoria
                ));
            }

            // Adiciona filtro para o campo is_visible_catalog = true
            predicates.add(criteriaBuilder.isTrue(root.get("isVisibleCatalog")));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Component> whereDinamicFilter(Map<String, Object> filtros) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            try {
                String campo = (String) filtros.get("where");
                Object valor = filtros.get("whereValue");

                if (campo != null && !campo.isBlank() && valor != null) {
                    Path<?> path = root.get(campo);

                    if (path.getJavaType().equals(String.class)) {
                        predicates.add(criteriaBuilder.like(
                                criteriaBuilder.lower(root.get(campo)),
                                "%" + valor.toString().toLowerCase() + "%"
                        ));
                    } else if (path.getJavaType().equals(Boolean.class)) {
                        predicates.add(criteriaBuilder.equal(root.get(campo), Boolean.valueOf(valor.toString())));
                    } else if (path.getJavaType().equals(Double.class)) {
                        predicates.add(criteriaBuilder.equal(root.get(campo), Double.valueOf(valor.toString())));
                    } else if (path.getJavaType().equals(LocalDateTime.class)) {
                        predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(campo), LocalDateTime.parse(valor.toString())));
                    } else if (path.getJavaType().equals(Integer.class)) {
                        predicates.add(criteriaBuilder.equal(root.get(campo), Integer.valueOf(valor.toString())));
                    }
                }
            } catch (IllegalArgumentException e) {
                // Campo informado não existe
                System.err.println("Campo inválido no filtro: " + e.getMessage());
            }

            // Sempre aplica esse filtro
            predicates.add(criteriaBuilder.isTrue(root.get("isVisibleCatalog")));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

}
