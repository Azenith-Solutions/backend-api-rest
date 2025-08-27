package com.azenithsolutions.backendapirest.v2.core.domain.model.role;

import com.azenithsolutions.backendapirest.v2.core.domain.model.user.User;

import java.util.List;

public class Role {
    private Long idFuncao;

    private String funcao;

    private List<User> users;

    private Role(Long idFuncao, String funcao, List<User> users) {
        this.idFuncao = idFuncao;
        this.funcao = funcao;
        this.users = users;
    }

    public static Role create(Long idFuncao, String funcao, List<User> users){
        if(idFuncao == null){
            throw new IllegalArgumentException("Role ID cannot be null");
        }else if(idFuncao <= 1) {
            throw new IllegalArgumentException("Role ID must be greater than 1");
        }
        return new Role(idFuncao, funcao, users);
    }
}
