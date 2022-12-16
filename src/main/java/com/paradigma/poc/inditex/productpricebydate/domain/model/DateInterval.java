package com.paradigma.poc.inditex.productpricebydate.domain.model;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Builder
@Value
public class DateInterval {

    LocalDateTime startDate;

    LocalDateTime endDate;

}
