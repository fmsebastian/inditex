package com.paradigma.poc.inditex.productbyprice.domain;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ProductIds {

    private Integer brandId;

    private Integer productId;

}
