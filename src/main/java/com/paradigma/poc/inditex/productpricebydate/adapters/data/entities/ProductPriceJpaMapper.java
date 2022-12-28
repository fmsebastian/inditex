package com.paradigma.poc.inditex.productpricebydate.adapters.data.entities;

import com.paradigma.poc.inditex.productpricebydate.domain.model.ProductIds;
import com.paradigma.poc.inditex.productpricebydate.domain.model.ProductPriceBetweenDates;
import org.springframework.stereotype.Component;

import java.util.Objects;

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
                .originalStartDate(Objects.nonNull(productPriceJpa.getOriginalStartDate()) ?
                        productPriceJpa.getOriginalStartDate() : productPriceJpa.getStartDate())
                .originalEndDate(Objects.nonNull(productPriceJpa.getOriginalEndDate()) ?
                        productPriceJpa.getOriginalEndDate() : productPriceJpa.getEndDate())
                .priceList(productPriceJpa.getPriceList())
                .price(productPriceJpa.getPrice())
                .currency(productPriceJpa.getCurrency())
                .priority(productPriceJpa.getPriority())
                .build();
    }

    public ProductPriceJpa fromProductBetweenDates(ProductPriceBetweenDates productPriceBetweenDates) {

        return ProductPriceJpa.builder()
                .productId(productPriceBetweenDates.getProductIds().getProductId())
                .brandId(productPriceBetweenDates.getProductIds().getBrandId())
                .startDate(productPriceBetweenDates.getStartDate())
                .endDate(productPriceBetweenDates.getEndDate())
                .originalStartDate(Objects.nonNull(productPriceBetweenDates.getOriginalStartDate()) ?
                        productPriceBetweenDates.getOriginalStartDate() : productPriceBetweenDates.getStartDate())
                .originalEndDate(Objects.nonNull(productPriceBetweenDates.getOriginalEndDate()) ?
                        productPriceBetweenDates.getOriginalEndDate() : productPriceBetweenDates.getEndDate())
                .priceList(productPriceBetweenDates.getPriceList())
                .price(productPriceBetweenDates.getPrice())
                .currency(productPriceBetweenDates.getCurrency())
                .priority(productPriceBetweenDates.getPriority())
                .build();
    }
}
