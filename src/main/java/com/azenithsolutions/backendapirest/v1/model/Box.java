package com.azenithsolutions.backendapirest.v1.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name="caixa")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Box {
    @Column(name = "id_caixa")
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long idCaixa;

    @Column(name = "nome_caixa")
    private String nomeCaixa;
    
    @OneToMany(mappedBy = "fkCaixa", cascade = CascadeType.ALL)
    @JsonBackReference
    private List<Component> components;
}
