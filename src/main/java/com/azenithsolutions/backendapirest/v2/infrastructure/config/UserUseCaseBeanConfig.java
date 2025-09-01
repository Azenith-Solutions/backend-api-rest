package com.azenithsolutions.backendapirest.v2.infrastructure.config;

import com.azenithsolutions.backendapirest.v2.core.usecase.user.*;
import com.azenithsolutions.backendapirest.v2.infrastructure.persistence.jpa.adapter.RoleRepositoryAdapter;
import com.azenithsolutions.backendapirest.v2.infrastructure.persistence.jpa.adapter.UserRespositoryAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserUseCaseBeanConfig {
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

    @Bean
    UpdateUserUseCase updateUserUseCase(UserRespositoryAdapter userRespositoryAdapter, RoleRepositoryAdapter roleRepositoryAdapter){
        return new UpdateUserUseCase(userRespositoryAdapter, roleRepositoryAdapter);
    }
}
