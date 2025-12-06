package com.azenithsolutions.backendapirest.v2.infrastructure.config.bean;

import com.azenithsolutions.backendapirest.v2.core.domain.model.email.QuoteEmailTemplateBuilder;
import com.azenithsolutions.backendapirest.v2.core.domain.repository.EmailGateway;
import com.azenithsolutions.backendapirest.v2.core.usecase.email.SendEmailUseCase;
import com.azenithsolutions.backendapirest.v2.core.usecase.email.SendQuoteEmailUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmailUseCaseBeanConfig {
    
    @Bean
    SendEmailUseCase sendEmailUseCase(EmailGateway gateway) { 
        return new SendEmailUseCase(gateway); 
    }
    
    @Bean
    QuoteEmailTemplateBuilder quoteEmailTemplateBuilder() {
        return new QuoteEmailTemplateBuilder();
    }
    
    @Bean
    SendQuoteEmailUseCase sendQuoteEmailUseCase(EmailGateway gateway, QuoteEmailTemplateBuilder templateBuilder) {
        return new SendQuoteEmailUseCase(gateway, templateBuilder);
    }
}
