package com.azenithsolutions.backendapirest.v2.infrastructure.persistence.cache;

import com.azenithsolutions.backendapirest.v2.core.domain.model.email.QuoteEmailData;
import com.azenithsolutions.backendapirest.v2.core.domain.repository.OrderEmailCacheGateway;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Repository
@RequiredArgsConstructor
public class OrderEmailCacheRepository implements OrderEmailCacheGateway {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    
    private static final String KEY_PREFIX = "email:order:";
    private static final long TTL_MINUTES = 30;
    
    @Override
    public void save(String orderCode, QuoteEmailData emailData) {
        String key = KEY_PREFIX + orderCode;
        redisTemplate.opsForValue().set(key, emailData, TTL_MINUTES, TimeUnit.MINUTES);
        System.out.println("Email data saved to cache for order: " + orderCode);
    }
    
    @Override
    public Optional<QuoteEmailData> findByOrderCode(String orderCode) {
        String key = KEY_PREFIX + orderCode;
        Object result = redisTemplate.opsForValue().get(key);
        
        if (result == null) {
            return Optional.empty();
        }
        
        return Optional.of((QuoteEmailData) result);
    }
    
    @Override
    public void delete(String orderCode) {
        String key = KEY_PREFIX + orderCode;
        redisTemplate.delete(key);
        System.out.println("Email data deleted from cache for order: " + orderCode);
    }
}
