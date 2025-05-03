package com.azenithsolutions.backendapirest.v1.observer;

import com.azenithsolutions.backendapirest.v1.model.EmailRequest;

public interface Notify {
    void notify(EmailRequest emailRequest);
}
