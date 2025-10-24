    package com.azenithsolutions.backendapirest.v2.infrastructure.persistence.jpa.adapter;

import com.azenithsolutions.backendapirest.v2.core.domain.model.component.EletronicComponent;
import com.azenithsolutions.backendapirest.v2.core.domain.repository.EletronicComponentGateway;
import com.azenithsolutions.backendapirest.v2.infrastructure.persistence.jpa.entity.EletronicComponentEntity;
import com.azenithsolutions.backendapirest.v2.infrastructure.persistence.jpa.repository.SpringDataEletronicComponentRepository;
import com.azenithsolutions.backendapirest.v2.infrastructure.persistence.jpa.specification.EletronicComponentSpecification;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.components.ComponentCatalogResponseDTO;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.components.ComponentObservationDTO;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.mappers.EletronicComponentMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Calendar;
import java.util.stream.Collectors;

@Component
public class EletronicComponentAdapter implements EletronicComponentGateway {
    private final SpringDataEletronicComponentRepository repository;

    public EletronicComponentAdapter(SpringDataEletronicComponentRepository repository) {
        this.repository = repository;
    }

    @Override
    public EletronicComponent save(EletronicComponent componenteEletronico) {
        EletronicComponentEntity entity = EletronicComponentMapper.toEntity(componenteEletronico);
        EletronicComponentEntity savedEntity = repository.save(entity);
        return EletronicComponentMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<EletronicComponent> findById(Long id) {
        return repository.findById(id).map(EletronicComponentMapper::toDomain);
    }

    @Override
    public List<EletronicComponent> findAll() {
        return EletronicComponentMapper.toDomainList(repository.findAll());
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }
    
    @Override
    public Page<ComponentCatalogResponseDTO> findPageable(Pageable pageable, String nome, Long categoriaId) {
        Specification<EletronicComponentEntity> spec = EletronicComponentSpecification.filterBy(nome, categoriaId);
        Page<EletronicComponentEntity> page = repository.findAll(spec, pageable);
        return page.map(EletronicComponentMapper::toResponseCatalogDTO);
    }

    @Override
    public List<ComponentCatalogResponseDTO> findByFilters(HashMap<String, Object> filtros) {
        Specification<EletronicComponentEntity> spec = EletronicComponentSpecification.whereDinamicFilter(filtros);
        
        String orderBy = (String) filtros.get("orderBy");
        Sort.Direction direction = filtros.containsKey("ASC") && (boolean) filtros.get("ASC") ? 
            Sort.Direction.ASC : Sort.Direction.DESC;
        Integer limit = filtros.containsKey("limit") ? Integer.parseInt(filtros.get("limit").toString()) : Integer.MAX_VALUE;

        Pageable pageable;
        if (orderBy != null && !orderBy.isBlank()) {
            pageable = PageRequest.of(0, limit, Sort.by(direction, orderBy));
        } else {
            pageable = PageRequest.of(0, limit);
        }

        Page<EletronicComponentEntity> page = repository.findAll(spec, pageable);
        return EletronicComponentMapper.tonListCatalogDTO(page.getContent());
    }

    @Override
    public Optional<ComponentCatalogResponseDTO> findDetailsById(Long id) {
        return repository.findById(id).map(EletronicComponentMapper::toResponseCatalogDTO);
    }

    @Override
    public List<EletronicComponent> findLowStockComponents() {
        // Usando o valor 5 para baixo estoque, como na V1
        List<EletronicComponentEntity> entities = repository.findByQuantityLessThan(5);
        return EletronicComponentMapper.toDomainList(entities);
    }

    @Override
    public List<ComponentObservationDTO> findComponentsInObservation() {
        // Passando a string EM_OBSERVACAO como parâmetro
        List<EletronicComponentEntity> entities = repository.findByObservationCondition("EM_OBSERVACAO");
        return entities.stream()
                .map(entity -> new ComponentObservationDTO(
                        entity.getId(),
                        entity.getNome(),
                        "Categoria", // TODO: Buscar categoria pelo ID se necessário
                        entity.getObservacao()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<EletronicComponent> findIncompleteComponents() {
        // Passando a condição EM_OBSERVACAO como parâmetro
        List<EletronicComponentEntity> entities = repository.findByIncomplete("EM_OBSERVACAO");
        return EletronicComponentMapper.toDomainList(entities);
    }

    @Override
    public List<EletronicComponent> findComponentsOutOfLastSaleSLA() {
        // 30 dias atrás
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -30);
        Date lastSaleSLA = calendar.getTime();
        
        List<EletronicComponentEntity> entities = repository.findByLastSaleSLA(lastSaleSLA);
        return EletronicComponentMapper.toDomainList(entities);
    }

    @Override
    public List<Integer> countTrueAndFalseFlagML() {
        Integer componentsWithFlagMLTrue = repository.findByFlagMLTrue();
        Integer componentsWithFlagMLFalse = repository.findByFlagMLFalse();
        
        return List.of(componentsWithFlagMLTrue, componentsWithFlagMLFalse);
    }

    @Override
    public EletronicComponent updateVisibility(Long id, Boolean isVisibleCatalog) {
        Optional<EletronicComponentEntity> entityOpt = repository.findById(id);
        
        if (entityOpt.isEmpty()) {
            throw new RuntimeException("Componente não encontrado com ID: " + id);
        }
        
        EletronicComponentEntity entity = entityOpt.get();
        entity.setIsVisibleCatalog(isVisibleCatalog);
        entity.setFlagML(isVisibleCatalog);
        entity.setUpdatedAt(new Date());
        
        EletronicComponentEntity savedEntity = repository.save(entity);
        return EletronicComponentMapper.toDomain(savedEntity);
    }

    @Override
    public EletronicComponent updateImagePath(Long id, String imagePath) {
        Optional<EletronicComponentEntity> entityOpt = repository.findById(id);
        
        if (entityOpt.isEmpty()) {
            throw new RuntimeException("Componente não encontrado com ID: " + id);
        }
        
        EletronicComponentEntity entity = entityOpt.get();
        entity.setS3ImagePath(imagePath);
        entity.setUpdatedAt(new Date());
        
        EletronicComponentEntity savedEntity = repository.save(entity);
        return EletronicComponentMapper.toDomain(savedEntity);
    }
}
