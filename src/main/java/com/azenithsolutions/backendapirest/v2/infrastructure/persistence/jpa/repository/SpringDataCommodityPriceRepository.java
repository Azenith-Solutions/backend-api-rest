package com.azenithsolutions.backendapirest.v2.infrastructure.persistence.jpa.repository;

import com.azenithsolutions.backendapirest.v2.infrastructure.persistence.jpa.entity.CommodityPriceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface SpringDataCommodityPriceRepository extends JpaRepository<CommodityPriceEntity, Long> {

    @Query("SELECT c FROM CommodityPriceEntity c WHERE c.metals = :metals ORDER BY c.date ASC")
    List<CommodityPriceEntity> findByMetalsOrderByDateAsc(@Param("metals") String metals);

    @Query("SELECT c FROM CommodityPriceEntity c WHERE c.metals = :metals AND c.date BETWEEN :from AND :to ORDER BY c.date ASC")
    List<CommodityPriceEntity> findByMetalsAndDateBetween(
            @Param("metals") String metals,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to
    );
}
