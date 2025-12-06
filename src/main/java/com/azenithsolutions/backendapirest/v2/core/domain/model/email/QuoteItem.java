package com.azenithsolutions.backendapirest.v2.core.domain.model.email;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuoteItem {
    private String nomeComponente;
    private Integer quantidadeCarrinho;
    private String descricao;
}
