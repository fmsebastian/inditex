package com.paradigma.poc.inditex.productbyprice.infra.model;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class PricesIdsJpa implements Serializable {

    Long productId;

    Integer brandId;

    LocalDateTime startDate;

    LocalDateTime endDate;

}
