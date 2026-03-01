package com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.ai;

import com.azenithsolutions.backendapirest.v2.core.domain.model.ai.ChatMessage;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AiChatRequestDTO {
    @NotBlank(message = "A mensagem nao pode estar vazia")
    private String message;
    private List<ChatMessage> history;
}
