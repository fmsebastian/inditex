package com.paradigma.poc.inditex.productpricebydate.domain.model;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class NewProductRequest {

    ProductIds productIds;

    LocalDateTime startDate;

    LocalDateTime endDate;

    Integer priceList;

    Integer priority;

    Double price;

    String currency;

}
