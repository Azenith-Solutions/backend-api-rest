    package com.azenithsolutions.backendapirest.v1.model;

    import com.azenithsolutions.backendapirest.v1.model.enums.StatusPedido;
    import com.fasterxml.jackson.annotation.JsonBackReference;
    import jakarta.persistence.*;
    import lombok.AllArgsConstructor;
    import lombok.Data;
    import lombok.NoArgsConstructor;
    import java.time.LocalDateTime;
    import java.util.List;

    @Entity
    @Table(name = "pedido")
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class Order {
        @Column(name = "id_pedido")
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long idPedido;

        @Column(name = "codigo", nullable = false)
        private String codigo;

        @OneToMany(mappedBy = "fkPedido")
        @JsonBackReference
        private List<Item> fkItens;

        @Column(name = "nome_comprador", length = 50)
        private String nomeComprador;

        @Column(name = "email_comprador", length = 50)
        private String emailComprador;

        @Column(name = "CNPJ", columnDefinition = "CHAR(14)")
        private String CNPJ;

        @Column(name = "valor")
        private String valor;

        // Enum que limita os status possíveis
        @Column(name = "status")
        @Enumerated(EnumType.STRING)
        private StatusPedido status;

        @Column(name = "ddd", length = 3)
        private int DDD;

        @Column(name = "tel_celular", columnDefinition = "CHAR(9)")
        private String telCelular;

        @Column(name = "created_at")
        private LocalDateTime createdAt;

        @Column(name = "updated_at")
        private LocalDateTime updatedAt;
    }
