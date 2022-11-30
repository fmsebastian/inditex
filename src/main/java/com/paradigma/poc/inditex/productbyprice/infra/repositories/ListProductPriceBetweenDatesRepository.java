package com.paradigma.poc.inditex.productbyprice.infra.repositories;

import com.paradigma.poc.inditex.productbyprice.application.repositories.ProductPriceBetweenDatesRepository;
import com.paradigma.poc.inditex.productbyprice.domain.ProductIds;
import com.paradigma.poc.inditex.productbyprice.domain.ProductPriceBetweenDates;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

// TODO: delete this class (its only for testing purposes)
@Repository
public class ListProductPriceBetweenDatesRepository implements ProductPriceBetweenDatesRepository {

    public static final int PRODUCT_ID = 35455;
    public static final int BRAND_ID = 1;

    /**
     * 1         2020-06-14-00.00.00                        2020-12-31-23.59.59                        1                        35455                0                        35.50            EUR
     * 1         2020-06-14-15.00.00                        2020-06-14-18.30.00                        2                        35455                1                        25.45            EUR
     * 1         2020-06-15-00.00.00                        2020-06-15-11.00.00                        3                        35455                1                        30.50            EUR
     * 1         2020-06-15-16.00.00                        2020-06-30-23.59.59                        4                        35455                1                        38.95            EUR
     */

    private static ProductIds productIds = ProductIds.builder().productId(PRODUCT_ID).brandId(BRAND_ID).build();
    private static ProductPriceBetweenDates product1 = ProductPriceBetweenDates.builder()
            .productIds(productIds)
            .startDate(LocalDateTime.parse("2020-06-14T00:00:00"))
            .endDate(LocalDateTime.parse("2020-12-31T23:59:59"))
            .price(35.50)
            .currency("EUR")
            .priority(0)
            .priceList(1)
            .build();
    private static ProductPriceBetweenDates product2 = ProductPriceBetweenDates.builder()
            .productIds(productIds)
            .startDate(LocalDateTime.parse("2020-06-14T15:00:00"))
            .endDate(LocalDateTime.parse("2020-06-14T18:30:00"))
            .price(25.45)
            .currency("EUR")
            .priority(1)
            .priceList(2)
            .build();
    private static ProductPriceBetweenDates product3 = ProductPriceBetweenDates.builder()
            .productIds(productIds)
            .startDate(LocalDateTime.parse("2020-06-15T00:00:00"))
            .endDate(LocalDateTime.parse("2020-06-15T11:00:00"))
            .price(30.50)
            .currency("EUR")
            .priority(1)
            .priceList(3)
            .build();
    private static ProductPriceBetweenDates product4 = ProductPriceBetweenDates.builder()
            .productIds(productIds)
            .startDate(LocalDateTime.parse("2020-06-15T16:00:00"))
            .endDate(LocalDateTime.parse("2020-06-30T23:59:59"))
            .price(38.95)
            .currency("EUR")
            .priority(1)
            .priceList(4)
            .build();

    @Override
    public List<ProductPriceBetweenDates> findByProductIds(ProductIds productIds) {

        return List.of(product1, product2, product3, product4);
    }
}
