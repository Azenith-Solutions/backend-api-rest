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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_item")
    private Long idItem;

    @ManyToOne
    @JoinColumn(name = "fk_componente")
    @JsonManagedReference
    private Component fkComponente;

    @ManyToOne
    @JoinColumn(name = "fk_pedido")
    @JsonManagedReference
    private Order fkPedido;

    @Column(name = "quantidade")
    private Integer quantidade;
}
