package com.azenithsolutions.backendapirest.v2.infrastructure.persistence.jpa.adapter;

import com.azenithsolutions.backendapirest.v2.core.domain.repository.EletronicComponentGateway;
import com.azenithsolutions.backendapirest.v2.infrastructure.persistence.jpa.repository.SpringDataEletronicComponentRepository;
import org.springframework.stereotype.Component;

@Component
public class EletronicComponentAdapter implements EletronicComponentGateway {
    private final SpringDataEletronicComponentRepository repository;

    public EletronicComponentAdapter(SpringDataEletronicComponentRepository repository) {
        this.repository = repository;
    }


    @Override



}
