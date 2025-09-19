package com.azenithsolutions.backendapirest.v2.core.usecase.email;

import com.azenithsolutions.backendapirest.v2.core.domain.model.email.EmailBudget;
import com.azenithsolutions.backendapirest.v2.core.domain.repository.EmailGateway;

public class SendEmailUseCase {
    private final EmailGateway gateway;

    public SendEmailUseCase(EmailGateway gateway) {
        this.gateway = gateway;
    }

    public String execute(EmailBudget budget) {
        boolean hasEmailBeenSent = gateway.sendEmail(budget);

        if (!hasEmailBeenSent) throw new IllegalStateException("Email não foi enviado com sucesso!");

        return "Email enviado com sucesso!";
    }
}
