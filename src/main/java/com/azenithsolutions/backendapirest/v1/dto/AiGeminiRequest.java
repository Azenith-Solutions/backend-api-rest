package com.azenithsolutions.backendapirest.v1.dto;

import com.azenithsolutions.backendapirest.v1.model.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class AiGeminiRequest {
    private String message;
    private List<ChatMessage> history;
}
