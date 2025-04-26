package com.azenithsolutions.backendapirest.v1.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name="funcao")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role {
    @Column(name = "id_funcao")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idFuncao;

    @Column(name = "nome_funcao")
    private String funcao;
    
    @OneToMany(mappedBy = "fkFuncao", cascade = CascadeType.ALL)
    @JsonBackReference
    private List<User> users;
}
