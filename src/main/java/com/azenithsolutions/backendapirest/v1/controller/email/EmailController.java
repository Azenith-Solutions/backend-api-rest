package com.azenithsolutions.backendapirest.v1.controller.email;

import com.azenithsolutions.backendapirest.v1.dto.shared.ApiResponseDTO;
import com.azenithsolutions.backendapirest.v1.model.EmailRequest;
import com.azenithsolutions.backendapirest.v1.service.email.EmailService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Tag(name = "Email Sender - v1", description = "Endpoints to send emails")
@RestController
@RequestMapping("/v1/emails")
public class EmailController {
    @Autowired
    private EmailService emailService;

    @PostMapping("/send")
    public ResponseEntity<ApiResponseDTO<?>> sendEmail(@RequestBody EmailRequest emailToSend, HttpServletRequest request) {
        try {
            System.out.println("Entrou no controller, estou chamando a service!");

            String emailSent = emailService.sendEmail(emailToSend).block();

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(
                            new ApiResponseDTO<>(
                                    LocalDateTime.now(),
                                    HttpStatus.CREATED.value(),
                                    "Email enviado!",
                                    emailSent,
                                    request.getRequestURI()
                            )
                    );
        }
        catch (Exception e) {
            System.err.println("=== ERRO NO CONTROLLER AO ENVIAR EMAIL ===");
            System.err.println("Error type: " + e.getClass().getName());
            System.err.println("Error message: " + e.getMessage());
            System.err.println("Stack trace:");
            e.printStackTrace();

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            new ApiResponseDTO<>(
                                    LocalDateTime.now(),
                                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                    "Falha ao enviar email: " + e.getMessage(),
                                    null,
                                    request.getRequestURI()
                            )
                    );
        }
    }
}
