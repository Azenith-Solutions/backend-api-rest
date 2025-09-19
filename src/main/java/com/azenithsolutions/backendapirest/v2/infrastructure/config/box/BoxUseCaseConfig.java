package com.azenithsolutions.backendapirest.v2.infrastructure.config.box;

import com.azenithsolutions.backendapirest.v2.core.domain.repository.BoxGateway;
import com.azenithsolutions.backendapirest.v2.core.usecase.box.FindLimitBoxesUseCase;
import com.azenithsolutions.backendapirest.v2.core.usecase.box.ListBoxesUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BoxUseCaseConfig {
    @Bean
    ListBoxesUseCase listBoxesUseCase(BoxGateway repository) { return new ListBoxesUseCase(repository); }

    @Bean
    FindLimitBoxesUseCase findLimitBoxesUseCase(BoxGateway repository) { return new FindLimitBoxesUseCase(repository); }
}
