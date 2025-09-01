package com.azenithsolutions.backendapirest.v2.infrastructure.config;

import com.azenithsolutions.backendapirest.v2.core.domain.repository.email.EmailGateway;
import com.azenithsolutions.backendapirest.v2.core.domain.repository.order.OrderRepositoryPort;
import com.azenithsolutions.backendapirest.v2.core.usecase.email.SendEmailUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

@Configuration
public class EmailUseCaseConfig {
    @Bean
    SendEmailUseCase sendEmailUseCase(EmailGateway gateway) { return new SendEmailUseCase(gateway); }
}
