package com.paradigma.poc.inditex.productpricebydate.domain.model;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class PriceByDateRequest {

    private LocalDateTime date;

    private Integer brandId;

    private Long productId;

}
