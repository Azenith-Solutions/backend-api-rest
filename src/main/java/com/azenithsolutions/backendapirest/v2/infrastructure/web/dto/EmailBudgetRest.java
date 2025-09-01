package com.azenithsolutions.backendapirest.v2.infrastructure.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EmailBudgetRest {
    @NotBlank
    private String toEmail;

    @NotBlank
    private String toName;

    @NotBlank
    private String subject;

    @NotBlank
    private String content;
}
