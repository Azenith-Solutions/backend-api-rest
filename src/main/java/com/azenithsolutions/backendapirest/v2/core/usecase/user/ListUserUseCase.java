package com.azenithsolutions.backendapirest.v2.core.usecase.user;

import com.azenithsolutions.backendapirest.v2.core.domain.model.user.User;
import com.azenithsolutions.backendapirest.v2.core.domain.repository.UserGateway;

import java.util.List;

public class ListUserUseCase {
    private final UserGateway userGateway;

    public ListUserUseCase(UserGateway userGateway) {
        this.userGateway = userGateway;
    }

    public List<User> execute(){
        return userGateway.findAll();
    }
}
