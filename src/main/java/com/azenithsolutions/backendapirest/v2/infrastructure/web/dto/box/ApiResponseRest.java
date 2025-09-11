package com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.box;

import java.time.LocalDateTime;

public record ApiResponseRest<T>(
        LocalDateTime timestamp,
        int status,
        String message,
        T data
) {}
