package com.azenithsolutions.backendapirest.v2.core.usecase.components;

import com.azenithsolutions.backendapirest.v2.core.domain.repository.EletronicComponentGateway;

import java.util.List;

public class GetCountOfTrueAndFalseFlagMLUseCase {
    private final EletronicComponentGateway gateway;

    public GetCountOfTrueAndFalseFlagMLUseCase(EletronicComponentGateway gateway) {
        this.gateway = gateway;
    }

    public List<Integer> execute() {
        return gateway.countTrueAndFalseFlagML();
    }
}
