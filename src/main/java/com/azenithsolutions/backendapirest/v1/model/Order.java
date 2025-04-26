package com.azenithsolutions.backendapirest.v1.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "pedido")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @Column(name = "id_pedido")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPedido;

    @Column(name = "codigo")
    private String codigo;

    @Column(name = "nome_comprador")
    private String nomeComprador;

    @Column(name = "email_comprador")
    private String emailComprador;

    @Column(name = "tel_celular")
    private String telCelular;

    @Column(name = "status")
    private String status;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "updated_at")
    private LocalDate updatedAt;
    
    @ManyToOne
    @JoinColumn(name = "fk_empresa", referencedColumnName = "id_empresa")
    private Enterprise empresa;
    
    @OneToMany(mappedBy = "fkPedido")
    @JsonBackReference
    private List<Item> itens;
}
