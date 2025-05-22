package com.azenithsolutions.backendapirest.v1.controller.auth;

import com.auth0.jwt.exceptions.JWTCreationException;
import com.azenithsolutions.backendapirest.v1.dto.auth.LoginRequestDTO;
import com.azenithsolutions.backendapirest.v1.dto.auth.LoginResponseDTO;
import com.azenithsolutions.backendapirest.v1.dto.user.UserRegisterRequestDTO;
import com.azenithsolutions.backendapirest.v1.dto.user.UserRegisterResponseDTO;
import com.azenithsolutions.backendapirest.v1.dto.shared.ApiResponseDTO;
import com.azenithsolutions.backendapirest.v1.model.Role;
import com.azenithsolutions.backendapirest.v1.model.User;
import com.azenithsolutions.backendapirest.v1.service.auth.TokenService;
import com.azenithsolutions.backendapirest.v1.service.files.ImageService;
import com.azenithsolutions.backendapirest.v1.service.user.UserService;
import com.azenithsolutions.backendapirest.v1.utils.CustomMultipartFile;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Tag(name = "Authentication - v1", description = "Endpoints to authenticate with JWT Token validation")
@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    @Autowired
    private final UserService userService;
    @Autowired
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private final TokenService tokenService;
    @Autowired
    private final ImageService imageService;

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
                            new LoginResponseDTO(user.getEmail(), user.getFullName(), user.getFkFuncao(), token),
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
    public ResponseEntity<ApiResponseDTO<?>> registerUser(@Valid @RequestBody UserRegisterRequestDTO body, HttpServletRequest request) {
        try {
            if (body.getFullName() == null || body.getEmail() == null || body.getPassword() == null || body.getRole() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        new ApiResponseDTO<>(
                                LocalDateTime.now(),
                                HttpStatus.BAD_REQUEST.value(),
                                "Bad Request",
                                List.of("Invalid Json format"),
                                request.getRequestURI()
                        )
                );
            }

            User user = new User();
            user.setFullName(body.getFullName());
            user.setEmail(body.getEmail());
            user.setPassword(passwordEncoder.encode(body.getPassword()));
            Optional<Role> role = userService.findRoleById(body.getRole());
            if (role.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        new ApiResponseDTO<>(
                                LocalDateTime.now(),
                                HttpStatus.BAD_REQUEST.value(),
                                "Bad Request",
                                List.of("Role not found"),
                                request.getRequestURI()
                        )
                );
            }
            user.setFkFuncao(role.get());

            userService.register(user);

            String token = this.tokenService.generateToken(user);

            return ResponseEntity.status(HttpStatus.CREATED).body(
                    new ApiResponseDTO<>(
                            LocalDateTime.now(),
                            HttpStatus.CREATED.value(),
                            "User registered successfully",
                            new UserRegisterResponseDTO(user.getEmail(), token),
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

    @PostMapping(value = "/register1", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponseDTO<?>> handleFileAndJson(
            @RequestPart(value = "data", required = false) UserRegisterRequestDTO myData,
            @RequestPart(value = "file", required = false) MultipartFile file,
            HttpServletRequest request
    ) {
        try {
            if (file == null || file.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        new ApiResponseDTO<>(
                                LocalDateTime.now(),
                                HttpStatus.BAD_REQUEST.value(),
                                "Bad Request",
                                List.of("Image file is required"),
                                request.getRequestURI()
                        )
                );
            }

            if (myData == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        new ApiResponseDTO<>(
                                LocalDateTime.now(),
                                HttpStatus.BAD_REQUEST.value(),
                                "Bad Request",
                                List.of("Invalid JSON format"),
                                request.getRequestURI()
                        )
                );
            }

            // debug test
            System.out.println("Arquivo: " + file.getOriginalFilename() + " | Tamanho: " + file.getSize());
            System.out.println("Dados: " + myData.getEmail());

            if(myData.getFullName() == null || myData.getEmail() == null || myData.getPassword() == null || myData.getRole() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        new ApiResponseDTO<>(
                                LocalDateTime.now(),
                                HttpStatus.BAD_REQUEST.value(),
                                "Bad Request",
                                List.of("Invalid Json format"),
                                request.getRequestURI()
                        )
                );
            }

            User user = new User();
            user.setFullName(myData.getFullName());
            user.setEmail(myData.getEmail());
            user.setPassword(passwordEncoder.encode(myData.getPassword()));
            Optional<Role> role = userService.findRoleById(myData.getRole());

            if (role.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        new ApiResponseDTO<>(
                                LocalDateTime.now(),
                                HttpStatus.BAD_REQUEST.value(),
                                "Bad Request",
                                List.of("Role not found"),
                                request.getRequestURI()
                        )
                );
            }
            user.setFkFuncao(role.get());

            String originalFilename = file.getOriginalFilename();
            String timestamp = String.valueOf(System.currentTimeMillis());
            String uniqueFileName = timestamp + "_" + originalFilename;

            MultipartFile renamedFile = new CustomMultipartFile(file, uniqueFileName);

            user.setProfilePicture(renamedFile.getOriginalFilename());

            userService.register(user);

            String fileName = imageService.saveImage(renamedFile);
            System.out.println("File saved with name: " + fileName);
            
            String token = this.tokenService.generateToken(user);

            return ResponseEntity.status(HttpStatus.CREATED).body(
                    new ApiResponseDTO<>(
                            LocalDateTime.now(),
                            HttpStatus.CREATED.value(),
                            "User registered successfully",
                            new UserRegisterResponseDTO(user.getEmail(), token),
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
                            List.of("Erro interno: " + e.getMessage()),
                            request.getRequestURI()
                    )
            );
        }
    }

}
