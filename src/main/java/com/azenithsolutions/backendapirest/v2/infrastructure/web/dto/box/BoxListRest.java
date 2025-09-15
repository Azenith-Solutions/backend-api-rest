package com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.box;

import io.swagger.v3.oas.annotations.media.Schema;

public record BoxListRest(
        @Schema(description = "Box identifier", example = "1") Long id,
        @Schema(description = "Box name", example = "CAIXA 1") String caixa
) {}
