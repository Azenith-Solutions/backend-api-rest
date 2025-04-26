package com.azenithsolutions.backendapirest.v1.dto.auth;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor
public class RegisterRequestDTO {
    @NotBlank(message = "Full name cannot be empty")
    @Size(max = 100, message = "Full name must be less than 100 characters")
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Only letters and spaces are allowed")
    private String fullName;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Email is not valid")
    @Size(max = 100, message = "Email must be less than 100 characters")
    @Pattern(regexp = ".*@.*\\..*", message = "Email must contain @ and domain")
    private String email;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 8, max = 100, message = "Password has to be between 8-100 characters")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+={}:;\"'<>,.?~`-])[A-Za-z\\d!@#$%^&*()_+={}:;\"'<>,.?~`-]{8,}$", message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, one special character, and be at least 8 characters long.")
    private String password;

    @NotNull(message = "Role ID cannot be null")
    @Min(value = 1, message = "Role ID must be greater than 1")
    private Long role;

}
