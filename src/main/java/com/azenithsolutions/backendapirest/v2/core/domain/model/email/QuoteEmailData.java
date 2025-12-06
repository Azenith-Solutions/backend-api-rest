package com.azenithsolutions.backendapirest.v2.core.domain.model.email;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuoteEmailData {
    private String quoteId;
    private String currentDate;
    private String currentTime;
    private String name;
    private String email;
    private String telefone;
    private Boolean isPJ;
    private String cnpj;
    private String content;
    private List<QuoteItem> items;
}
