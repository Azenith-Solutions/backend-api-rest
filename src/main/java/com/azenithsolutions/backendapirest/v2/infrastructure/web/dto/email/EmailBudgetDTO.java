package com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.email;

import com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.item.ItemRequestDTO;

import java.util.List;

public class EmailBudgetDTO {
    private String toEmail;
    private String toName;
    private String quoteId;
    private String telefone;
    private String cnpj;
    private List<ItemRequestDTO> items;
    private String observacoes;
}
