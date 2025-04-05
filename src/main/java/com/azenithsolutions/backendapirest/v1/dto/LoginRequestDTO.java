package com.azenithsolutions.backendapirest.v1.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class LoginRequestDTO {
    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Email is not valid")
    @Pattern(regexp = ".*@.*\\..*", message = "Email must contain @ and domain")
    @Size(max = 100, message = "Email must be less than 100 characters")
    private String email;

    @NotBlank(message = "Password cannot be empty")
    @Size(max = 255, message = "Password must be less than 100 characters")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,}$",
            message = "Password needs to have: - One capital letter | - One lowercase letter | - One number | - One special character")

    private String password;
}
