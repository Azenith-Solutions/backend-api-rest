package com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.category;

import io.swagger.v3.oas.annotations.media.Schema;

public record CategoryCreateRequest(
    @Schema(description = "Category name", example = "Resistores")
    String nome
) {}
