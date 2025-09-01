package com.azenithsolutions.backendapirest.v2.infrastructure.config;

import com.azenithsolutions.backendapirest.v2.core.domain.repository.EletronicComponentGateway;
import com.azenithsolutions.backendapirest.v2.core.domain.repository.OrderGateway;
import com.azenithsolutions.backendapirest.v2.core.usecase.components.*;
import com.azenithsolutions.backendapirest.v2.core.usecase.order.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EletronicComponentBeanConfig {
    @Bean
    CreateEletronicComponentUseCase createEletronicComponentUseCase(EletronicComponentGateway gateway) { return new CreateEletronicComponentUseCase(gateway); }

    @Bean
    UpdateEletronicComponentUseCase updateEletronicComponentUseCase(EletronicComponentGateway gateway) { return new UpdateEletronicComponentUseCase(gateway); }

    @Bean
    GetEletronicComponentByIdUseCase getEletronicComponentByIdUseCase(EletronicComponentGateway gateway) { return new GetEletronicComponentByIdUseCase(gateway); }

    @Bean
    GetEletronicComponentUseCase getEletronicComponentUseCase(EletronicComponentGateway gateway) { return new GetEletronicComponentUseCase(gateway); }

    @Bean
    DeleteEletronicComponentUseCase deleteEletronicComponentUseCase(EletronicComponentGateway gateway) { return new DeleteEletronicComponentUseCase(gateway); }
}
