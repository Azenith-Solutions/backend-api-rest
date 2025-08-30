package com.azenithsolutions.backendapirest.v2.infrastructure.web.controller;

import com.azenithsolutions.backendapirest.v2.core.domain.model.user.User;
import com.azenithsolutions.backendapirest.v2.core.usecase.user.CreateUserUseCase;
import com.azenithsolutions.backendapirest.v2.core.usecase.user.command.CreateUserCommand;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("userControllerV2")
@RequestMapping("/v2/user")
@Tag(name = "User Management - v2", description = "Clean architecture endpoint for Users")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class UserController {
    private final CreateUserUseCase createUserUseCase;

    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody CreateUserCommand command){
        try{
            User user = createUserUseCase.execute(command);
            return ResponseEntity.status(201).body("Usuário criado: %s".formatted(user));
        }catch (IllegalArgumentException exception){
            return ResponseEntity.status(400).body("Argumento Inválido: %s".formatted(exception.getMessage()));
        }catch (RuntimeException exception){
            return ResponseEntity.status(400).body("Argumento Inválifo: %s".formatted(exception.getMessage()));
        }
    }
}
