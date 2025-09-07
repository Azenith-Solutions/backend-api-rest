package com.azenithsolutions.backendapirest.v2.infrastructure.config;

import com.azenithsolutions.backendapirest.v2.core.usecase.auth.LoginUserUserCase;
import com.azenithsolutions.backendapirest.v2.core.usecase.auth.RegisterUserUseCase;
import com.azenithsolutions.backendapirest.v2.infrastructure.persistence.adapter.RoleRepositoryAdapter;
import com.azenithsolutions.backendapirest.v2.infrastructure.persistence.adapter.UserRespositoryAdapter;
import com.azenithsolutions.backendapirest.v2.infrastructure.security.adapter.TokenGatewayAdapter;
import com.azenithsolutions.backendapirest.v2.infrastructure.storage.adapter.ImageStorageGatewayAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AuthUseCaseConfig {
    @Bean
    RegisterUserUseCase registerUserUseCase(UserRespositoryAdapter userRespositoryAdapter, RoleRepositoryAdapter roleRepositoryAdapter, TokenGatewayAdapter tokenGatewayAdapter, ImageStorageGatewayAdapter imageStorageGatewayAdapter, PasswordEncoder passwordEncoder){
        return new RegisterUserUseCase(userRespositoryAdapter, roleRepositoryAdapter, tokenGatewayAdapter, imageStorageGatewayAdapter, passwordEncoder);
    }

    @Bean
    LoginUserUserCase loginUserUserCase(UserRespositoryAdapter userRespositoryAdapter, TokenGatewayAdapter tokenGatewayAdapter, PasswordEncoder passwordEncoder){
        return new LoginUserUserCase(userRespositoryAdapter, tokenGatewayAdapter, passwordEncoder);
    }
}
