package com.paradigma.poc.inditex.productbyprice.web.resources;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class ProductPriceForDateResponse {

    Integer productId;

    Integer brandId;

    Integer priceList;

    LocalDateTime startDate;

    LocalDateTime endDate;

    Double price;

    String currency;

}
