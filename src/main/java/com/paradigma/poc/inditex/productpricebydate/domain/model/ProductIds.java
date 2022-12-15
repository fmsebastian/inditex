package com.paradigma.poc.inditex.productpricebydate.domain.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ProductIds {

    private Integer brandId;

    private Long productId;

}
