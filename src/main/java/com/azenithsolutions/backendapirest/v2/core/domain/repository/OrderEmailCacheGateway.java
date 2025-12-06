package com.azenithsolutions.backendapirest.v2.core.domain.repository;

import com.azenithsolutions.backendapirest.v2.core.domain.model.email.QuoteEmailData;

import java.util.Optional;

public interface OrderEmailCacheGateway {
    void save(String orderCode, QuoteEmailData emailData);
    
    Optional<QuoteEmailData> findByOrderCode(String orderCode);
    
    void delete(String orderCode);
}
