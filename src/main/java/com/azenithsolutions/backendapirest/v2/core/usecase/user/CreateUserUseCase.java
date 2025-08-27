package com.azenithsolutions.backendapirest.v2.core.usecase.user;

import com.azenithsolutions.backendapirest.v2.core.domain.model.role.Role;
import com.azenithsolutions.backendapirest.v2.core.domain.model.user.User;
import com.azenithsolutions.backendapirest.v2.core.domain.model.user.valueobject.Email;
import com.azenithsolutions.backendapirest.v2.core.domain.model.user.valueobject.FullName;
import com.azenithsolutions.backendapirest.v2.core.domain.model.user.valueobject.Password;
import com.azenithsolutions.backendapirest.v2.core.domain.repository.RoleGateway;
import com.azenithsolutions.backendapirest.v2.core.domain.repository.UserGateway;
import com.azenithsolutions.backendapirest.v2.core.usecase.user.command.CreateUserCommand;

public class CreateUserUseCase {

    private final UserGateway userGateway;
    private final RoleGateway roleGateway;

    public CreateUserUseCase(UserGateway userGateway, RoleGateway roleGateway) {
        this.userGateway = userGateway;
        this.roleGateway = roleGateway;
    }

    public User execute(CreateUserCommand createUserCommand){
        if(userGateway.existsByEmail(createUserCommand.email())){
            throw new RuntimeException("Já existe um usuário com este email");
        }

        Role role = roleGateway.existsById(createUserCommand.fkFuncao());
        if (role == null) {
            throw new RuntimeException("Função não encontrada");
        }

        User user = User.create(
                FullName.create(createUserCommand.fullName()),
                Email.create(createUserCommand.email()),
                Password.create(createUserCommand.password()),
                createUserCommand.profilePicture(),
                createUserCommand.status(),
                role,
                createUserCommand.createdAt(),
                createUserCommand.updatedAt()
        );

        return userGateway.save(user);
    }
}
