package com.azenithsolutions.backendapirest.v1.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor
public class RegisterRequestDTO {
    private String fullName;
    private String lastName;
    private String email;
}
