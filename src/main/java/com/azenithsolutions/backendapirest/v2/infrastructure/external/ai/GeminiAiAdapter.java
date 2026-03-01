package com.azenithsolutions.backendapirest.v2.infrastructure.external.ai;

import com.azenithsolutions.backendapirest.v1.service.ai.GeminiService;
import com.azenithsolutions.backendapirest.v2.core.domain.repository.AiAssistantGateway;
import org.springframework.stereotype.Service;

@Service
public class GeminiAiAdapter implements AiAssistantGateway {

    private final GeminiService geminiService;

    public GeminiAiAdapter(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    @Override
    public String generateContent(String prompt) {
        return geminiService.generateContent(prompt).block();
    }
}
