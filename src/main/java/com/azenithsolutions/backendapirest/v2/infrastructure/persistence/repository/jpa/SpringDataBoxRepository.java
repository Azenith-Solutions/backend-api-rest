package com.azenithsolutions.backendapirest.v2.infrastructure.persistence.repository.jpa;


import com.azenithsolutions.backendapirest.v2.infrastructure.persistence.entity.BoxEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SpringDataBoxRepository extends JpaRepository<BoxEntity, Long> {
    // Ordena pelas caixas com mais componentes, sem usar LIMIT (controle de top N pode ser feito na camada de serviço/adaptador)
    @Query("SELECT b FROM BoxEntity b ORDER BY SIZE(b.fkComponents) DESC")
    List<BoxEntity> findBoxesGreaterOrAlmostOrInLimitOfComponents();

    // Conta os componentes de uma caixa específica
    @Query("SELECT COUNT(c) FROM BoxEntity b JOIN b.fkComponents c WHERE b.idCaixa = :idCaixa")
    Integer countComponentsInBoxes(@Param("idCaixa") Long idCaixa);
}
