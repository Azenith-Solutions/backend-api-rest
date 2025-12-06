package com.azenithsolutions.backendapirest.v2.infrastructure.web.controller;

import com.azenithsolutions.backendapirest.v2.core.usecase.email.SendEmailUseCase;
import com.azenithsolutions.backendapirest.v2.core.usecase.email.SendQuoteEmailUseCase;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.EmailBudgetRest;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.QuoteEmailRest;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.mappers.EmailRestMapper;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.mappers.QuoteEmailRestMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/v2/emails")
@Tag(name = "Email Management - v2", description = "Clean architecture endpoint for Emails")
@RequiredArgsConstructor
public class EmailBudgetController {
    private final SendEmailUseCase send;
    private final SendQuoteEmailUseCase sendQuoteEmail;

    @Operation(summary = "Enviar e-mail de orçamento", description = "Envia um e-mail de orçamento (v2 clean architecture)")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "E-mail enviado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "500", description = "Erro interno")
    })
    @PostMapping("/send")
    public ResponseEntity<String> sendEmail(@Valid @RequestBody EmailBudgetRest restBudget) {
        String response = send.execute(EmailRestMapper.toDomain(restBudget));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @Operation(summary = "Enviar e-mail de cotação", description = "Envia um e-mail de cotação com template HTML gerado automaticamente")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "E-mail de cotação enviado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "500", description = "Erro interno")
    })
    @PostMapping("/send-quote")
    public ResponseEntity<String> sendQuoteEmail(@Valid @RequestBody QuoteEmailRest quoteEmailRest) {
        String response = sendQuoteEmail.execute(QuoteEmailRestMapper.toDomain(quoteEmailRest));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
