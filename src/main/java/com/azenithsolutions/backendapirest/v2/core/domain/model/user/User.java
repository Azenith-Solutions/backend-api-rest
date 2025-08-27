package com.azenithsolutions.backendapirest.v2.core.domain.model.user;

import com.azenithsolutions.backendapirest.v2.core.domain.model.role.Role;
import com.azenithsolutions.backendapirest.v2.core.domain.model.user.valueobject.Email;
import com.azenithsolutions.backendapirest.v2.core.domain.model.user.valueobject.FullName;
import com.azenithsolutions.backendapirest.v2.core.domain.model.user.valueobject.Password;

import java.time.LocalDate;

public class User {
    private Integer id;

    private FullName fullName;

    private Email email;

    private Password password;

    private String profilePicture;

    private Boolean status;

    private Role fkFuncao;

    private LocalDate createdAt;

    private LocalDate updatedAt;

    private User(Integer id, FullName fullName, Email email, Password password, String profilePicture, Boolean status, Role fkFuncao, LocalDate createdAt, LocalDate updatedAt) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.profilePicture = profilePicture;
        this.status = status;
        this.fkFuncao = fkFuncao;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static User create(FullName fullName, Email email, Password password, String profilePicture, Boolean status, Role fkFuncao, LocalDate createdAt, LocalDate updatedAt){
        return new User(null, fullName, email, password, profilePicture, status, fkFuncao, createdAt, updatedAt);
    }
}
