package com.azenithsolutions.backendapirest.v2.infrastructure.web.controller;

import com.azenithsolutions.backendapirest.v2.core.domain.model.ai.AiAssistantResponse;
import com.azenithsolutions.backendapirest.v2.core.usecase.ai.ProcessAiChatUseCase;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.ai.AiChatRequestDTO;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.ai.AiChatResponseDTO;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.shared.ApiResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Tag(name = "AI Assistant - v2", description = "Endpoints for the AI-powered stock management assistant")
@RestController("aiAssistantControllerV2")
@RequestMapping("/v2/ai/assistant")
@RequiredArgsConstructor
public class AiAssistantController {

    private final ProcessAiChatUseCase processAiChat;

    @PostMapping("/chat")
    @Operation(summary = "Send a message to the AI assistant")
    public ResponseEntity<ApiResponseDTO<?>> chat(
            @Valid @RequestBody AiChatRequestDTO request,
            HttpServletRequest httpRequest) {
        try {
            AiAssistantResponse result = processAiChat.execute(
                    request.getMessage(),
                    request.getHistory() != null ? request.getHistory() : new ArrayList<>()
            );

            AiChatResponseDTO responseDTO = new AiChatResponseDTO(
                    result.getResponse(),
                    result.getType()
            );

            HttpStatus status = "ERROR".equals(result.getType()) ? HttpStatus.SERVICE_UNAVAILABLE : HttpStatus.OK;

            return ResponseEntity.status(status).body(new ApiResponseDTO<>(
                    LocalDateTime.now(),
                    status.value(),
                    status == HttpStatus.OK ? "Success" : result.getResponse(),
                    responseDTO,
                    httpRequest.getRequestURI()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponseDTO<>(
                    LocalDateTime.now(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Ocorreu um erro interno inesperado.",
                    null,
                    httpRequest.getRequestURI()
            ));
        }
    }
}
