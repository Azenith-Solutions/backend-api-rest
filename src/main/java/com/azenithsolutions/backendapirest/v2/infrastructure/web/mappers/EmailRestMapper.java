package com.azenithsolutions.backendapirest.v2.infrastructure.web.mappers;

import com.azenithsolutions.backendapirest.v2.core.domain.model.email.EmailBudget;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.EmailBudgetRest;

public class EmailRestMapper {
    public static EmailBudgetRest toRest(EmailBudget budget) {
        if (budget == null) return null;

        EmailBudgetRest rest = new EmailBudgetRest();
        rest.setToEmail(budget.getToEmail());
        rest.setToName(budget.getToName());
        rest.setSubject(budget.getSubject());
        rest.setContent(budget.getContent());
        return  rest;
    }

    public static EmailBudget toDomain(EmailBudgetRest rest) {
        if (rest == null) return null;

        EmailBudget budget = new EmailBudget();
        budget.setToEmail(rest.getToEmail());
        budget.setToName(rest.getToName());
        budget.setSubject(rest.getSubject());
        budget.setContent(rest.getContent());
        return budget;
    }
}
