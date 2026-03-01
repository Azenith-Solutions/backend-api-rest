package com.azenithsolutions.backendapirest.v2.infrastructure.config.bean;

import com.azenithsolutions.backendapirest.v2.core.usecase.ai.ProcessAiChatUseCase;
import com.azenithsolutions.backendapirest.v2.infrastructure.external.ai.GeminiAiAdapter;
import com.azenithsolutions.backendapirest.v2.infrastructure.persistence.jpa.adapter.DatabaseQueryAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiAssistantBeanConfig {

    @Bean
    ProcessAiChatUseCase processAiChatUseCase(GeminiAiAdapter geminiAiAdapter, DatabaseQueryAdapter databaseQueryAdapter) {
        return new ProcessAiChatUseCase(geminiAiAdapter, databaseQueryAdapter);
    }
}
