package com.azenithsolutions.backendapirest.v2.infrastructure.persistence.jpa.specification;

import com.azenithsolutions.backendapirest.v2.infrastructure.persistence.jpa.entity.EletronicComponentEntity;
import org.springframework.data.jpa.domain.Specification;

import java.util.HashMap;

public class EletronicComponentSpecification {

    public static Specification<EletronicComponentEntity> filterBy(String nome, Long categoriaId) {
        return Specification
                .where(nomeContains(nome))
                .and(categoryEquals(categoriaId));
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
                (root, query, cb) -> cb.equal(root.get("categoriaId"), categoriaId);
    }

    private static Specification<EletronicComponentEntity> categoryNameEquals(String categoriaNome) {
        return (categoriaNome == null || categoriaNome.isBlank()) ? null :
                (root, query, cb) -> cb.equal(root.get("categoriaNome"), categoriaNome);
    }

    private static Specification<EletronicComponentEntity> categoryIdEquals(Long categoriaId) {
        return (categoriaId == null) ? null :
                (root, query, cb) -> cb.equal(root.get("categoriaId"), categoriaId);
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
