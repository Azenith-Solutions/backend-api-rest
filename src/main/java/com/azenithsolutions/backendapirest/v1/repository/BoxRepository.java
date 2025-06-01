package com.azenithsolutions.backendapirest.v1.repository;

import com.azenithsolutions.backendapirest.v1.dto.box.BoxDashboardDTO;
import com.azenithsolutions.backendapirest.v1.model.Box;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoxRepository extends JpaRepository<Box, Long> {
    @Query("SELECT b FROM Box b WHERE SIZE(b.components) >= :maxComponentsPerBox OR SIZE(b.components) < :maxComponentsPerBox ORDER BY SIZE(b.components) DESC LIMIT 5")
    List<BoxDashboardDTO> findBoxesGreaterOrAlmostOrInLimitOfComponents(Integer maxComponentsPerBox);

    @Query("SELECT COUNT (b.components) FROM Box b WHERE b.idCaixa = :idCaixa")
    Integer countComponentsInBoxes(Long idCaixa);
}