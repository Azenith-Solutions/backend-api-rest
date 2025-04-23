package com.azenithsolutions.backendapirest.v1.dto.shared;

import lombok.*;

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
