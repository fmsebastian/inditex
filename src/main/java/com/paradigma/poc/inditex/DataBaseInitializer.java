package com.paradigma.poc.inditex;

import com.paradigma.poc.inditex.productpricebydate.domain.model.NewProductRequest;
import com.paradigma.poc.inditex.productpricebydate.domain.model.ProductIds;
import com.paradigma.poc.inditex.productpricebydate.domain.write.NewProductPriceWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
@Profile("!test")
public class DataBaseInitializer {

    private final long PRODUCT_ID = 35455;
    private final int BRAND_ID = 1;

    private final long OTHER_PRODUCT_ID = 7894;
    private final int OTHER_BRAND_ID = 55;
    private final NewProductPriceWriter newProductPriceWriter;

    /**
     * INSERT INTO prices (BRAND_ID, START_DATE, END_DATE, PRICE_LIST, PRODUCT_ID, PRIORITY, PRICE, CURR) VALUES (1, '2020-06-14 00.00.00', '2020-12-31 23.59.59', 1, 35455, 0, 35.50, 'EUR');
     * INSERT INTO prices (BRAND_ID, START_DATE, END_DATE, PRICE_LIST, PRODUCT_ID, PRIORITY, PRICE, CURR) VALUES (1, '2020-06-14 15.00.00', '2020-06-14 18.30.00', 2, 35455, 1, 25.45, 'EUR');
     * ...
     */
    @EventListener(ApplicationReadyEvent.class)
    public void initDatabaseWithTestProducts() {

        NewProductRequest PRODUCT_PRICE_1 = NewProductRequest.builder()
                .productIds(ProductIds.builder().productId(PRODUCT_ID)
                        .brandId(BRAND_ID)
                        .build())
                .startDate(LocalDateTime.parse("2020-06-14T00:00:00"))
                .endDate(LocalDateTime.parse("2020-12-31T23:59:59"))
                .price(35.50)
                .currency("EUR")
                .priority(0)
                .priceList(1)
                .build();

        NewProductRequest PRODUCT_PRICE_2 = NewProductRequest.builder()
                .productIds(ProductIds.builder().productId(PRODUCT_ID)
                        .brandId(BRAND_ID)
                        .build())
                .startDate(LocalDateTime.parse("2020-06-14T15:00:00"))
                .endDate(LocalDateTime.parse("2020-06-14T18:30:00"))
                .price(25.45)
                .currency("EUR")
                .priority(1)
                .priceList(2)
                .build();

        NewProductRequest PRODUCT_PRICE_3 = NewProductRequest.builder()
                .productIds(ProductIds.builder().productId(PRODUCT_ID)
                        .brandId(BRAND_ID)
                        .build())
                .startDate(LocalDateTime.parse("2020-06-15T00:00:00"))
                .endDate(LocalDateTime.parse("2020-06-15T11:00:00"))
                .price(30.50)
                .currency("EUR")
                .priority(1)
                .priceList(3)
                .build();

        NewProductRequest PRODUCT_PRICE_4 = NewProductRequest.builder()
                .productIds(ProductIds.builder().productId(PRODUCT_ID)
                        .brandId(BRAND_ID)
                        .build())
                .startDate(LocalDateTime.parse("2020-06-15T16:00:00"))
                .endDate(LocalDateTime.parse("2020-06-30T23:59:59"))
                .price(38.95)
                .currency("EUR")
                .priority(1)
                .priceList(4)
                .build();

        NewProductRequest PRODUCT_PRICE_5 = NewProductRequest.builder()
                .productIds(ProductIds.builder().productId(PRODUCT_ID)
                        .brandId(BRAND_ID)
                        .build())
                .startDate(LocalDateTime.parse("2020-06-15T13:00:00"))
                .endDate(LocalDateTime.parse("2020-06-15T19:59:59"))
                .price(15.00)
                .currency("EUR")
                .priority(2)
                .priceList(4)
                .build();

        NewProductRequest DIFFERENT_PRODUCT_1 = NewProductRequest.builder()
                .productIds(ProductIds.builder().productId(OTHER_PRODUCT_ID)
                        .brandId(BRAND_ID)
                        .build())
                .startDate(LocalDateTime.parse("2020-06-15T00:00:00"))
                .endDate(LocalDateTime.parse("2020-06-30T23:59:59"))
                .price(10.00)
                .currency("EUR")
                .priority(0)
                .priceList(1)
                .build();

        NewProductRequest DIFFERENT_PRODUCT_2 = NewProductRequest.builder()
                .productIds(ProductIds.builder().productId(OTHER_PRODUCT_ID)
                        .brandId(BRAND_ID)
                        .build())
                .startDate(LocalDateTime.parse("2020-06-15T16:00:00"))
                .endDate(LocalDateTime.parse("2020-06-15T18:59:59"))
                .price(20.00)
                .currency("EUR")
                .priority(1)
                .priceList(2)
                .build();

        NewProductRequest DIFFERENT_BRAND_1 = NewProductRequest.builder()
                .productIds(ProductIds.builder().productId(PRODUCT_ID)
                        .brandId(OTHER_BRAND_ID)
                        .build())
                .startDate(LocalDateTime.parse("2020-06-15T00:00:00"))
                .endDate(LocalDateTime.parse("2020-06-30T23:59:59"))
                .price(10.00)
                .currency("EUR")
                .priority(0)
                .priceList(1)
                .build();

        NewProductRequest DIFFERENT_BRAND_2 = NewProductRequest.builder()
                .productIds(ProductIds.builder().productId(PRODUCT_ID)
                        .brandId(OTHER_BRAND_ID)
                        .build())
                .startDate(LocalDateTime.parse("2020-06-15T16:00:00"))
                .endDate(LocalDateTime.parse("2020-06-15T18:59:59"))
                .price(20.00)
                .currency("EUR")
                .priority(1)
                .priceList(2)
                .build();

        log.info("initializing database");

        newProductPriceWriter.saveNewProductPrice(PRODUCT_PRICE_1);
        newProductPriceWriter.saveNewProductPrice(PRODUCT_PRICE_2);
        newProductPriceWriter.saveNewProductPrice(PRODUCT_PRICE_3);
        newProductPriceWriter.saveNewProductPrice(PRODUCT_PRICE_4);
        newProductPriceWriter.saveNewProductPrice(PRODUCT_PRICE_5);
        newProductPriceWriter.saveNewProductPrice(DIFFERENT_PRODUCT_1);
        newProductPriceWriter.saveNewProductPrice(DIFFERENT_PRODUCT_2);
        newProductPriceWriter.saveNewProductPrice(DIFFERENT_BRAND_1);
        newProductPriceWriter.saveNewProductPrice(DIFFERENT_BRAND_2);

        log.info("finishing initializing");
    }
}
