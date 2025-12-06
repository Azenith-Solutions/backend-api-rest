package com.azenithsolutions.backendapirest.v2.infrastructure.web.mappers;

import com.azenithsolutions.backendapirest.v2.core.domain.model.email.QuoteEmailData;
import com.azenithsolutions.backendapirest.v2.core.domain.model.email.QuoteItem;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.QuoteEmailRest;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.QuoteItemRest;

import java.util.stream.Collectors;

public class QuoteEmailRestMapper {
    
    private QuoteEmailRestMapper() {
        throw new IllegalStateException("Utility class");
    }
    
    public static QuoteEmailData toDomain(QuoteEmailRest rest) {
        return QuoteEmailData.builder()
                .quoteId(rest.getQuoteId())
                .currentDate(rest.getCurrentDate())
                .currentTime(rest.getCurrentTime())
                .name(rest.getName())
                .email(rest.getEmail())
                .telefone(rest.getTelefone())
                .isPJ(rest.getIsPJ())
                .cnpj(rest.getCnpj())
                .content(rest.getContent())
                .items(rest.getItems().stream()
                        .map(QuoteEmailRestMapper::toQuoteItemDomain)
                        .collect(Collectors.toList()))
                .build();
    }
    
    private static QuoteItem toQuoteItemDomain(QuoteItemRest rest) {
        return QuoteItem.builder()
                .nomeComponente(rest.getNomeComponente())
                .quantidadeCarrinho(rest.getQuantidadeCarrinho())
                .descricao(rest.getDescricao())
                .build();
    }
}
