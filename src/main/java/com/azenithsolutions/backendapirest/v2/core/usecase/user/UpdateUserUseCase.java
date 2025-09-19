package com.azenithsolutions.backendapirest.v2.core.usecase.user;

import com.azenithsolutions.backendapirest.v2.core.domain.model.role.Role;
import com.azenithsolutions.backendapirest.v2.core.domain.model.user.User;
import com.azenithsolutions.backendapirest.v2.core.domain.model.user.valueobject.Email;
import com.azenithsolutions.backendapirest.v2.core.domain.model.user.valueobject.FullName;
import com.azenithsolutions.backendapirest.v2.core.domain.model.user.valueobject.Password;
import com.azenithsolutions.backendapirest.v2.core.domain.repository.RoleGateway;
import com.azenithsolutions.backendapirest.v2.core.domain.repository.UserGateway;
import com.azenithsolutions.backendapirest.v2.core.usecase.user.command.CreateUserCommand;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.user.UserUpdateRequestDTO;

import java.time.LocalDate;
import java.util.Objects;

public class UpdateUserUseCase {
    private final UserGateway userGateway;
    private final RoleGateway roleGateway;

    public UpdateUserUseCase(UserGateway userGateway, RoleGateway roleGateway) {
        this.userGateway = userGateway;
        this.roleGateway = roleGateway;
    }

    public User execute(Integer id, UserUpdateRequestDTO body){
        User user = userGateway.findById(id);
        if(Objects.isNull(user)){
            throw new RuntimeException("Usuário não encontrado");
        }

        Role role = roleGateway.getById(body.role());
        if (role == null) {
            throw new RuntimeException("Função não encontrada");
        }

        user.setFullName(FullName.create(body.fullName()));
        user.setEmail(Email.create(body.email()));
        user.setPassword(Password.create(body.password()));
        user.setFkFuncao(role);
        user.setCreatedAt(LocalDate.now());
        user.setUpdatedAt(LocalDate.now());

        return userGateway.save(user);
    }
}
