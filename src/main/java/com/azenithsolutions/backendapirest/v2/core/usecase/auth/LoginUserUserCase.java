package com.azenithsolutions.backendapirest.v2.core.usecase.auth;

import com.azenithsolutions.backendapirest.v2.core.domain.model.user.User;
import com.azenithsolutions.backendapirest.v2.core.domain.repository.TokenGateway;
import com.azenithsolutions.backendapirest.v2.core.domain.repository.UserGateway;
import com.azenithsolutions.backendapirest.v2.core.usecase.auth.dto.AuthenticatedUserResponse;
import com.azenithsolutions.backendapirest.v2.core.usecase.auth.dto.LoginUserRequest;
import org.springframework.security.crypto.password.PasswordEncoder;

public class LoginUserUserCase {
    private final UserGateway userGateway;
    private final TokenGateway tokenGateway;
    private final PasswordEncoder passwordEncoder;

    public LoginUserUserCase(UserGateway userGateway, TokenGateway tokenGateway, PasswordEncoder passwordEncoder) {
        this.userGateway = userGateway;
        this.tokenGateway = tokenGateway;
        this.passwordEncoder = passwordEncoder;
    }
    public AuthenticatedUserResponse execute(LoginUserRequest request) {
        User user = userGateway.findByEmail(request.email());
        if(user == null){
            new RuntimeException("Invalid credentials");
        }

        if (!passwordEncoder.matches(request.password(), user.getPassword().getValue())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = tokenGateway.generateToken(user);

        return new AuthenticatedUserResponse(user, token);
    }
}
