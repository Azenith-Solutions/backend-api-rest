package com.azenithsolutions.backendapirest.v1.model;

import lombok.Data;

@Data
public class EmailRequest {
    private String toEmail;
    private String toName;
    private String subject;
    private String content;
}
