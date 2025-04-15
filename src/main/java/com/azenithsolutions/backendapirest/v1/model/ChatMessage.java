package com.azenithsolutions.backendapirest.v1.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
public class ChatMessage {
    private String role;
    private String content;

}
