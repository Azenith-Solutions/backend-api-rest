package com.azenithsolutions.backendapirest.v2.core.domain.repository;

import com.azenithsolutions.backendapirest.v2.core.domain.model.email.EmailBudget;

public interface EmailGateway {
    boolean sendEmail(EmailBudget budget);
}
