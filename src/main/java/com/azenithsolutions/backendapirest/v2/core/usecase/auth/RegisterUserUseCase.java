package com.azenithsolutions.backendapirest.v2.core.usecase.auth;

import com.azenithsolutions.backendapirest.v2.core.domain.model.role.Role;
import com.azenithsolutions.backendapirest.v2.core.domain.model.user.User;
import com.azenithsolutions.backendapirest.v2.core.domain.model.user.valueobject.Password;
import com.azenithsolutions.backendapirest.v2.core.domain.repository.ImageStorageGateway;
import com.azenithsolutions.backendapirest.v2.core.domain.repository.RoleGateway;
import com.azenithsolutions.backendapirest.v2.core.domain.repository.TokenGateway;
import com.azenithsolutions.backendapirest.v2.core.domain.repository.UserGateway;
import com.azenithsolutions.backendapirest.v2.core.usecase.auth.dto.RegisterUserRequest;
import com.azenithsolutions.backendapirest.v2.core.usecase.auth.dto.RegisteredUserResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

public class RegisterUserUseCase {
    private final UserGateway userGateway;
    private final RoleGateway roleGateway;
    private final TokenGateway tokenGateway;
    private final ImageStorageGateway imageStorageGateway;
    private final PasswordEncoder passwordEncoder;

    public RegisterUserUseCase(UserGateway userGateway, RoleGateway roleGateway, TokenGateway tokenGateway, ImageStorageGateway imageStorageGateway, PasswordEncoder passwordEncoder) {
        this.userGateway = userGateway;
        this.roleGateway = roleGateway;
        this.tokenGateway = tokenGateway;
        this.imageStorageGateway = imageStorageGateway;
        this.passwordEncoder = passwordEncoder;
    }

    public RegisteredUserResponse execute(RegisterUserRequest request, MultipartFile file){
        // 1. Validar campos obrigatórios
        if (request.fullName() == null || request.email() == null ||
                request.password() == null || request.role() == null) {
            throw new IllegalArgumentException("Missing required fields: fullName, email, password or role");
        }

        // 2. Buscar role
        Role role = roleGateway.getById(request.role());
        if (role == null) {
            throw new IllegalArgumentException("Role not found");
        }

        // 3. Criar usuário com senha codificada
        Password password = Password.create(request.password());
        String encodedPassword = passwordEncoder.encode(password.getValue());

        User user = User.create(
                null,
                request.fullName(),
                request.email(),
                encodedPassword,
                "default",
                true,
                role.getIdFuncao(),
                role.getFuncao(),
                LocalDate.now(),
                LocalDate.now()
        );

        // 4. Salvar imagem, se houver
        if (file != null && !file.isEmpty()) {
            String storedFileName = imageStorageGateway.saveImage(file);
            user.setProfilePicture(storedFileName);
        }

        // 5. Persistir usuário
        User savedUser = userGateway.save(user);

        // 6. Gerar token
        String token = tokenGateway.generateToken(savedUser);

        // 7. Retornar DTO de resposta
        return new RegisteredUserResponse(savedUser, token);

    }
}
