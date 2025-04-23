package com.azenithsolutions.backendapirest.v1.controller.auth;

import com.auth0.jwt.exceptions.JWTCreationException;
import com.azenithsolutions.backendapirest.v1.dto.auth.LoginRequestDTO;
import com.azenithsolutions.backendapirest.v1.dto.auth.LoginResponseDTO;
import com.azenithsolutions.backendapirest.v1.dto.auth.RegisterRequestDTO;
import com.azenithsolutions.backendapirest.v1.dto.auth.RegisterResponseDTO;
import com.azenithsolutions.backendapirest.v1.dto.shared.ApiResponseDTO;
import com.azenithsolutions.backendapirest.v1.model.User;
import com.azenithsolutions.backendapirest.v1.service.auth.TokenService;
import com.azenithsolutions.backendapirest.v1.service.component.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "Authentication - v1", description = "Endpoints to authenticate with JWT Token validation")
@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @Operation(summary = "Sign in", description = "User sign in validation")
    @PostMapping("/login")
    public ResponseEntity<ApiResponseDTO<?>> authenticate(@Valid @RequestBody LoginRequestDTO body, HttpServletRequest request) {
        try {
            if (body.getEmail() == null || body.getPassword() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        new ApiResponseDTO<>(
                                LocalDateTime.now(),
                                HttpStatus.BAD_REQUEST.value(),
                                "Bad Request",
                                List.of("Email and password are required"),
                                request.getRequestURI()
                        )
                );
            }

            User user = userService.findUserByEmail(body.getEmail());

            if (user == null || !passwordEncoder.matches(body.getPassword(), user.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                        new ApiResponseDTO<>(
                                LocalDateTime.now(),
                                HttpStatus.UNAUTHORIZED.value(),
                                "Unauthorized",
                                List.of("Invalid credentials"),
                                request.getRequestURI()
                        )
                );
            }

            String token = this.tokenService.generateToken(user);

            return ResponseEntity.status(HttpStatus.OK).body(
                    new ApiResponseDTO<>(
                            LocalDateTime.now(),
                            HttpStatus.OK.value(),
                            "Success",
                            new LoginResponseDTO(user.getEmail(), token),
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
    @PostMapping("/register")
    public ResponseEntity<ApiResponseDTO<?>> registerUser(@RequestBody RegisterRequestDTO body, HttpServletRequest request) {
        try {
            if (body.getFullName() == null || body.getEmail() == null || body.getPassword() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        new ApiResponseDTO<>(
                                LocalDateTime.now(),
                                HttpStatus.BAD_REQUEST.value(),
                                "Bad Request",
                                List.of("Full name, email, and password are required"),
                                request.getRequestURI()
                        )
                );
            }

            User user = new User();
            user.setFullName(body.getFullName());
            user.setEmail(body.getEmail());
            user.setPassword(passwordEncoder.encode(body.getPassword()));

            userService.register(user);

            String token = this.tokenService.generateToken(user);

            return ResponseEntity.status(HttpStatus.CREATED).body(
                    new ApiResponseDTO<>(
                            LocalDateTime.now(),
                            HttpStatus.CREATED.value(),
                            "User registered successfully",
                            new RegisterResponseDTO(user.getEmail(), token),
                            request.getRequestURI()
                    )
            );
        } catch (EntityExistsException e) {
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
                            List.of("Something went wrong"),
                            request.getRequestURI()
                    )
            );
        }
    }
}
