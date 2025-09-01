package com.azenithsolutions.backendapirest.v2.core.domain.model.email;

public class EmailBudget {
    private String toEmail;
    private String toName;
    private String subject;
    private String content;

    public EmailBudget() {
    }

    public EmailBudget(String toEmail, String toName, String subject, String content) {
        this.toEmail = toEmail;
        this.toName = toName;
        this.subject = subject;
        this.content = content;
    }

    public String getToEmail() {
        return toEmail;
    }

    public String getToName() {
        return toName;
    }

    public String getSubject() {
        return subject;
    }

    public String getContent() {
        return content;
    }

    public void setToEmail(String toEmail) {
        this.toEmail = toEmail;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
