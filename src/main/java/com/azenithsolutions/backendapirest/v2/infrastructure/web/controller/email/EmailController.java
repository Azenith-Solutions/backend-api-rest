package com.azenithsolutions.backendapirest.v2.infrastructure.web.controller.email;

import com.azenithsolutions.backendapirest.v2.core.usecase.email.SendEmailUseCase;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.EmailBudgetRest;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v2/emails")
@Tag(name = "Email Management - v2", description = "Clean architecture endpoint for Emails")
@RequiredArgsConstructor
public class EmailController {
    private final SendEmailUseCase send;

    public ResponseEntity<String> sendEmail(EmailBudgetRest budget) {

    }
}
