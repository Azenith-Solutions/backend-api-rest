package com.azenithsolutions.backendapirest.v2.infrastructure.persistence.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name="usuario")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nome")
    private String fullName;

    @Column(unique = true, name = "email")
    private String email;

    @Column(name = "senha")
    private String password;

    @Column(name = "foto")
    private String profilePicture;

    @Column(name = "status")
    private Boolean status;

    @ManyToOne
    @JoinColumn(name = "fk_funcao")
    @JsonManagedReference
    private RoleEntity fkFuncao;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "updated_at")
    private LocalDate updatedAt;
}
