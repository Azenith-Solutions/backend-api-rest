package com.azenithsolutions.backendapirest.v2.infrastructure.web.controller;

import com.azenithsolutions.backendapirest.v2.core.domain.model.user.User;
import com.azenithsolutions.backendapirest.v2.core.usecase.user.*;
import com.azenithsolutions.backendapirest.v2.core.usecase.user.command.CreateUserCommand;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.user.UserResponseDTO;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.mappers.UserEntityMapper;
import io.swagger.v3.oas.annotations.Operation;
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
    private final DeleteUserUseCase deleteUserUserCase;
    private final GetUserByIdUseCase getUserByIdUseCase;
    private final ListUserUseCase listUserUseCase;
    private final UpdateUserUseCase updateUserUseCase;

    @GetMapping
    @Operation(summary = "List users", description = "Returns all users (v2 clean architecture)")
    public ResponseEntity<List<UserResponseDTO>> getAll(){
        List<User> userList = listUserUseCase.execute();
        List<UserResponseDTO> userResponseDTOList = userList.stream().map(UserEntityMapper::toResposeDTO).toList();
        return ResponseEntity.status(200).body(userResponseDTOList);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by id", description = "Returns users by id (v2 clean architecture)")
    public ResponseEntity<Map<String, Object>> getById(@RequestParam(value = "id")Integer id){
        Map<String, Object> response = new HashMap<>();
        try {
            User user = getUserByIdUseCase.execute(id);
            UserResponseDTO userResponse = UserEntityMapper.toResposeDTO(user);
            response.put("message", "Usuário encontrado!");
            response.put("user", userResponse);
            return ResponseEntity.status(200).body(response);
        }catch (RuntimeException exception){
            response.put("message", "Usuário não encontrado!");
            response.put("status", 404);
            return ResponseEntity.status(404).body(response);
        }

    }

    @PostMapping
    @Operation(summary = "Create user", description = "Returns user create (v2 clean architecture)")
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

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user by id", description = "Delete user (v2 clean architecture)")
    public ResponseEntity<String> deleteUser(@RequestParam(value = "id") Integer id){
        try {
            deleteUserUserCase.execute(id);
            return ResponseEntity.status(204).build();
        }catch (RuntimeException exception){
            return ResponseEntity.status(400).body("Error: %s".formatted(exception.getMessage()));
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update user", description = "Returns new user update (v2 clean architecture)")
    public ResponseEntity<Map<String, Object>> updateUser(@RequestParam(value = "id")Integer id, @RequestBody CreateUserCommand command){
        Map<String, Object> response = new HashMap<>();

        try{
            User user = updateUserUseCase.execute(id, command);
            UserResponseDTO userResponse = UserEntityMapper.toResposeDTO(user);

            response.put("message", "Usuário atualizado com sucesso!");
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
