package com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.shared;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor @NoArgsConstructor
public class ApiResponseDTO<T> {
    private LocalDateTime timestamp;
    private int status;
    private String message;
    private T data;
    private String path;
}
