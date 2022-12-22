package com.paradigma.poc.inditex.productpricebydate.domain.model;

import org.springframework.stereotype.Component;

@Component
public class NewProductRequestMapper {

    public ProductPriceBetweenDates toProductBetweenDates(NewProductRequest productRequest) {

        return ProductPriceBetweenDates.builder()
                .productIds(productRequest.getProductIds())
                .startDate(productRequest.getStartDate())
                .endDate(productRequest.getEndDate())
                .priceList(productRequest.getPriceList())
                .priority(productRequest.getPriority())
                .price(productRequest.getPrice())
                .currency(productRequest.getCurrency())
                .originalStartDate(productRequest.getStartDate())
                .originalEndDate(productRequest.getEndDate())
                .build();
    }

}
