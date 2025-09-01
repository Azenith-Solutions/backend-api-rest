package com.azenithsolutions.backendapirest.v2.core.domain.model.box;

public class Box {
    private final Long id;
    private String codigo;
    private String localizacao;
    private int capacidadeMaxima;
    private int ocupacaoAtual;

    // Construtor privado
    private Box(Long id, String codigo, String localizacao, int capacidadeMaxima, int ocupacaoAtual) {
        this.id = id;
        this.codigo = codigo;
        this.localizacao = localizacao;
        this.capacidadeMaxima = capacidadeMaxima;
        this.ocupacaoAtual = ocupacaoAtual;
    }

    // Factory Method para criar uma nova caixa
    public static Box criar(String codigo, String localizacao, int capacidadeMaxima) {
        validarCapacidade(capacidadeMaxima);
        validarCodigo(codigo);

        return new Box(null, codigo, localizacao, capacidadeMaxima, 0);
    }

    // Factory Method para recriar uma caixa existente
    public static Box recriar(Long id, String codigo, String localizacao, int capacidadeMaxima, int ocupacaoAtual) {
        return new Box(id, codigo, localizacao, capacidadeMaxima, ocupacaoAtual);
    }

    private static void validarCapacidade(int capacidadeMaxima) {
        if (capacidadeMaxima <= 0) {
            throw new IllegalArgumentException("Capacidade máxima deve ser maior que zero");
        }
    }

    private static void validarCodigo(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("Código da caixa não pode ser vazio");
        }
    }

    // Métodos de negócio
    public boolean podeAdicionarComponentes(int quantidade) {
        return (ocupacaoAtual + quantidade) <= capacidadeMaxima;
    }

    public void adicionarComponentes(int quantidade) {
        if (!podeAdicionarComponentes(quantidade)) {
            throw new IllegalStateException("Capacidade da caixa excedida");
        }
        ocupacaoAtual += quantidade;
    }

    public void removerComponentes(int quantidade) {
        if (quantidade > ocupacaoAtual) {
            throw new IllegalArgumentException("Quantidade a remover excede ocupação atual");
        }
        ocupacaoAtual -= quantidade;
    }

    public int espacoDisponivel() {
        return capacidadeMaxima - ocupacaoAtual;
    }

    // Getters e setters limitados
    public Long getId() {
        return id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        validarCodigo(codigo);
        this.codigo = codigo;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public int getCapacidadeMaxima() {
        return capacidadeMaxima;
    }

    public void setCapacidadeMaxima(int capacidadeMaxima) {
        validarCapacidade(capacidadeMaxima);

        if (capacidadeMaxima < ocupacaoAtual) {
            throw new IllegalArgumentException("Nova capacidade não pode ser menor que ocupação atual");
        }
        this.capacidadeMaxima = capacidadeMaxima;
    }

    public int getOcupacaoAtual() {
        return ocupacaoAtual;
    }
}
