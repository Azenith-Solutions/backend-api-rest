package com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.box;

import io.swagger.v3.oas.annotations.media.Schema;

public record BoxCreateRequest(
    @Schema(description = "Box name", example = "CAIXA 1")
    String nome
) {}
