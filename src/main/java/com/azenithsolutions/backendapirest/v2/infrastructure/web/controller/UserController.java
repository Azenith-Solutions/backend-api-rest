package com.azenithsolutions.backendapirest.v2.infrastructure.web.controller;

import com.azenithsolutions.backendapirest.v2.core.domain.model.user.User;
import com.azenithsolutions.backendapirest.v2.core.usecase.user.CreateUserUseCase;
import com.azenithsolutions.backendapirest.v2.core.usecase.user.command.CreateUserCommand;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.user.UserResponseDTO;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.mappers.UserEntityMapper;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController("userControllerV2")
@RequestMapping("/v2/user")
@Tag(name = "User Management - v2", description = "Clean architecture endpoint for Users")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class UserController {
    private final CreateUserUseCase createUserUseCase;

    @PostMapping
    public ResponseEntity<Map<String, Object>> createUser(@RequestBody CreateUserCommand command){
        Map<String, Object> response = new HashMap<>();

        try{
            User user = createUserUseCase.execute(command);
            UserResponseDTO userResponse = UserEntityMapper.toResposeDTO(user);

            response.put("message", "Usuário criado com sucesso!");
            response.put("user", userResponse);

            return ResponseEntity.status(201).body(response);
        }catch (IllegalArgumentException exception){
            response.put("message", "Argumento Inválido: %s".formatted(exception.getMessage()));
            return ResponseEntity.status(400).body(response);
        }catch (RuntimeException exception){
            response.put("message", "Error: %s".formatted(exception.getMessage()));
            return ResponseEntity.status(400).body(response);

        }
    }
}
