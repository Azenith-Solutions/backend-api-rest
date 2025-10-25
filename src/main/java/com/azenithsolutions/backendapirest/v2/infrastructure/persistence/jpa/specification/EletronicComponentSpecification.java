package com.azenithsolutions.backendapirest.v2.infrastructure.persistence.jpa.specification;

import com.azenithsolutions.backendapirest.v2.infrastructure.persistence.jpa.entity.EletronicComponentEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EletronicComponentSpecification {

    public static Specification<EletronicComponentEntity> filterBy(String nomeComponente, Long categoria) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (nomeComponente != null && !nomeComponente.trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("nome")),
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

    public static Specification<EletronicComponentEntity> whereDinamicFilter(HashMap<String, Object> filtros) {
        Specification<EletronicComponentEntity> spec = Specification.where(null);

        if (filtros.containsKey("nome") && filtros.get("nome") != null) {
            String nome = (String) filtros.get("nome");
            if (!nome.isBlank()) {
                spec = spec.and(nomeContains(nome));
            }
        }

        if (filtros.containsKey("categoria") && filtros.get("categoria") != null) {
            String categoriaNome = (String) filtros.get("categoria");
            if (!categoriaNome.isBlank()) {
                spec = spec.and(categoryNameEquals(categoriaNome));
            }
        }

        if (filtros.containsKey("categoriaId") && filtros.get("categoriaId") != null) {
            Long categoriaId = Long.parseLong(filtros.get("categoriaId").toString());
            spec = spec.and(categoryIdEquals(categoriaId));
        }

        if (filtros.containsKey("quantidade") && filtros.get("quantidade") != null) {
            Integer quantidade = Integer.parseInt(filtros.get("quantidade").toString());
            spec = spec.and(quantidadeGreaterThan(quantidade));
        }

        if (filtros.containsKey("visivel") && filtros.get("visivel") != null) {
            Boolean visivel = Boolean.parseBoolean(filtros.get("visivel").toString());
            spec = spec.and(isVisible(visivel));
        }

        return spec;
    }

    private static Specification<EletronicComponentEntity> nomeContains(String nome) {
        return (nome == null || nome.isBlank()) ? null :
                (root, query, cb) -> cb.like(cb.lower(root.get("nome")), "%" + nome.toLowerCase() + "%");
    }

    private static Specification<EletronicComponentEntity> categoryEquals(Long categoriaId) {
        return (categoriaId == null) ? null :
                (root, query, cb) -> cb.equal(root.get("fkCategoria").get("id"), categoriaId);
    }

    private static Specification<EletronicComponentEntity> categoryNameEquals(String categoriaNome) {
        return (categoriaNome == null || categoriaNome.isBlank()) ? null :
                (root, query, cb) -> cb.equal(cb.lower(root.get("fkCategoria").get("nome")), categoriaNome.toLowerCase());
    }

    private static Specification<EletronicComponentEntity> categoryIdEquals(Long categoriaId) {
        return (categoriaId == null) ? null :
                (root, query, cb) -> cb.equal(root.get("fkCategoria").get("id"), categoriaId);
    }

    private static Specification<EletronicComponentEntity> quantidadeGreaterThan(Integer quantidade) {
        return (quantidade == null) ? null :
                (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("quantidade"), quantidade);
    }

    private static Specification<EletronicComponentEntity> isVisible(Boolean visivel) {
        return (visivel == null) ? null :
                (root, query, cb) -> cb.equal(root.get("isVisibleCatalog"), visivel);
    }
}
