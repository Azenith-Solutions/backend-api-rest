package com.azenithsolutions.backendapirest.v1.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
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

    @ManyToOne
    @JoinColumn(name = "fk_empresa")
    @JsonManagedReference
    private Company fkEmpresa;

    @OneToMany(mappedBy = "fkPedido")
    @JsonBackReference
    private List<Item> fkItens;

    @Column(name = "nome_comprador")
    private String nomeComprador;

    @Column(name = "email_comprador")
    private String emailComprador;

    @Column(name = "tel_celular")
    private String telCelular;

    @Column(name = "status")
    private String status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
