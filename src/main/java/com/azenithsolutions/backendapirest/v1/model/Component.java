package com.azenithsolutions.backendapirest.v1.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="Componente")
@Data
@AllArgsConstructor @NoArgsConstructor
public class Component {
    @Column(name = "id_hardwaretech")
    @Id
    private String idHardWareTech;

    @Column(name = "caixa")
    private String caixa;
    @Column(name = "part_number")
    private String partNumber;
    @Column(name = "quantidade")
    private int quantidade;
    @Column(name = "flag_ml")
    private Boolean flagML;
    @Column(name = "codigo_ml")
    private String codigoML;
    @Column(name = "flag_verificado")
    private Boolean flagVerificado;
    @Column(name = "condicao")
    private String condicao;
    @Column(name = "observacao")
    private String observacao;
    @Column(name = "descricao")
    private String descricao;

}
