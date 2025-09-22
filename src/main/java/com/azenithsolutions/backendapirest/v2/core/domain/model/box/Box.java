package com.azenithsolutions.backendapirest.v2.core.domain.model.box;

import com.azenithsolutions.backendapirest.v1.model.Component;//apagardps de criar component V2

import java.util.List;

public class Box {
    private Long idCaixa;
    private String nomeCaixa;
    private List<Component> fkComponente;

    public Box(){

    }

    private Box(Long idCaixa, String nomeCaixa, List<Component> fkComponente) {
        this.idCaixa = idCaixa;
        this.nomeCaixa = nomeCaixa;
        this.fkComponente = fkComponente;
    }

    public static Box criarNovo(String nomeCaixa, List<Component> fkComponente) {
        validarCamposObrigatorios(nomeCaixa);

        return new Box(
                null,
                nomeCaixa,
                fkComponente
        );
    }

    private static void validarCamposObrigatorios(String nomeCaixa) {
        if (nomeCaixa == null || nomeCaixa.isBlank()) {
            throw new IllegalArgumentException("O nome da caixa é obrigatório.");
        }
    }

    public static Box recriar(Long idCaixa, String nomeCaixa, List<Component> fkComponente) {
        validarCamposObrigatorios(nomeCaixa);

        return new Box(
                idCaixa,
                nomeCaixa,
                fkComponente
        );
    }

    public Long getIdCaixa() {
        return idCaixa;
    }

    public void setIdCaixa(Long idCaixa) {
        this.idCaixa = idCaixa;
    }

    public String getNomeCaixa() {
        return nomeCaixa;
    }

    public void setNomeCaixa(String nomeCaixa) {
        this.nomeCaixa = nomeCaixa;
    }

    public List<Component> getFkComponente() {
        return fkComponente;
    }

    public void setFkComponente(List<Component> fkComponente) {
        this.fkComponente = fkComponente;
    }
}
