package com.azenithsolutions.backendapirest.v2.core.usecase.user.command;

import java.time.LocalDate;

public record CreateUserCommand(String fullName, String email, String password, Long fkFuncao) {
}
