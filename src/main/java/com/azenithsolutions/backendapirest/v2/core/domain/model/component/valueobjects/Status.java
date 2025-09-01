package com.azenithsolutions.backendapirest.v2.core.domain.model.component.valueobjects;

public class Status {
    private final Boolean flagVerificado;
    private final String condicao;
    private final String observacao;

    private Status(Boolean flagVerificado, String condicao, String observacao) {
        this.flagVerificado = flagVerificado;
        this.condicao = condicao;
        this.observacao = observacao;
    }


    public static Status verificado(String condicao, String observacao) {
        if (condicao == null || condicao.trim().isEmpty()) {
            throw new IllegalArgumentException("Condição não pode ser vazia para um componente verificado");
        }
        return new Status(true, condicao, observacao);
    }

    public static Status naoVerificado(String observacao) {
        return new Status(false, "Não verificado", observacao);
    }

    public static Status recriar(Boolean flagVerificado, String condicao, String observacao) {
        return new Status(flagVerificado, condicao, observacao);
    }


    public Boolean getFlagVerificado() {
        return flagVerificado;
    }

    public String getCondicao() {
        return condicao;
    }

    public String getObservacao() {
        return observacao;
    }
}
