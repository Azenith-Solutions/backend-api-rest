package com.azenithsolutions.backendapirest.v2.core.domain.model.component;

import com.azenithsolutions.backendapirest.v2.core.domain.model.box.Box;
import com.azenithsolutions.backendapirest.v2.core.domain.model.component.valueobjects.Category;
import com.azenithsolutions.backendapirest.v2.core.domain.model.component.valueobjects.Status;

import java.util.Date;
import java.util.UUID;

public class EletronicComponent {
    private Long id;
    private String idHardwaretech;
    private String nome;
    private Box caixa;
    private Category categoria;
    private String partNumber;
    private int quantidade;
    private Boolean anunciado;
    private String codigoMercadoLivre;
    private Status status;
    private String s3ImagePath;
    private Date dataUltimaVenda;
    private Date dataCriacao;
    private Date dataUltimaAtualizacao;

    private EletronicComponent(Long id, String idHardwaretech, String nome, Box caixa,
                                 Category categoria, String partNumber, int quantidade,
                                 Boolean anunciado, String codigoMercadoLivre, Status status,
                                 String s3ImagePath, Date dataUltimaVenda,
                                 Date dataCriacao, Date dataUltimaAtualizacao) {
        this.id = id;
        this.idHardwaretech = idHardwaretech;
        this.nome = nome;
        this.caixa = caixa;
        this.categoria = categoria;
        this.partNumber = partNumber;
        this.quantidade = quantidade;
        this.anunciado = anunciado;
        this.codigoMercadoLivre = codigoMercadoLivre;
        this.status = status;
        this.s3ImagePath = s3ImagePath;
        this.dataUltimaVenda = dataUltimaVenda;
        this.dataCriacao = dataCriacao;
        this.dataUltimaAtualizacao = dataUltimaAtualizacao;
    }


    public static EletronicComponent criarNovo(String nome, Category categoria, String partNumber, int quantidade) {
        validarCamposObrigatorios(nome, categoria, quantidade);

        return new EletronicComponent(
                null,
                gerarIdHardwaretech(),
                nome,
                null,
                categoria,
                partNumber,
                quantidade,
                false,
                null,
                Status.naoVerificado("Novo componente"),
                null,
                null,
                new Date(),
                new Date()
        );
    }

    public static EletronicComponent recriar(Long id, String idHardwaretech, String nome, Box caixa,
                                               Category categoria, String partNumber, int quantidade,
                                               Boolean anunciado, String codigoMercadoLivre, Status status,
                                               String s3ImagePath, Date dataUltimaVenda,
                                               Date dataCriacao, Date dataUltimaAtualizacao) {
        return new EletronicComponent(
                id, idHardwaretech, nome, caixa, categoria, partNumber, quantidade,
                anunciado, codigoMercadoLivre, status, s3ImagePath,
                dataUltimaVenda, dataCriacao, dataUltimaAtualizacao
        );
    }

    private static void validarCamposObrigatorios(String nome, Category categoria, int quantidade) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do componente não pode ser vazio");
        }

        if (categoria == null) {
            throw new IllegalArgumentException("Categoria do componente não pode ser nula");
        }

        if (quantidade < 0) {
            throw new IllegalArgumentException("Quantidade não pode ser negativa");
        }
    }

    private static String gerarIdHardwaretech() {
        return "HT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    public Long getId() {
        return id;
    }

    public String getIdHardwaretech() {
        return idHardwaretech;
    }

    public String getNome() {
        return nome;
    }

    public Box getCaixa() {
        return caixa;
    }

    public Category getCategoria() {
        return categoria;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public Boolean getAnunciado() {
        return anunciado;
    }

    public String getCodigoMercadoLivre() {
        return codigoMercadoLivre;
    }

    public Status getStatus() {
        return status;
    }

    public String getS3ImagePath() {
        return s3ImagePath;
    }

    public Date getDataUltimaVenda() {
        return dataUltimaVenda;
    }

    public Date getDataCriacao() {
        return dataCriacao;
    }

    public Date getDataUltimaAtualizacao() {
        return dataUltimaAtualizacao;
    }

    public EletronicComponent update(String nome, Category categoria, String partNumber, int quantidade) {
        validarCamposObrigatorios(nome, categoria, quantidade);
        
        this.nome = nome;
        this.categoria = categoria;
        this.partNumber = partNumber;
        this.quantidade = quantidade;
        this.dataUltimaAtualizacao = new Date();
        
        return this;
    }
}
