package com.azenithsolutions.backendapirest.v2.infrastructure.persistence.jpa.repository;


import com.azenithsolutions.backendapirest.v2.infrastructure.persistence.jpa.entity.BoxEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SpringDataBoxRepository extends JpaRepository<BoxEntity, Long> {
    @Query("SELECT b FROM BoxEntity b ORDER BY SIZE(b.fkComponents) DESC")
    List<BoxEntity> findBoxesGreaterOrAlmostOrInLimitOfComponents();

    @Query("SELECT COUNT(c) FROM BoxEntity b JOIN b.fkComponents c WHERE b.idCaixa = :idCaixa")
    Integer countComponentsInBoxes(@Param("idCaixa") Long idCaixa);
}
