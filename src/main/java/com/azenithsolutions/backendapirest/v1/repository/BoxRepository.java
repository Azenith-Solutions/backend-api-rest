package com.azenithsolutions.backendapirest.v1.repository;

import com.azenithsolutions.backendapirest.v1.model.Box;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoxRepository extends JpaRepository<Box, Long> {
    @Query("SELECT b FROM Box b LEFT JOIN b.components c " +
            "GROUP BY b.idCaixa, b.nomeCaixa " +
            "ORDER BY COUNT(c) DESC " +
            "LIMIT 5")
    List<Box> findBoxesGreaterOrAlmostOrInLimitOfComponents();

    @Query("SELECT COUNT(c) FROM Box b JOIN b.components c WHERE b.idCaixa = :idCaixa")
    Integer countComponentsInBoxes(Long idCaixa);
}