package com.azenithsolutions.backendapirest.v1.observer;

import com.azenithsolutions.backendapirest.v1.model.EmailRequest;
import org.springframework.stereotype.Component;

@Component
public class EmailNotification implements Notify {
    @Override
    public void notify(EmailRequest emailRequest) {
        System.out.println("Email sent successfully to: " + emailRequest.getToEmail());
    }
}
