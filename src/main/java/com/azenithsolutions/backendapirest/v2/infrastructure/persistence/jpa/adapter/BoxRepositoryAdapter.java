package com.azenithsolutions.backendapirest.v2.infrastructure.persistence.jpa.adapter;

import com.azenithsolutions.backendapirest.v2.core.domain.model.box.Box;
import com.azenithsolutions.backendapirest.v2.core.domain.repository.BoxGateway;
import com.azenithsolutions.backendapirest.v2.infrastructure.persistence.jpa.entity.BoxEntity;
import com.azenithsolutions.backendapirest.v2.infrastructure.persistence.jpa.repository.SpringDataBoxRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BoxRepositoryAdapter implements BoxGateway {
    private final SpringDataBoxRepository repository;

    public BoxRepositoryAdapter(SpringDataBoxRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Box> findAll() {
        return repository.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public List<Box> findBoxesGreaterOrAlmostOrInLimitOfComponents() {
    return repository.findBoxesGreaterOrAlmostOrInLimitOfComponents()
        .stream()
        .limit(5)
        .map(this::toDomain)
        .toList();
    }

    @Override
    public int countComponentsInBoxes(Long boxId) {
        return repository.countComponentsInBoxes(boxId);
    }

    private BoxEntity toEntity(Box box) {
        BoxEntity entity = new BoxEntity();
        entity.setIdCaixa(box.getIdCaixa());
        entity.setNomeCaixa(box.getNomeCaixa());
        entity.setFkComponents(box.getFkComponente());
        return entity;
    }

    private Box toDomain(BoxEntity entity){
        Box domain = new Box();
        domain.setIdCaixa(entity.getIdCaixa());
        domain.setNomeCaixa(entity.getNomeCaixa());
        domain.setFkComponente(entity.getFkComponents());
        return domain;
    }
}
