package com.azenithsolutions.backendapirest.v2.infrastructure.web.controller;

import com.azenithsolutions.backendapirest.v2.core.domain.model.user.User;
import com.azenithsolutions.backendapirest.v2.core.usecase.user.CreateUserUseCase;
import com.azenithsolutions.backendapirest.v2.core.usecase.user.ListUserUseCase;
import com.azenithsolutions.backendapirest.v2.core.usecase.user.command.CreateUserCommand;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.user.UserResponseDTO;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.mappers.UserEntityMapper;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController("userControllerV2")
@RequestMapping("/v2/user")
@Tag(name = "User Management - v2", description = "Clean architecture endpoint for Users")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class UserController {
    private final CreateUserUseCase createUserUseCase;
    private final ListUserUseCase listUserUseCase;

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAll(){
        List<User> userList = listUserUseCase.execute();
        List<UserResponseDTO> userResponseDTOList = userList.stream().map(UserEntityMapper::toResposeDTO).toList();
        return ResponseEntity.status(200).body(userResponseDTOList);
    }

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
