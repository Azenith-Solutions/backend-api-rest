package com.azenithsolutions.backendapirest.v1.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class LoginRequestDTO {
    private String email;
    private String password;
}
