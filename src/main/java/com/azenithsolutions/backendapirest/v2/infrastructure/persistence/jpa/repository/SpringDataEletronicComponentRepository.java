package com.azenithsolutions.backendapirest.v2.infrastructure.persistence.jpa.repository;
import com.azenithsolutions.backendapirest.v2.infrastructure.persistence.jpa.entity.EletronicComponentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.Date;
import java.util.List;

public interface SpringDataEletronicComponentRepository extends JpaRepository<EletronicComponentEntity, Long>, JpaSpecificationExecutor<EletronicComponentEntity> {
    
    Page<EletronicComponentEntity> findAll(Specification<EletronicComponentEntity> spec, Pageable pageable);
    
    @Query("SELECT e FROM EletronicComponentEntity e WHERE e.quantidade <= :quantidade")
    List<EletronicComponentEntity> findByQuantityLessThan(@Param("quantidade") Integer quantidade);
    
    @Query("SELECT e FROM EletronicComponentEntity e WHERE e.condicao = :observacao")
    List<EletronicComponentEntity> findByObservationCondition(@Param("observacao") String observacao);
    
    @Query("SELECT e FROM EletronicComponentEntity e WHERE " +
            "(e.isVisibleCatalog = true AND (e.codigoML IS NULL OR e.codigoML = '')) OR " +
            "(e.isVisibleCatalog = false AND e.codigoML IS NOT NULL AND e.codigoML != '') OR " +
            "(e.condicao IS NULL) OR " +
            "(e.condicao = :condition AND (e.observacao IS NULL OR e.observacao = '')) OR " +
            "(e.partNumber IS NULL OR e.partNumber = '') OR " +
            "(e.flagVerificado = false) OR " +
            "(e.fkCaixa IS NULL) OR " +
            "(e.s3ImagePath IS NULL OR e.s3ImagePath = '')")
    List<EletronicComponentEntity> findByIncomplete(@Param("condition") String condition);
    
    @Query("SELECT e FROM EletronicComponentEntity e WHERE e.dataUltimaVenda <= :SLAUltimaVenda")
    List<EletronicComponentEntity> findByLastSaleSLA(@Param("SLAUltimaVenda") Date SLAUltimaVenda);
    
    @Query("SELECT COUNT(e) FROM EletronicComponentEntity e WHERE e.flagML = true")
    Integer findByFlagMLTrue();
    
    @Query("SELECT COUNT(e) FROM EletronicComponentEntity e WHERE e.flagML = false OR e.flagML IS NULL")
    Integer findByFlagMLFalse();
}
