package com.paradigma.poc.inditex.productbyprice.web.resources;

import com.paradigma.poc.inditex.productbyprice.domain.ProductPriceBetweenDates;
import org.springframework.stereotype.Component;

//@Mapper(componentModel = "spring")
@Component
public class ProductPriceForDateResponseMapper {

    /*@Mapping(target="productId", source="productPriceBetweenDates.productIds.productId")
    @Mapping(target="brandId", source="productPriceBetweenDates.productIds.brandId")*/
    public ProductPriceForDateResponse fromProductBetweenDates(ProductPriceBetweenDates productPriceBetweenDates) {

        return ProductPriceForDateResponse.builder()
                .productId(productPriceBetweenDates.getProductIds().getProductId())
                .brandId(productPriceBetweenDates.getProductIds().getBrandId())
                .startDate(productPriceBetweenDates.getStartDate())
                .endDate(productPriceBetweenDates.getEndDate())
                .priceList(productPriceBetweenDates.getPriceList())
                .price(productPriceBetweenDates.getPrice())
                .currency(productPriceBetweenDates.getCurrency())
                .build();
    }
    /*{

        return ProductPriceForDateResponse.builder()
                .productId(productPriceBetweenDates.getProductIds().getProductId())
                .productId(productPriceBetweenDates.getProductIds().getBrandId())
    }*/
}
