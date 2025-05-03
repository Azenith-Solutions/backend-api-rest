package com.azenithsolutions.backendapirest.v1.service.email;

import com.azenithsolutions.backendapirest.v1.model.EmailRequest;
import com.azenithsolutions.backendapirest.v1.observer.EmailNotification;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
@Transactional
public class EmailService {
    @Value("${brevo.key}")
    private String apiKey;

    @Value("${brevo.url}")
    private String apiUrl;

    private final WebClient webClient;
    private final EmailNotification emailNotication;

    public EmailService(WebClient.Builder webClientBuilder, EmailNotification emailNotication) {
        this.webClient = webClientBuilder.build();
        this.emailNotication = new EmailNotification();
    }

    public Mono<String> sendEmail(EmailRequest request) {
        Map<String, Object> payload = Map.of(
                "sender", Map.of("name", "HardwareTech", "email", "hardwaretech@hardwaretech.com.br"), // Alterar para email HardwareTech
                "to", new Map[]{Map.of("email", request.getToEmail(), "name", request.getToName())},
                "subject", request.getSubject(),
                "htmlContent", "<html><body>" + request.getContent() + "</body></html>"
        );

        return webClient.post()
                .uri(apiUrl)
                .header("api-key", apiKey)
                .header("Content-Type", "application/json")
                .bodyValue(payload)
                .retrieve() // Inicia recuperação da resposta
                .bodyToMono(String.class)
                .onErrorResume(e -> {
                    System.err.println("Error sending email: " + e.getMessage());
                    return Mono.error(new RuntimeException("Failed to send email"));
                });
    }
}
