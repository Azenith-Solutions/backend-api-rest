package com.azenithsolutions.backendapirest.v2.infrastructure.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class QuoteEmailRest {
    @Schema(example = "AZT-2024-1234", description = "ID da cotação (gerado automaticamente se não fornecido)")
    private String quoteId;
    
    @Schema(example = "05/12/2024", description = "Data atual (gerada automaticamente se não fornecida)")
    private String currentDate;
    
    @Schema(example = "14:30:00", description = "Hora atual (gerada automaticamente se não fornecida)")
    private String currentTime;
    
    @NotBlank(message = "Nome é obrigatório")
    @Schema(example = "João Silva")
    private String name;
    
    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ser válido")
    @Schema(example = "joao.silva@empresa.com.br")
    private String email;
    
    @Schema(example = "(11) 98765-4321")
    private String telefone;
    
    @Schema(example = "true", description = "Indica se é pessoa jurídica")
    private Boolean isPJ;
    
    @Schema(example = "12.345.678/0001-90", description = "CNPJ (obrigatório se isPJ=true)")
    private String cnpj;
    
    @Schema(example = "Gostaria de receber informações sobre prazo de entrega e condições de pagamento.")
    private String content;
    
    @NotEmpty(message = "Lista de itens não pode estar vazia")
    @Valid
    @Schema(description = "Lista de itens da cotação")
    private List<QuoteItemRest> items;
}
