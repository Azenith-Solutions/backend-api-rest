package com.azenithsolutions.backendapirest.v1.dto.auth;

import com.azenithsolutions.backendapirest.v1.model.Role;

public record LoginResponseDTO(String email, String name, Role fkFuncao, String token) { }
