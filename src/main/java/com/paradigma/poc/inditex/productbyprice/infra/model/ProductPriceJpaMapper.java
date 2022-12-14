package com.paradigma.poc.inditex.productbyprice.infra.model;

import com.paradigma.poc.inditex.productbyprice.domain.ProductIds;
import com.paradigma.poc.inditex.productbyprice.domain.ProductPriceBetweenDates;
import org.springframework.stereotype.Component;

@Component
public class ProductPriceJpaMapper {

    public ProductPriceBetweenDates toProductBetweenDates(ProductPriceJpa productPriceJpa) {

        return ProductPriceBetweenDates.builder()
                .productIds(ProductIds.builder()
                        .productId(Integer.valueOf(productPriceJpa.getProductId().intValue()))
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
