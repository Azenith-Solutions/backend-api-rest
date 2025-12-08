package com.azenithsolutions.backendapirest.v2.core.usecase.email;

import com.azenithsolutions.backendapirest.v2.core.domain.model.email.*;
import com.azenithsolutions.backendapirest.v2.core.domain.repository.EmailGateway;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class SendQuoteEmailUseCase {
    private final EmailGateway emailGateway;
    private final QuoteEmailTemplateBuilder templateBuilder;
    
    private static final DateTimeFormatter DATE_FORMATTER = 
            DateTimeFormatter.ofPattern("dd/MM/yyyy", new Locale("pt", "BR"));
    private static final DateTimeFormatter TIME_FORMATTER = 
            DateTimeFormatter.ofPattern("HH:mm:ss", new Locale("pt", "BR"));
    
    public SendQuoteEmailUseCase(EmailGateway emailGateway, QuoteEmailTemplateBuilder templateBuilder) {
        this.emailGateway = emailGateway;
        this.templateBuilder = templateBuilder;
    }
    
    public String execute(QuoteEmailData quoteData) {
        validateQuoteData(quoteData);       
        enrichWithDateTime(quoteData);
        
        if (quoteData.getQuoteId() == null || quoteData.getQuoteId().isEmpty()) {
            quoteData.setQuoteId(generateQuoteId());
        }
        
        EmailTemplate template = templateBuilder.buildQuoteEmailTemplate(quoteData);
        EmailBudget emailBudget = EmailBudget.builder()
                .toEmail("azenithsolutions@gmail.com")
                .toName("Azenith Solutions")
                .subject(String.format("Nova Cotação #%s - %s", quoteData.getQuoteId(), quoteData.getName()))
                .content(template.getHtmlContent())
                .build();
        
        boolean hasEmailBeenSent = emailGateway.sendEmail(emailBudget);
        
        if (!hasEmailBeenSent) {
            throw new IllegalStateException("Falha ao enviar email de cotação!");
        }
        
        return "Email de cotação enviado com sucesso!";
    }
    
    private void validateQuoteData(QuoteEmailData data) {
        if (data == null) {
            throw new IllegalArgumentException("Dados da cotação não podem ser nulos");
        }
        
        if (data.getName() == null || data.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do cliente é obrigatório");
        }
        
        if (data.getEmail() == null || data.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email do cliente é obrigatório");
        }
        
        if (data.getItems() == null || data.getItems().isEmpty()) {
            throw new IllegalArgumentException("Lista de itens não pode estar vazia");
        }
        
        if (Boolean.TRUE.equals(data.getIsPJ()) && 
            (data.getCnpj() == null || data.getCnpj().trim().isEmpty())) {
            throw new IllegalArgumentException("CNPJ é obrigatório para pessoa jurídica");
        }
    }
    
    private void enrichWithDateTime(QuoteEmailData data) {
        LocalDateTime now = LocalDateTime.now();
        
        if (data.getCurrentDate() == null || data.getCurrentDate().isEmpty()) {
            data.setCurrentDate(now.format(DATE_FORMATTER));
        }
        
        if (data.getCurrentTime() == null || data.getCurrentTime().isEmpty()) {
            data.setCurrentTime(now.format(TIME_FORMATTER));
        }
    }
    
    private String generateQuoteId() {
        int randomNumber = 1000 + (int) (Math.random() * 9000);
        return String.format("AZT-%d-%04d", LocalDateTime.now().getYear(), randomNumber);
    }
}
