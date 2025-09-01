package com.azenithsolutions.backendapirest.v2.core.domain.model.order;

import com.azenithsolutions.backendapirest.v2.core.domain.model.enums.OrderStatus;

import java.time.LocalDateTime;

public class Order {
    private Long id;
    private String codigo;
    private String nomeComprador;
    private String emailComprador;
    private String cnpj;
    private String valor;
    private OrderStatus status;
    private String telCelular;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Order() {
    }

    public Order(Long id, String codigo, String nomeComprador, String emailComprador, String cnpj, String valor, OrderStatus status, String telCelular, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.codigo = codigo;
        this.nomeComprador = nomeComprador;
        this.emailComprador = emailComprador;
        this.cnpj = cnpj;
        this.valor = valor;
        this.status = status;
        this.telCelular = telCelular;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNomeComprador() {
        return nomeComprador;
    }

    public String getEmailComprador() {
        return emailComprador;
    }

    public String getCnpj() {
        return cnpj;
    }

    public String getValor() {
        return valor;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public String getTelCelular() {
        return telCelular;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public void setNomeComprador(String nomeComprador) {
        this.nomeComprador = nomeComprador;
    }

    public void setEmailComprador(String emailComprador) {
        this.emailComprador = emailComprador;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public void setTelCelular(String telCelular) {
        this.telCelular = telCelular;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
