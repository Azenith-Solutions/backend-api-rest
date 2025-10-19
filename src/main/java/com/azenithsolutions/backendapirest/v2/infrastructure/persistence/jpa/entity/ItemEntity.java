package com.azenithsolutions.backendapirest.v2.infrastructure.persistence.jpa.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="item")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemEntity {
    @Column(name = "id_item")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idItem;

    @ManyToOne
    @JoinColumn(name = "fkComponente")
    @JsonManagedReference
    private EletronicComponentEntity fkComponente;

    @ManyToOne
    @JoinColumn(name = "fkPedido")
    @JsonManagedReference
    private OrderEntity fkPedido;

    @Column(name = "quantidade")
    private Integer quantidadeCarrinho;
}
