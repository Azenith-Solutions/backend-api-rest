package com.azenithsolutions.backendapirest.v2.core.usecase.user.command;

import java.time.LocalDate;

public record CreateUserCommand(String fullName, String email, String password, String profilePicture, Boolean status, Long fkFuncao, LocalDate createdAt, LocalDate updatedAt) {
}
