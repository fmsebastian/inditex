package com.paradigma.poc.inditex.productpricebydate.adapters.data.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "prices")
@Entity
@IdClass(PricesIdsJpa.class)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
    @Column(name = "ORIGINAL_START_DATE")
    private LocalDateTime originalStartDate;
    @Column(name = "ORIGINAL_END_DATE")
    private LocalDateTime originalEndDate;
    @Column(name = "PRICE_LIST")
    private Integer priceList;
    @Column(name = "PRIORITY")
    private Integer priority;
    @Column(name = "PRICE")
    private Double price;
    @Column(name = "CURR")
    private String currency;
}
