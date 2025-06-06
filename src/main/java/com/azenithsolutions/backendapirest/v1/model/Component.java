package com.azenithsolutions.backendapirest.v1.model;

import com.azenithsolutions.backendapirest.v1.model.enums.ComponentCondition;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name="componente")
@Data
@AllArgsConstructor @NoArgsConstructor
public class Component {
    @Column(name = "id_componente")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idComponente;

    @Column(name = "id_hardwaretech")
    private String idHardWareTech;

    @Column(name = "nome_componente")
    private String nomeComponente;

    @ManyToOne
    @JoinColumn(name = "fk_caixa")
    @JsonManagedReference
    private Box fkCaixa;

    @ManyToOne
    @JoinColumn(name = "fk_categoria")
    @JsonManagedReference
    private Category fkCategoria;

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
    @Enumerated(EnumType.STRING)
    private ComponentCondition condicao;

    @Column(name = "observacao")
    private String observacao;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "imagem")
    private String imagem;

    @Column(name = "data_ultima_venda")
    private LocalDate dataUltimaVenda;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "updated_at")
    private LocalDate updatedAt;

    @Column(name = "quantidade_vendido")
    private Integer quantidadeVendido;

    @Column(name = "is_visible_catalog")
    private Boolean isVisibleCatalog;
    
    @OneToMany(mappedBy = "fkComponente")
    @JsonBackReference
    private List<Item> itens;

    @Override
    public String toString() {
        return "Component{" +
                "id=" + idComponente +
                ", name='" + nomeComponente + "'" +
                ", idHardWareTech='" + idHardWareTech + "'" +
                ", descricao=" + descricao +
                // Referência à Box sem chamar toString()
                ", boxId=" + (fkCaixa != null ? fkCaixa.getIdCaixa() : null) +
                '}';
    }
}
