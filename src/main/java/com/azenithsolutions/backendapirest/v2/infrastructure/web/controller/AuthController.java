package com.azenithsolutions.backendapirest.v2.infrastructure.web.controller;

import com.auth0.jwt.exceptions.JWTCreationException;
import com.azenithsolutions.backendapirest.v2.core.usecase.auth.LoginUserUserCase;
import com.azenithsolutions.backendapirest.v2.core.usecase.auth.RegisterUserUseCase;
import com.azenithsolutions.backendapirest.v2.core.usecase.auth.dto.AuthenticatedUserResponse;
import com.azenithsolutions.backendapirest.v2.core.usecase.auth.dto.LoginUserRequest;
import com.azenithsolutions.backendapirest.v2.core.usecase.auth.dto.RegisterUserRequest;
import com.azenithsolutions.backendapirest.v2.core.usecase.auth.dto.RegisteredUserResponse;
import com.azenithsolutions.backendapirest.v2.infrastructure.persistence.entity.UserEntity;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.shared.ApiResponseDTO;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.user.LoginRequestDTO;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.user.LoginResponseDTO;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.user.UserRegisterRequestDTO;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.user.UserRegisterResponseDTO;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.mappers.UserEntityMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@RestController("authControllerV2")
@RequestMapping("/v2/auth")
@Tag(name = "Auth Management - v2", description = "Clean architecture endpoint for Auth")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class AuthController {

    private final RegisterUserUseCase registerUserUseCase;
    private final LoginUserUserCase loginUserUserCase;
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    @Operation(summary = "Sign in", description = "User sign in validation")
    @PostMapping("/login")
    public ResponseEntity<ApiResponseDTO<?>> authenticateUser(@RequestBody LoginRequestDTO body, HttpServletRequest request) {
        try {
            LoginUserRequest loginUserRequest = new LoginUserRequest(
                    body.email(),
                    body.password()
            );

            AuthenticatedUserResponse authenticatedUserResponse = loginUserUserCase.execute(loginUserRequest);

            UserEntity userEntity = UserEntityMapper.toEntity(authenticatedUserResponse.user());
            String token = authenticatedUserResponse.token();
            log.info("Usuário autenticado");

            return ResponseEntity.status(HttpStatus.OK).body(
                    new ApiResponseDTO<>(
                            LocalDateTime.now(),
                            HttpStatus.OK.value(),
                            "Success",
                            new LoginResponseDTO(userEntity.getEmail(), userEntity.getFullName(), userEntity.getFkFuncao(), token),
                            request.getRequestURI()
                    )
            );

        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponseDTO<>(
                            LocalDateTime.now(),
                            HttpStatus.BAD_REQUEST.value(),
                            "Bad Request",
                            List.of("Resource not found"),
                            request.getRequestURI()
                    )
            );
        } catch (JWTCreationException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ApiResponseDTO<>(
                            LocalDateTime.now(),
                            HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Internal Server Error",
                            List.of("Error generating authentication token"),
                            request.getRequestURI()
                    )
            );
        } catch (Exception e) {
            log.error("Error inesperado", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ApiResponseDTO<>(
                            LocalDateTime.now(),
                            HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Internal Server Error",
                            List.of("An unexpected error occurred"),
                            request.getRequestURI()
                    )
            );
        }
    }

    @Operation(summary = "Sign up", description = "User sign up validation")
    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponseDTO<?>> registerUser(
            @RequestPart(value = "data", required = false) UserRegisterRequestDTO myData,
            @RequestPart(value = "file", required = false) MultipartFile file,
            HttpServletRequest request
    ) {
        try {
            RegisterUserRequest useCaseRequest = new RegisterUserRequest(
                    myData.fullName(),
                    myData.email(),
                    myData.password(),
                    myData.role()
            );

            RegisteredUserResponse useCaseResponse = registerUserUseCase.execute(useCaseRequest, file);

            return ResponseEntity.status(HttpStatus.CREATED).body(
                    new ApiResponseDTO<>(
                            LocalDateTime.now(),
                            HttpStatus.CREATED.value(),
                            "User registered successfully",
                            new UserRegisterResponseDTO(useCaseResponse.user().getEmail().getValue(), useCaseResponse.token()),
                            request.getRequestURI()
                    )
            );
        }  catch (EntityExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    new ApiResponseDTO<>(
                            LocalDateTime.now(),
                            HttpStatus.CONFLICT.value(),
                            "Conflict",
                            List.of(e.getMessage()),
                            request.getRequestURI()
                    )
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ApiResponseDTO<>(
                            LocalDateTime.now(),
                            HttpStatus.INTERNAL_SERVER_ERROR.value(),
                            "Internal Server Error",
                            List.of("Erro interno: " + e.getMessage()),
                            request.getRequestURI()
                    )
            );
        }
    }
}
