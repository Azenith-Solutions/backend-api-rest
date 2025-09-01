package com.azenithsolutions.backendapirest.v2.infrastructure.web.controller;

import com.azenithsolutions.backendapirest.v2.core.usecase.email.SendEmailUseCase;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.EmailBudgetRest;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.mappers.EmailRestMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v2/emails")
@Tag(name = "Email Management - v2", description = "Clean architecture endpoint for Emails")
@RequiredArgsConstructor
public class EmailBudgetController {
    private final SendEmailUseCase send;

    @PostMapping
    public ResponseEntity<String> sendEmail(EmailBudgetRest restBudget) {
        String response = send.execute(EmailRestMapper.toDomain(restBudget));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
