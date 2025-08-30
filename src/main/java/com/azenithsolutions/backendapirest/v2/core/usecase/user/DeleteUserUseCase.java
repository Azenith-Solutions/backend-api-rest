package com.azenithsolutions.backendapirest.v2.core.usecase.user;

import com.azenithsolutions.backendapirest.v2.core.domain.repository.UserGateway;

import java.util.Objects;

public class DeleteUserUseCase {
    private final UserGateway userGateway;

    public DeleteUserUseCase(UserGateway userGateway) {
        this.userGateway = userGateway;
    }

    public void execute(Integer id){
        if(Objects.isNull(userGateway.findById(id))){
            throw new RuntimeException("Usuário não encontrado");
        }
        userGateway.deleteById(id);
    }
}
