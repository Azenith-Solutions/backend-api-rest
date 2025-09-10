package com.azenithsolutions.backendapirest.v2.infrastructure.config.bean;

import com.azenithsolutions.backendapirest.v2.core.domain.repository.EmailGateway;
import com.azenithsolutions.backendapirest.v2.core.usecase.email.SendEmailUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmailUseCaseBeanConfig {
    @Bean
    SendEmailUseCase sendEmailUseCase(EmailGateway gateway) { return new SendEmailUseCase(gateway); }
}
