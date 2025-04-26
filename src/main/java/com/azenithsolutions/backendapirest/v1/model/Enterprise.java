package com.azenithsolutions.backendapirest.v1.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name="empresa")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Enterprise {
    @Column(name = "id_empresa")
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long idEmpresa;

    @Column(name = "nome_empresa")
    private String cnpj;

    @Column(name = "nome")
    private String nome;

    @Column(name = "qntd_solicitacoes")
    private int qntdSolicitacoes;
    
    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL)
    private List<Order> pedidos;
}
