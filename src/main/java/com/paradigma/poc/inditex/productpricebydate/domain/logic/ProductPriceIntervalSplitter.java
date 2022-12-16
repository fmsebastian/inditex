package com.paradigma.poc.inditex.productpricebydate.domain.logic;

import com.paradigma.poc.inditex.productpricebydate.domain.model.DateInterval;
import com.paradigma.poc.inditex.productpricebydate.domain.model.ProductPriceBetweenDates;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductPriceIntervalSplitter {

    private final DateIntervalSplitter dateIntervalSplitter;

    public Set<ProductPriceBetweenDates> splitByDate(
            ProductPriceBetweenDates originalProductPrice, DateInterval dateInterval) {

        DateInterval productDateInterval = DateInterval.builder()
                .startDate(originalProductPrice.getStartDate())
                .endDate(originalProductPrice.getEndDate())
                .build();

        return dateIntervalSplitter.removeInterval(productDateInterval, dateInterval)
                .stream().map(newDateInterval ->
                        originalProductPrice.toBuilder()
                                .startDate(newDateInterval.getStartDate())
                                .endDate(newDateInterval.getEndDate())
                                .build()).collect(Collectors.toSet());
    }
}
