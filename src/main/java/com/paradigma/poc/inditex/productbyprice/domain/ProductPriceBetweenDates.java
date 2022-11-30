package com.paradigma.poc.inditex.productbyprice.domain;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class ProductPriceBetweenDates {

    ProductIds productIds;

    LocalDateTime startDate;

    LocalDateTime endDate;

    Integer priceList;

    Integer priority;

    Double price;

    String currency;

}
