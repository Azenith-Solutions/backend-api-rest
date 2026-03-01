package com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.ai;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AiChatResponseDTO {
    private String response;
    private String type;
}
