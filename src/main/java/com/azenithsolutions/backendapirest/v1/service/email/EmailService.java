package com.azenithsolutions.backendapirest.v1.service.email;

import com.azenithsolutions.backendapirest.v1.model.EmailRequest;
import com.azenithsolutions.backendapirest.v1.observer.EmailNotification;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class EmailService {
    @Value("${brevo.url}")
    private String apiUrl;

    private final WebClient brevoWebClient;

    public EmailService(@Qualifier("brevoWebClient") WebClient brevoWebClient) {
        this.brevoWebClient = brevoWebClient;
    }

    public Mono<String> sendEmail(EmailRequest request) {
        System.out.println("Entrou no service, estou montando o objeto de email");
        Map<String, Object> payload = Map.of(
                "sender", Map.of("name", "HardwareTech", "email", "hardwaretech@hardwaretech.com.br"),
                "to", new Map[]{Map.of("email", request.getToEmail(), "name", request.getToName())},
                "subject", request.getSubject(),
                "htmlContent", "<html><body>" + request.getContent() + "</body></html>"
        );

        System.out.println("Payload sendo enviado ao Brevo: " + payload);
        System.out.println("Estou fazendo a requisição para o Brevo");

        return brevoWebClient.post()
                .uri(apiUrl)
                .header("Content-Type", "application/json")
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(String.class)
                .onErrorResume(e -> {
                    if (e instanceof WebClientResponseException) {
                        WebClientResponseException wcre = (WebClientResponseException) e;

                        System.err.println("=== ERRO DE RESPOSTA DA API BREVO ===");
                        System.err.println("Status code: " + wcre.getRawStatusCode());
                        System.err.println("Status text: " + wcre.getStatusText());
                        System.err.println("Response headers: " + wcre.getHeaders());
                        System.err.println("Response body: " + wcre.getResponseBodyAsString());

                        // Log os detalhes da requisição se disponíveis
                        if (wcre.getRequest() != null) {
                            System.err.println("Request URI: " + wcre.getRequest().getURI());
                            System.err.println("Request method: " + wcre.getRequest().getMethod());
                        }
                    } else {
                        System.err.println("=== ERRO GENÉRICO AO ENVIAR EMAIL ===");
                        System.err.println("Error type: " + e.getClass().getName());
                        System.err.println("Error message: " + e.getMessage());
                        System.err.println("Stack trace:");
                        e.printStackTrace();
                    }

                    return Mono.error(new RuntimeException("Failed to send email: " + e.getMessage()));
                });
    }

    public Mono<String> sendEmailWithAttachment(EmailRequest request, MultipartFile file) {
        try {
            String base64File = Base64.getEncoder().encodeToString(file.getBytes());
            Map<String, Object> attachment = Map.of(
                    "content", base64File,
                    "name", file.getOriginalFilename()
            );

            Map<String, Object> payload = Map.of(
                    "sender", Map.of("name", "HardwareTech", "email", "hardwaretech@hardwaretech.com.br"),
                    "to", new Map[]{Map.of("email", request.getToEmail(), "name", request.getToName())},
                    "subject", request.getSubject(),
                    "htmlContent", "<html><body>" + request.getContent() + "</body></html>",
                    "attachment", List.of(attachment)
            );

            return brevoWebClient.post()
                    .uri(apiUrl)
                    .header("Content-Type", "application/json")
                    .bodyValue(payload)
                    .retrieve()
                    .bodyToMono(String.class);
        } catch (IOException e) {
            return Mono.error(new RuntimeException("Erro ao processar o arquivo: " + e.getMessage()));
        }
    }
}
