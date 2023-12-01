package com.paradigma.poc.inditex.productpricebydate.adapters.data.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PricesIdsJpa implements Serializable {

    Long productId;

    Integer brandId;

    LocalDateTime startDate;

    LocalDateTime endDate;

}
