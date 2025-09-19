package com.azenithsolutions.backendapirest.v2.infrastructure.persistence.adapter;

import com.azenithsolutions.backendapirest.v2.core.domain.model.email.EmailBudget;
import com.azenithsolutions.backendapirest.v2.core.domain.repository.EmailGateway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EmailGatewayAdapter implements EmailGateway {
    @Value("${brevo.url}")
    private String apiUrl;

    private final WebClient brevoClient;

    public EmailGatewayAdapter(@Qualifier("brevoWebClient") WebClient brevoClient) {
        this.brevoClient = brevoClient;
    }

    @Override
    public boolean sendEmail(EmailBudget budget) {
        Map<String, Object> payload = buildPayload(budget);
        try {
            brevoClient.post()
                    .uri(apiUrl)
                    .bodyValue(payload)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Map<String, Object> buildPayload(EmailBudget budget) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("sender", Map.of(
                "name", "HardwareTech",
                "email", "hardwaretech@hardwaretech.com.br"
        ));
        payload.put("to", List.of(Map.of(
                "email", budget.getToEmail(),
                "name", budget.getToName()
        )));
        payload.put("subject", budget.getSubject());
        payload.put("htmlContent", "<html><body>" + budget.getContent() + "</body></html>");
        return payload;
    }
}
