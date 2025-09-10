    package com.azenithsolutions.backendapirest.v2.infrastructure.persistence.jpa.adapter;

import com.azenithsolutions.backendapirest.v2.core.domain.model.component.EletronicComponent;
import com.azenithsolutions.backendapirest.v2.core.domain.repository.EletronicComponentGateway;
import com.azenithsolutions.backendapirest.v2.infrastructure.persistence.jpa.entity.EletronicComponentEntity;
import com.azenithsolutions.backendapirest.v2.infrastructure.persistence.jpa.repository.SpringDataEletronicComponentRepository;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.mappers.EletronicComponentMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

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
}
