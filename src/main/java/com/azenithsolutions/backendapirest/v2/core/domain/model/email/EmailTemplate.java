package com.azenithsolutions.backendapirest.v2.core.domain.model.email;


public class EmailTemplate {
    private String htmlContent;
    private String plainTextContent;

    public EmailTemplate() {
    }

    public EmailTemplate(String htmlContent, String plainTextContent) {
        this.htmlContent = htmlContent;
        this.plainTextContent = plainTextContent;
    }

    public String getHtmlContent() {
        return htmlContent;
    }

    public void setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
    }

    public String getPlainTextContent() {
        return plainTextContent;
    }

    public void setPlainTextContent(String plainTextContent) {
        this.plainTextContent = plainTextContent;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String htmlContent;
        private String plainTextContent;

        public Builder htmlContent(String htmlContent) {
            this.htmlContent = htmlContent;
            return this;
        }

        public Builder plainTextContent(String plainTextContent) {
            this.plainTextContent = plainTextContent;
            return this;
        }

        public EmailTemplate build() {
            return new EmailTemplate(htmlContent, plainTextContent);
        }
    }
}
