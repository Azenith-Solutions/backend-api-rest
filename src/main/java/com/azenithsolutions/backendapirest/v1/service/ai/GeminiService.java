package com.azenithsolutions.backendapirest.v1.service.ai;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
public class GeminiService {

    private final WebClient webClient;
    private final String geminiApiUrl;
    private final String geminiApiKey;

    public GeminiService(WebClient.Builder webClientBuilder,
         @Value("${gemini.api.url}") String geminiApiUrl,
         @Value("${gemini.api.key}") String geminiApiKey) {
        this.webClient = webClientBuilder.build();
        this.geminiApiUrl = geminiApiUrl;
        this.geminiApiKey = geminiApiKey;
    }

    public Mono<String> generateContent(String prompt) {
        String apiUrl = geminiApiUrl + geminiApiKey;
        Map<String, Object> requestBody = Map.of(
                "contents", List.of(
                        Map.of("parts", List.of(Map.of("text", prompt)))
                )
        );

        return webClient.post()
                .uri(apiUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .map(json -> json.at("/candidates/0/content/parts/0/text").asText("")) // Provide default empty string
                .onErrorResume(e -> {
                    System.err.println("Error calling Gemini API: " + e.getMessage());
                    return Mono.just("ERROR_API_CALL");
                });
    }
}