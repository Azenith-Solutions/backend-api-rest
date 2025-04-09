package com.azenithsolutions.backendapirest.v1.controller;

import com.azenithsolutions.backendapirest.config.WebClientConfig;
import com.azenithsolutions.backendapirest.v1.dto.AiGeminiRequest;
import com.azenithsolutions.backendapirest.v1.dto.AiGeminiResponse;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/ai")
public class AiController {
    @Autowired
    private WebClient.Builder webClientBuilder;

    @Value("${gemini.api.key}")
    private String geminiApiKey;


    @PostMapping("/gemini/chat")
    public ResponseEntity<?> getGemini(@RequestBody AiGeminiRequest request) {
        try{
            if(request.getMessage() == ""){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Message is empty");
            }

            String apiUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key="+geminiApiKey;
            String prompt = request.getMessage();
            Map<String, Object> requestBody = Map.of(
                    "contents", List.of(
                            Map.of("parts", List.of(Map.of("text", prompt)))
                    )
            );

            String responseText = webClientBuilder.build()
                    .post()
                    .uri(apiUrl)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .map(json -> json.at("/candidates/0/content/parts/0/text").asText())
                    .block();

            return ResponseEntity.ok(new AiGeminiResponse(responseText));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
