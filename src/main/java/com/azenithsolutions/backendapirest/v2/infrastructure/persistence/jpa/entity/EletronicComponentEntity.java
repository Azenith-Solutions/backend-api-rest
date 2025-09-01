package com.azenithsolutions.backendapirest.v2.infrastructure.persistence.jpa.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "componentes_eletronicos")
public class EletronicComponentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_hardwaretech", unique = true, nullable = false)
    private String idHardwaretech;

    @Column(nullable = false)
    private String nome;

    @Column(name = "part_number")
    private String partNumber;

    @Column(nullable = false)
    private int quantidade;

    @Column(nullable = false)
    private Boolean anunciado;

    @Column(name = "codigo_mercado_livre")
    private String codigoMercadoLivre;

    @Column(name = "s3_image_path")
    private String s3ImagePath;

    @Column(name = "data_ultima_venda")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataUltimaVenda;

    @Column(name = "data_criacao", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataCriacao;

    @Column(name = "data_ultima_atualizacao", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataUltimaAtualizacao;

    // Campos de Categoria
    @Column(name = "categoria_nome")
    private String categoriaNome;

    @Column(name = "categoria_descricao")
    private String categoriaDescricao;

    // Campos de Caixa
    @Column(name = "caixa_codigo")
    private String caixaCodigo;

    @Column(name = "caixa_localizacao")
    private String caixaLocalizacao;

    @Column(name = "caixa_capacidade_maxima")
    private Integer caixaCapacidadeMaxima;

    @Column(name = "caixa_ocupacao_atual")
    private Integer caixaOcupacaoAtual;

    // Campos de Status
    @Column(name = "status_verificado")
    private Boolean statusFlagVerificado;

    @Column(name = "status_condicao")
    private String statusCondicao;

    @Column(name = "status_observacao")
    private String statusObservacao;

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

    public Boolean getAnunciado() {
        return anunciado;
    }

    public void setAnunciado(Boolean anunciado) {
        this.anunciado = anunciado;
    }

    public String getCodigoMercadoLivre() {
        return codigoMercadoLivre;
    }

    public void setCodigoMercadoLivre(String codigoMercadoLivre) {
        this.codigoMercadoLivre = codigoMercadoLivre;
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

    public Date getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public Date getDataUltimaAtualizacao() {
        return dataUltimaAtualizacao;
    }

    public void setDataUltimaAtualizacao(Date dataUltimaAtualizacao) {
        this.dataUltimaAtualizacao = dataUltimaAtualizacao;
    }

    public String getCategoriaNome() {
        return categoriaNome;
    }

    public void setCategoriaNome(String categoriaNome) {
        this.categoriaNome = categoriaNome;
    }

    public String getCategoriaDescricao() {
        return categoriaDescricao;
    }

    public void setCategoriaDescricao(String categoriaDescricao) {
        this.categoriaDescricao = categoriaDescricao;
    }

    public String getCaixaCodigo() {
        return caixaCodigo;
    }

    public void setCaixaCodigo(String caixaCodigo) {
        this.caixaCodigo = caixaCodigo;
    }

    public String getCaixaLocalizacao() {
        return caixaLocalizacao;
    }

    public void setCaixaLocalizacao(String caixaLocalizacao) {
        this.caixaLocalizacao = caixaLocalizacao;
    }

    public Integer getCaixaCapacidadeMaxima() {
        return caixaCapacidadeMaxima;
    }

    public void setCaixaCapacidadeMaxima(Integer caixaCapacidadeMaxima) {
        this.caixaCapacidadeMaxima = caixaCapacidadeMaxima;
    }

    public Integer getCaixaOcupacaoAtual() {
        return caixaOcupacaoAtual;
    }

    public void setCaixaOcupacaoAtual(Integer caixaOcupacaoAtual) {
        this.caixaOcupacaoAtual = caixaOcupacaoAtual;
    }

    public Boolean getStatusFlagVerificado() {
        return statusFlagVerificado;
    }

    public void setStatusFlagVerificado(Boolean statusFlagVerificado) {
        this.statusFlagVerificado = statusFlagVerificado;
    }

    public String getStatusCondicao() {
        return statusCondicao;
    }

    public void setStatusCondicao(String statusCondicao) {
        this.statusCondicao = statusCondicao;
    }

    public String getStatusObservacao() {
        return statusObservacao;
    }

    public void setStatusObservacao(String statusObservacao) {
        this.statusObservacao = statusObservacao;
    }
}
