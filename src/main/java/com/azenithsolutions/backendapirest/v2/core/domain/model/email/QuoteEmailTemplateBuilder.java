package com.azenithsolutions.backendapirest.v2.core.domain.model.email;

import java.time.Year;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class QuoteEmailTemplateBuilder {
    
    public EmailTemplate buildQuoteEmailTemplate(QuoteEmailData data) {
        String htmlContent = generateHtmlTemplate(data);
        String plainTextContent = generatePlainTextTemplate(data);
        
        return EmailTemplate.builder()
                .htmlContent(htmlContent)
                .plainTextContent(plainTextContent)
                .build();
    }
    
    private String generateHtmlTemplate(QuoteEmailData data) {
        String itemsList = generateItemsListHtml(data);
        
        return String.format("""
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Solicitação de Cotação</title>
</head>
<body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333; margin: 0; padding: 0; background-color: #f4f4f4;">
    <table width="100%%" cellpadding="0" cellspacing="0" style="max-width: 650px; margin: 0 auto; background-color: #ffffff; border-radius: 5px; box-shadow: 0 0 10px rgba(0,0,0,0.1);">
        <tr>
            <td style="padding: 20px;">
                <!-- Header -->
                <table width="100%%" cellpadding="0" cellspacing="0">
                    <tr>
                        <td style="background-color: #5F1516; padding: 20px; text-align: center; border-radius: 5px 5px 0 0;">
                            <h1 style="color: #ffffff; margin: 0; font-size: 24px;">Solicitação de Cotação</h1>
                        </td>
                    </tr>
                </table>
                
                <!-- Quote ID and Date -->
                <table width="100%%" cellpadding="0" cellspacing="0" style="margin-top: 20px; background-color: #f9f9f9; border-left: 4px solid #5F1516;">
                    <tr>
                        <td style="padding: 15px;">
                            <p style="margin: 0; font-weight: bold; font-size: 16px;">ID de cotação: %s</p>
                            <p style="margin: 5px 0 0 0; color: #666;">Data: %s às %s</p>
                        </td>
                    </tr>
                </table>
                
                <!-- Customer Information -->
                <table width="100%%" cellpadding="0" cellspacing="0" style="margin-top: 20px;">
                    <tr>
                        <td>
                            <h2 style="color: #5F1516; border-bottom: 1px solid #ddd; padding-bottom: 8px; font-size: 18px;">Informações do Cliente</h2>
                            %s
                            <p><strong>Email:</strong> %s</p>
                            %s
                        </td>
                    </tr>
                </table>
                
                <!-- Requested Items -->
                <table width="100%%" cellpadding="0" cellspacing="0" style="margin-top: 20px;">
                    <tr>
                        <td>
                            <h2 style="color: #5F1516; border-bottom: 1px solid #ddd; padding-bottom: 8px; font-size: 18px;">Itens Solicitados</h2>
                            <table width="100%%" cellpadding="0" cellspacing="0" style="border-collapse: collapse;">
                                <thead>
                                    <tr style="background-color: #f2f2f2;">
                                        <th style="padding: 10px; text-align: left; border-bottom: 2px solid #ddd;">#</th>
                                        <th style="padding: 10px; text-align: left; border-bottom: 2px solid #ddd;">Item</th>
                                        <th style="padding: 10px; text-align: left; border-bottom: 2px solid #ddd;">Qtd</th>
                                        <th style="padding: 10px; text-align: left; border-bottom: 2px solid #ddd;">Descrição</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    %s
                                </tbody>
                            </table>
                        </td>
                    </tr>
                </table>
                
                <!-- Client Message -->
                %s
                
                <!-- Additional Notes -->
                <table width="100%%" cellpadding="0" cellspacing="0" style="margin-top: 30px; background-color: #f5f5f5; border-radius: 4px;">
                    <tr>
                        <td style="padding: 15px;">
                            <p style="margin: 0; font-size: 14px;"><strong>Nota:</strong> Esta é uma solicitação automática gerada pelo sistema. Analisar os itens e enviar a cotação ao cliente o mais breve possível.</p>
                        </td>
                    </tr>
                </table>
                
                <!-- Footer -->
                <table width="100%%" cellpadding="0" cellspacing="0" style="margin-top: 30px; border-top: 1px solid #ddd; text-align: center;">
                    <tr>
                        <td style="padding: 20px;">
                            <p style="margin: 0; color: #666; font-size: 12px;">© %d HardwareTech. Todos os direitos reservados.</p>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
    </table>
</body>
</html>""",
                data.getQuoteId(),
                data.getCurrentDate(),
                data.getCurrentTime(),
                generateCustomerInfo(data),
                data.getEmail(),
                generatePhoneInfo(data),
                itemsList,
                generateClientMessage(data),
                Year.now().getValue()
        );
    }
    
    private String generateCustomerInfo(QuoteEmailData data) {
        if (Boolean.TRUE.equals(data.getIsPJ())) {
            return String.format(
                "<p><strong>Empresa:</strong> %s</p>\n                                <p><strong>CNPJ:</strong> %s</p>",
                data.getName(),
                data.getCnpj()
            );
        } else {
            return String.format(
                "<p><strong>Solicitante:</strong> %s</p>\n                                <p><strong>Tipo:</strong> Pessoa Física</p>",
                data.getName()
            );
        }
    }
    
    private String generatePhoneInfo(QuoteEmailData data) {
        if (data.getTelefone() != null && !data.getTelefone().isEmpty()) {
            return String.format("<p><strong>Telefone:</strong> %s</p>", data.getTelefone());
        }
        return "";
    }
    
    private String generateItemsListHtml(QuoteEmailData data) {
        return IntStream.range(0, data.getItems().size())
                .mapToObj(i -> {
                    QuoteItem item = data.getItems().get(i);
                    String description = item.getDescricao() != null && !item.getDescricao().isEmpty()
                            ? (item.getDescricao().length() > 60 
                                ? item.getDescricao().substring(0, 60) + "..." 
                                : item.getDescricao())
                            : "Sem descrição";
                    
                    return String.format("""
                                        <tr>
                                          <td style="padding: 8px; border-bottom: 1px solid #ddd;">%d</td>
                                          <td style="padding: 8px; border-bottom: 1px solid #ddd;">%s</td>
                                          <td style="padding: 8px; border-bottom: 1px solid #ddd;">%d</td>
                                          <td style="padding: 8px; border-bottom: 1px solid #ddd;">%s</td>
                                        </tr>""",
                            i + 1,
                            item.getNomeComponente(),
                            item.getQuantidadeCarrinho(),
                            description
                    );
                })
                .collect(Collectors.joining("\n"));
    }
    
    private String generateClientMessage(QuoteEmailData data) {
        if (data.getContent() != null && !data.getContent().isEmpty()) {
            String formattedContent = data.getContent().replace("\n", "<br>");
            return String.format("""
                
                <table width="100%%" cellpadding="0" cellspacing="0" style="margin-top: 20px;">
                    <tr>
                        <td>
                            <h2 style="color: #5F1516; border-bottom: 1px solid #ddd; padding-bottom: 8px; font-size: 18px;">Mensagem do Cliente</h2>
                            <p style="background-color: #f9f9f9; padding: 10px; border-radius: 4px;">%s</p>
                        </td>
                    </tr>
                </table>""", formattedContent);
        }
        return "";
    }
    
    private String generatePlainTextTemplate(QuoteEmailData data) {
        StringBuilder sb = new StringBuilder();
        
        sb.append("SOLICITAÇÃO DE COTAÇÃO\n");
        sb.append(String.format("ID: %s\n", data.getQuoteId()));
        sb.append(String.format("Data: %s às %s\n\n", data.getCurrentDate(), data.getCurrentTime()));
        
        sb.append("INFORMAÇÕES DO CLIENTE\n");
        if (Boolean.TRUE.equals(data.getIsPJ())) {
            sb.append(String.format("Empresa: %s\n", data.getName()));
            sb.append(String.format("CNPJ: %s\n", data.getCnpj()));
        } else {
            sb.append(String.format("Solicitante: %s\n", data.getName()));
            sb.append("Tipo: Pessoa Física\n");
        }
        sb.append(String.format("Email: %s\n", data.getEmail()));
        if (data.getTelefone() != null && !data.getTelefone().isEmpty()) {
            sb.append(String.format("Telefone: %s\n", data.getTelefone()));
        }
        
        sb.append("\nITENS SOLICITADOS\n");
        for (int i = 0; i < data.getItems().size(); i++) {
            QuoteItem item = data.getItems().get(i);
            sb.append(String.format("%d. %s - Qtd: %d\n", 
                    i + 1, 
                    item.getNomeComponente(), 
                    item.getQuantidadeCarrinho()));
        }
        
        if (data.getContent() != null && !data.getContent().isEmpty()) {
            sb.append(String.format("\nMENSAGEM DO CLIENTE\n%s\n", data.getContent()));
        }
        
        sb.append("\nNota: Esta é uma solicitação automática gerada pelo sistema. Analisar os itens e enviar a cotação ao cliente o mais breve possível.\n");
        sb.append(String.format("\n© %d HardwareTech. Todos os direitos reservados.", Year.now().getValue()));
        
        return sb.toString();
    }
}
