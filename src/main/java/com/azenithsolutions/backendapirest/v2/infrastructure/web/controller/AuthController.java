package com.azenithsolutions.backendapirest.v2.infrastructure.web.controller;

import com.azenithsolutions.backendapirest.v2.core.usecase.auth.RegisterUserUseCase;
import com.azenithsolutions.backendapirest.v2.core.usecase.auth.dto.RegisterUserRequest;
import com.azenithsolutions.backendapirest.v2.core.usecase.auth.dto.RegisteredUserResponse;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.shared.ApiResponseDTO;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.user.UserRegisterRequestDTO;
import com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.user.UserRegisterResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityExistsException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
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
