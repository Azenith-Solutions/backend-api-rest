package com.azenithsolutions.backendapirest.v2.infrastructure.persistence.jpa.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "componente")
public class EletronicComponentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_componente")
    private Long id;

    @Column(name = "id_hardwaretech")
    private String idHardwaretech;

    @Column(name = "nome_componente")
    private String nome;

    @Column(name = "part_number")
    private String partNumber;

    @Column(name = "quantidade")
    private int quantidade;

    @Column(name = "codigo_ml")
    private String codigoML;

    @Column(name = "imagem")
    private String s3ImagePath;

    @Column(name = "data_ultima_venda")
    @Temporal(TemporalType.DATE)
    private Date dataUltimaVenda;

    @Column(name = "created_at")
    @Temporal(TemporalType.DATE)
    private Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.DATE)
    private Date updatedAt;

    @Column(name = "quantidade_vendido")
    private Integer quantidadeVendido;

    @Column(name = "flag_ml")
    private Boolean flagML;

    @Column(name = "flag_verificado")
    private Boolean flagVerificado;

    @Column(name = "condicao")
    private String condicao;

    @Column(name = "observacao")
    private String observacao;

    @Column(name = "descricao")
    private String descricao;

    @ManyToOne
    @JoinColumn(name = "fk_caixa")
    @JsonManagedReference
    private BoxEntity fkCaixa;

    @ManyToOne
    @JoinColumn(name = "fk_categoria")
    @JsonManagedReference
    private CategoryEntity fkCategoria;
    
    @Column(name = "is_visible_catalog")
    private Boolean isVisibleCatalog;

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdHardwaretech() {
        return idHardwaretech;
    }

    public void setIdHardwaretech(String idHardwaretech) {
        this.idHardwaretech = idHardwaretech;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public String getCodigoML() {
        return codigoML;
    }

    public void setCodigoML(String codigoML) {
        this.codigoML = codigoML;
    }

    public String getS3ImagePath() {
        return s3ImagePath;
    }

    public void setS3ImagePath(String s3ImagePath) {
        this.s3ImagePath = s3ImagePath;
    }

    public Date getDataUltimaVenda() {
        return dataUltimaVenda;
    }

    public void setDataUltimaVenda(Date dataUltimaVenda) {
        this.dataUltimaVenda = dataUltimaVenda;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public Boolean getIsVisibleCatalog() {
        return isVisibleCatalog;
    }

    public void setIsVisibleCatalog(Boolean isVisibleCatalog) {
        this.isVisibleCatalog = isVisibleCatalog;
    }

    public Integer getQuantidadeVendido() {
        return quantidadeVendido;
    }

    public void setQuantidadeVendido(Integer quantidadeVendido) {
        this.quantidadeVendido = quantidadeVendido;
    }

    public Boolean getFlagML() {
        return flagML;
    }

    public void setFlagML(Boolean flagML) {
        this.flagML = flagML;
    }

    public Boolean getFlagVerificado() {
        return flagVerificado;
    }

    public void setFlagVerificado(Boolean flagVerificado) {
        this.flagVerificado = flagVerificado;
    }

    public String getCondicao() {
        return condicao;
    }

    public void setCondicao(String condicao) {
        this.condicao = condicao;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BoxEntity getFkCaixa() {
        return fkCaixa;
    }

    public void setFkCaixa(BoxEntity fkCaixa) {
        this.fkCaixa = fkCaixa;
    }

    public CategoryEntity getFkCategoria() {
        return fkCategoria;
    }

    public void setFkCategoria(CategoryEntity fkCategoria) {
        this.fkCategoria = fkCategoria;
    }
}
