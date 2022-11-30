package com.paradigma.poc.inditex.productbyprice.domain;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
public class PriceByDateRequest {

    private LocalDateTime date;

    private Integer brandId;

    private Integer productId;

}
