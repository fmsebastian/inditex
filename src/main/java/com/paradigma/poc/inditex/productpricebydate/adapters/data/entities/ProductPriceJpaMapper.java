package com.paradigma.poc.inditex.productpricebydate.adapters.data.entities;

import com.paradigma.poc.inditex.productpricebydate.domain.model.ProductIds;
import com.paradigma.poc.inditex.productpricebydate.domain.model.ProductPriceBetweenDates;
import org.springframework.stereotype.Component;

@Component
public class ProductPriceJpaMapper {

    public ProductPriceBetweenDates toProductBetweenDates(ProductPriceJpa productPriceJpa) {

        return ProductPriceBetweenDates.builder()
                .productIds(ProductIds.builder()
                        .productId(productPriceJpa.getProductId())
                        .brandId(productPriceJpa.getBrandId())
                        .build())
                .startDate(productPriceJpa.getStartDate())
                .endDate(productPriceJpa.getEndDate())
                .priceList(productPriceJpa.getPriceList())
                .price(productPriceJpa.getPrice())
                .currency(productPriceJpa.getCurrency())
                .priority(productPriceJpa.getPriority())
                .build();
    }
}
