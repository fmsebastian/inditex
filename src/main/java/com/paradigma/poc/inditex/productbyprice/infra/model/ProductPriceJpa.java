package com.paradigma.poc.inditex.productbyprice.infra.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "prices")
@Entity
@IdClass(PricesIdsJpa.class)
@Data
public class ProductPriceJpa {

    @Id
    @Column(name = "PRODUCT_ID")
    private Long productId;
    @Id
    @Column(name = "BRAND_ID")
    private Integer brandId;
    @Id
    @Column(name = "START_DATE")
    private LocalDateTime startDate;
    @Id
    @Column(name = "END_DATE")
    private LocalDateTime endDate;
    @Column(name = "PRICE_LIST")
    private Integer priceList;
    @Column(name = "PRIORITY")
    private Integer priority;
    @Column(name = "PRICE")
    private Double price;
    @Column(name = "CURR")
    private String currency;

}
