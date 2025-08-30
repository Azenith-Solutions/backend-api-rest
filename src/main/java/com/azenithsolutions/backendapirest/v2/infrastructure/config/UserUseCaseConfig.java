package com.azenithsolutions.backendapirest.v2.infrastructure.config;

import com.azenithsolutions.backendapirest.v2.core.usecase.user.CreateUserUseCase;
import com.azenithsolutions.backendapirest.v2.core.usecase.user.DeleteUserUseCase;
import com.azenithsolutions.backendapirest.v2.core.usecase.user.GetUserByIdUseCase;
import com.azenithsolutions.backendapirest.v2.core.usecase.user.ListUserUseCase;
import com.azenithsolutions.backendapirest.v2.infrastructure.persistence.adapter.RoleRepositoryAdapter;
import com.azenithsolutions.backendapirest.v2.infrastructure.persistence.adapter.UserRespositoryAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserUseCaseConfig {
    @Bean
    CreateUserUseCase createUserUseCase(UserRespositoryAdapter userRespositoryAdapter, RoleRepositoryAdapter roleRepositoryAdapter){
        return new CreateUserUseCase(userRespositoryAdapter, roleRepositoryAdapter);
    }

    @Bean
    ListUserUseCase listUserUseCase(UserRespositoryAdapter userRespositoryAdapter){
        return new ListUserUseCase(userRespositoryAdapter);
    }

    @Bean
    DeleteUserUseCase deleteUserUseCase(UserRespositoryAdapter userRespositoryAdapter){
        return new DeleteUserUseCase(userRespositoryAdapter);

    }

    @Bean
    GetUserByIdUseCase getUserByIdUseCase(UserRespositoryAdapter userRespositoryAdapter){
        return new GetUserByIdUseCase(userRespositoryAdapter);

    }
}
