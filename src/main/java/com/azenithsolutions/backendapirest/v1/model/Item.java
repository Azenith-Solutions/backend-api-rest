package com.azenithsolutions.backendapirest.v1.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="item")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    @Column(name = "id_item")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idItem;

    @ManyToOne
    @JoinColumn(name = "fkComponente")
    @JsonManagedReference
    private Component fkComponente;

    @ManyToOne
    @JoinColumn(name = "fkPedido")
    @JsonManagedReference
    private Order fkPedido;

    @Column(name = "quantidade")
    private Integer quantidade;
}
