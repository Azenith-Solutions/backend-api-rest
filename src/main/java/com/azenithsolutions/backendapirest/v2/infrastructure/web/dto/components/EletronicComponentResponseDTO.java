package com.azenithsolutions.backendapirest.v2.infrastructure.web.dto.components;

import java.util.Date;

public class EletronicComponentResponseDTO {
    private Long id;
    private String idHardwaretech;
    private String nome;
    private Long idCaixa;
    private String categoria;
    private String partNumber;
    private int quantidade;
    private Boolean anunciado;
    private String codigoMercadoLivre;
    private Boolean verificado;
    private String condicao;
    private String observacao;
    private String status;
    private String s3ImagePath;
    private Date dataUltimaVenda;
    private Date dataCriacao;
    private Date dataUltimaAtualizacao;


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

    public Long getCaixaID() {
        return idCaixa;
    }

    public void setCaixaID(Long caixaNome) {
        this.idCaixa = caixaNome;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
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

    public Boolean getVerificado() {
        return verificado;
    }

    public void setVerificado(Boolean verificado) {
        this.verificado = verificado;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
}
