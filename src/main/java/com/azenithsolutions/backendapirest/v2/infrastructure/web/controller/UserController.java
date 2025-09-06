package com.azenithsolutions.backendapirest.v2.infrastructure.web.controller;

import com.azenithsolutions.backendapirest.v2.core.domain.model.user.User;
import com.azenithsolutions.backendapirest.v2.core.usecase.user.*;
import com.azenithsolutions.backendapirest.v2.core.usecase.user.command.CreateUserCommand;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.shared.ApiResponseDTO;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.user.UserListResponseDTO;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.user.UserResponseDTO;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.user.UserUpdateRequestDTO;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.user.UserUpdateResponseDTO;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.mappers.UserEntityMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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

    @GetMapping()
    @Operation(summary = "List users", description = "Returns all users (v2 clean architecture)")
    public ResponseEntity<ApiResponseDTO<?>> getUsers(HttpServletRequest request) {
        try{
            List<User> users = listUserUseCase.execute();

            List<UserListResponseDTO> userListResponseDTOS = users.stream()
                    .map(UserEntityMapper::toListResposeDTO).toList();

            return ResponseEntity.ok(
                    new ApiResponseDTO<>(
                            LocalDateTime.now(),
                            HttpStatus.OK.value(),
                            "Success",
                            userListResponseDTOS,
                            request.getRequestURI()
                    )
            );
        }catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ApiResponseDTO<>(
                            LocalDateTime.now(),
                            HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Internal Server Error",
                            List.of("An error occurred while processing the request"),
                            request.getRequestURI()
                    )
            );
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update user", description = "Returns new user update (v2 clean architecture)")
    public ResponseEntity<ApiResponseDTO<?>> updateUser(@PathVariable Integer id, @RequestBody UserUpdateRequestDTO body, HttpServletRequest request){
        try{
                User user = updateUserUseCase.execute(id, body);
                UserUpdateResponseDTO responseDTO = UserEntityMapper.toUserUpdateResponseDTO(user);

                return ResponseEntity.status(HttpStatus.OK).body(
                        new ApiResponseDTO<>(
                                LocalDateTime.now(),
                                HttpStatus.OK.value(),
                                "Success",
                                List.of(responseDTO),
                                request.getRequestURI()
                        )
                );

        }catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponseDTO<>(
                            LocalDateTime.now(),
                            HttpStatus.BAD_REQUEST.value(),
                            "Bad Request",
                            e.getMessage(),
                            request.getRequestURI()
                    )
            );
        }catch(EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ApiResponseDTO<>(
                            LocalDateTime.now(),
                            HttpStatus.NOT_FOUND.value(),
                            "Not Found",
                            List.of(e.getMessage()),
                            request.getRequestURI()
                    )
            );

        }catch(EntityExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    new ApiResponseDTO<>(
                            LocalDateTime.now(),
                            HttpStatus.CONFLICT.value(),
                            "Conflict",
                            List.of(e.getMessage()),
                            request.getRequestURI()
                    )
            );
        }catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ApiResponseDTO<>(
                            LocalDateTime.now(),
                            HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Internal Server Error",
                            List.of("An error occurred while processing the request"),
                            request.getRequestURI()
                    )
            );
        }
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
    public ResponseEntity<ApiResponseDTO<?>> deleteUser(@PathVariable Integer id, HttpServletRequest request) {
        try {
            deleteUserUserCase.execute(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(
                    new ApiResponseDTO<>(
                            LocalDateTime.now(),
                            HttpStatus.NO_CONTENT.value(),
                            "No Content",
                            null,
                            request.getRequestURI()
                    )
            );
        }catch(EntityExistsException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ApiResponseDTO<>(
                            LocalDateTime.now(),
                            HttpStatus.NOT_FOUND.value(),
                            "Not Found",
                            List.of(e.getMessage()),
                            request.getRequestURI()
                    )
            );
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ApiResponseDTO<>(
                            LocalDateTime.now(),
                            HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Internal Server Error",
                            List.of("An error occurred while processing the request"),
                            request.getRequestURI()
                    )
            );
        }
    }
}
