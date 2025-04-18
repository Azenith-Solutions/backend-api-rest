package com.azenithsolutions.backendapirest.v1.controller;

import com.azenithsolutions.backendapirest.config.WebClientConfig;
import com.azenithsolutions.backendapirest.v1.dto.AiGeminiRequest;
import com.azenithsolutions.backendapirest.v1.dto.AiGeminiResponse;
import com.azenithsolutions.backendapirest.v1.dto.ApiResponseDTO;
import com.azenithsolutions.backendapirest.v1.model.ChatMessage;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
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
    public ResponseEntity<ApiResponseDTO<?>> getGemini(@RequestBody AiGeminiRequest body, HttpServletRequest request) {
        try {
            if (body.getMessage() == null || body.getMessage().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        new ApiResponseDTO<>(
                                LocalDateTime.now(),
                                HttpStatus.BAD_REQUEST.value(),
                                "Bad Request",
                                List.of("Message is empty"),
                                request.getRequestURI()
                        )
                );
            }

            String apiUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + geminiApiKey;

            Map<String, Object> requestBody;

            if (body.getHistory() != null && !body.getHistory().isEmpty()) {
                StringBuilder contextBuilder = new StringBuilder();
                contextBuilder.append("Essa é uma conversa entre um user e um assistant, responda como um assistant.\n\n");

                List<ChatMessage> messagesExceptLast = body.getHistory();
                for (ChatMessage message : messagesExceptLast) {
                    String role = message.getRole().equals("user") ? "User" : "Assistant";
                    contextBuilder.append(role).append(": ").append(message.getContent()).append("\n");
                }

                contextBuilder.append("User: ").append(body.getMessage()).append("\n");
                contextBuilder.append("Assistant:");

                requestBody = Map.of(
                        "contents", List.of(
                                Map.of("parts", List.of(Map.of("text", contextBuilder.toString())))
                        )
                );
            } else {
                requestBody = Map.of(
                        "contents", List.of(
                                Map.of("parts", List.of(Map.of("text", body.getMessage())))
                        )
                );
            }

            String responseText = webClientBuilder.build()
                    .post()
                    .uri(apiUrl)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .map(json -> json.at("/candidates/0/content/parts/0/text").asText())
                    .block();

            responseText = responseText.trim();

            return ResponseEntity.ok(new ApiResponseDTO<>(
                    LocalDateTime.now(),
                    HttpStatus.OK.value(),
                    "Success",
                    new AiGeminiResponse(responseText),
                    request.getRequestURI()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ApiResponseDTO<>(
                            LocalDateTime.now(),
                            HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Internal Server Error",
                            List.of(e.getMessage()),
                            request.getRequestURI()
                    )
            );
        }
    }
}
