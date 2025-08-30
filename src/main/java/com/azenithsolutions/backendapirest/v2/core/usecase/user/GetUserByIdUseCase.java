package com.azenithsolutions.backendapirest.v2.core.usecase.user;

import com.azenithsolutions.backendapirest.v2.core.domain.model.user.User;
import com.azenithsolutions.backendapirest.v2.core.domain.repository.UserGateway;

import java.util.Objects;
import java.util.Optional;

public class GetUserByIdUseCase {
    private final UserGateway userGateway;

    public GetUserByIdUseCase(UserGateway userGateway) {
        this.userGateway = userGateway;
    }

    public User execute(Integer id){
        User user = userGateway.findById(id);
        if(Objects.isNull(user)){
            throw new RuntimeException("Usuário não encontrado");
        }
        return user;
    }
}
