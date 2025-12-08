package com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.order;

import com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.QuoteEmailRest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(name = "OrderWithQuoteRequest", description = "Order creation request with email quote data")
public class OrderWithQuoteRequestDTO {
    
    @NotNull(message = "Order data is required")
    @Valid
    @Schema(description = "Order information")
    private OrderRequestDTO order;
    
    @NotNull(message = "Email quote data is required")
    @Valid
    @Schema(description = "Email quote information to be cached")
    private QuoteEmailRest emailData;
}
