package com.paradigma.poc.inditex.productpricebydate.adapters.web.resources;

import com.paradigma.poc.inditex.productpricebydate.domain.model.ProductPriceBetweenDates;
import org.springframework.stereotype.Component;

@Component
public class ProductPriceForDateResponseMapper {

    public ProductPriceForDateResponse fromProductBetweenDates(ProductPriceBetweenDates productPriceBetweenDates) {

        return ProductPriceForDateResponse.builder()
                .productId(productPriceBetweenDates.getProductIds().getProductId())
                .brandId(productPriceBetweenDates.getProductIds().getBrandId())
                .startDate(productPriceBetweenDates.getOriginalStartDate())
                .endDate(productPriceBetweenDates.getOriginalEndDate())
                .priceList(productPriceBetweenDates.getPriceList())
                .price(productPriceBetweenDates.getPrice())
                .currency(productPriceBetweenDates.getCurrency())
                .build();
    }
}
