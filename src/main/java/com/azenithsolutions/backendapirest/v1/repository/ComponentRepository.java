package com.azenithsolutions.backendapirest.v1.repository;

import com.azenithsolutions.backendapirest.v1.model.Component;
import com.azenithsolutions.backendapirest.v1.model.enums.ComponentCondition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ComponentRepository extends JpaRepository<Component, Long>, JpaSpecificationExecutor<Component> {
    @Query("SELECT c FROM Component c WHERE c.quantidade <= :quantidade")
    List<Component> findByQuantityLessThan(Integer quantidade);

    @Query("SELECT c FROM Component c WHERE c.condicao = :observacao")
    List<Component> findByObservationCondition(@Param("observacao") ComponentCondition observacao);

    @Query("SELECT c FROM Component c WHERE " +
            "(c.flagML = true AND (c.codigoML IS NULL OR c.codigoML = '')) OR " +
            "(c.flagML = false AND c.codigoML IS NOT NULL AND c.codigoML != '') OR " +
            "(c.condicao IS NULL) OR " +
            "(c.condicao = :condition AND (c.observacao IS NULL OR c.observacao = '')) OR " +
            "(c.descricao IS NULL OR c.descricao = '') OR " +
            "(c.flagVerificado = false) OR " +
            "(c.fkCaixa IS NULL) OR " +
            "(c.imagem IS NULL OR c.imagem = '')")

    List<Component> findByIncomplete(@Param("condition") ComponentCondition condition);

    @Query("SELECT c FROM Component c WHERE c.dataUltimaVenda <= :SLAUltimaVenda")
    List<Component> findByLastSaleSLA(LocalDate SLAUltimaVenda);

    @Query("SELECT COUNT (*) FROM Component c WHERE c.flagML = true")
    Integer findByFlagMLTrue();

    @Query("SELECT COUNT (*) FROM Component c WHERE c.flagML = false")
    Integer findByFlagMLFalse();
}
