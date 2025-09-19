package com.azenithsolutions.backendapirest.v2.infrastructure.persistence.entity;

import com.azenithsolutions.backendapirest.v1.model.Component;//apagar dps q criar componente
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table (name = "caixa")
public class BoxEntity {
    @Column(name = "id_caixa")
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long idCaixa;

    @Column(name = "nome_caixa")
    private String nomeCaixa;

    @OneToMany(mappedBy = "fkCaixa", cascade = CascadeType.ALL)
    @JsonBackReference
    private List<Component> fkComponents;

}
