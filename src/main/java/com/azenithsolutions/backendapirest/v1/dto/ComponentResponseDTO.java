package com.azenithsolutions.backendapirest.v1.dto;

import com.azenithsolutions.backendapirest.v1.model.Component;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ComponentResponseDTO {
    private String idHardWareTech;
    private String caixa;
    private String partNumber;
    private int quantidade;
    private Boolean flagML;
    private String codigoML;
    private Boolean flagVerificado;
    private String condicao;
    private String observacao;
    private String descricao;

    public ComponentResponseDTO(Component component) {
        this.idHardWareTech = component.getIdHardWareTech();
        this.caixa = component.getCaixa();
        this.partNumber = component.getPartNumber();
        this.quantidade = component.getQuantidade();
        this.flagML = component.getFlagML();
        this.codigoML = component.getCodigoML();
        this.flagVerificado = component.getFlagVerificado();
        this.condicao = component.getCondicao();
        this.observacao = component.getObservacao();
        this.descricao = component.getDescricao();
    }
}
