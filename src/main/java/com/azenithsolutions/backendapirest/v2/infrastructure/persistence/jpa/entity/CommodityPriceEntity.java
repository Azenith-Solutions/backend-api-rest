package com.azenithsolutions.backendapirest.v2.infrastructure.persistence.jpa.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "commodities_prices")
public class CommodityPriceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "price_BRL", nullable = false, precision = 10, scale = 2)
    private BigDecimal priceBrl;

    @Column(name = "metals", nullable = false, length = 50)
    private String metals;

    @Column(name = "country", nullable = false, length = 50)
    private String country;

    @Column(name = "unit", nullable = false, length = 20)
    private String unit;
}
