package com.azenithsolutions.backendapirest.v1.repository;

import com.azenithsolutions.backendapirest.v1.model.Component;
import com.azenithsolutions.backendapirest.v1.model.enums.ComponentCondition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ComponentRepository extends JpaRepository<Component, Long> {
    @Query("SELECT c FROM Component c WHERE c.quantidade <= 1")
    List<Component> findByQuantityLessThanTen();

    @Query("SELECT c FROM Component c WHERE c.condicao = :critico OR c.condicao = :observacao")
    List<Component> findByCriticAndObservationCondition(
            @Param("critico") ComponentCondition critico,
            @Param("observacao") ComponentCondition observacao
    );

    @Query("SELECT c FROM Component c WHERE (c.flagML = true AND (c.codigoML IS NULL OR c.codigoML = '')) OR " +
            "(c.flagML = false AND c.codigoML IS NOT NULL AND c.codigoML != '') OR " +
            "(c.condicao IS NULL) OR " +
            "(c.condicao = :condition AND (c.observacao IS NULL OR c.observacao = '')) OR " +
            "(c.descricao IS NULL OR c.descricao = '') OR " +
            "(c.flagVerificado = false) OR " +
            "(c.fkCaixa IS NULL)")
    List<Component> findByIncomplete(@Param("condition") ComponentCondition condition);
}
