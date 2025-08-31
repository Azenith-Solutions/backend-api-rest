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

    public static User create(Integer id, String fullName, String email, String password, String profilePicture, Boolean status, Long fkFuncao, String funcao, LocalDate createdAt, LocalDate updatedAt){
        return new User(
                id,
                FullName.create(fullName),
                Email.create(email),
                Password.create(password),
                profilePicture,
                status,
                Role.create(fkFuncao, funcao),
                createdAt,
                updatedAt);
    }

    public Integer getId() {
        return id;
    }

    public FullName getFullName() {
        return fullName;
    }

    public Email getEmail() {
        return email;
    }

    public Password getPassword() {
        return password;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public Boolean getStatus() {
        return status;
    }

    public Role getFkFuncao() {
        return fkFuncao;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setFullName(FullName fullName) {
        this.fullName = fullName;
    }

    public void setEmail(Email email) {
        this.email = email;
    }

    public void setPassword(Password password) {
        this.password = password;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public void setFkFuncao(Role fkFuncao) {
        this.fkFuncao = fkFuncao;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }
}
