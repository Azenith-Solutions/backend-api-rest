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
    @Id
    private String idHardWareTech;

    private String caixa;
    private String partNumber;
    private int quantidade;
    private Boolean flagML;
    private String codigoML;
    private Boolean flagVerificado;
    private String condicao;
    private String observacao;
    private String descricao;

}
