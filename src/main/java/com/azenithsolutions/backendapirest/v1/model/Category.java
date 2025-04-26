package com.azenithsolutions.backendapirest.v1.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name="categoria")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    @Column(name = "id_categoria")
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long idCategoria;

    @Column(name = "nome_categoria")
    private String nomeCategoria;
    
    @OneToMany(mappedBy = "fkCategoria", cascade = CascadeType.ALL)
    @JsonBackReference
    private List<Component> componentes;
}
