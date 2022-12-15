package com.paradigma.poc.inditex.productpricebydate.adapters.web.resources;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class ProductPriceForDateResponse {

    Long productId;

    Integer brandId;

    Integer priceList;

    LocalDateTime startDate;

    LocalDateTime endDate;

    Double price;

    String currency;

}
