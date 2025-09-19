package com.azenithsolutions.backendapirest.v2.core.domain.model.role;

import com.azenithsolutions.backendapirest.v2.core.domain.model.user.User;

import java.util.List;

public class Role {
    private Long idFuncao;

    private String funcao;


    private Role(Long idFuncao, String funcao) {
        this.idFuncao = idFuncao;
        this.funcao = funcao;
    }

    public static Role create(Long idFuncao, String funcao){
        if(idFuncao == null){
            throw new IllegalArgumentException("Role ID cannot be null");
        }else if(idFuncao <= 1) {
            throw new IllegalArgumentException("Role ID must be greater than 1");
        }
        return new Role(idFuncao, funcao);
    }

    public Long getIdFuncao() {
        return idFuncao;
    }

    public String getFuncao() {
        return funcao;
    }

}
