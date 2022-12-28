package com.paradigma.poc.inditex.productpricebydate.domain.write;

import com.paradigma.poc.inditex.productpricebydate.adapters.data.PricesJpaRepository;
import com.paradigma.poc.inditex.productpricebydate.adapters.data.entities.ProductPriceJpa;
import com.paradigma.poc.inditex.productpricebydate.domain.model.NewProductRequest;
import com.paradigma.poc.inditex.productpricebydate.domain.model.ProductIds;
import com.paradigma.poc.inditex.productpricebydate.domain.model.ProductPriceBetweenDates;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class WriteIntegrationTest {

    private static final ProductIds PRODUCT_IDS = ProductIds.builder().productId(1L).brandId(1).build();

    private static final int DAYS = 10;
    private static final Period PERIOD_PRODUCT_DATES = Period.ofDays(DAYS);
    private static final LocalDateTime NEW_START_DATE = LocalDateTime.parse("2022-10-10T00:00:00");
    private static final LocalDateTime NEW_END_DATE = NEW_START_DATE.plus(PERIOD_PRODUCT_DATES);
    private static final NewProductRequest NEW_PRODUCT_REQUEST = NewProductRequest.builder()
            .productIds(PRODUCT_IDS)
            .startDate(NEW_START_DATE)
            .endDate(NEW_END_DATE)
            .price(100.0)
            .priority(1)
            .build();
    @Autowired
    private PricesJpaRepository pricesJpaRepository;

    @Autowired
    private NewProductPriceWriter newProductPriceWriter;

    @Test
    @DisplayName(value = "when no previous products, new product should be the only result")
    public void returnOnlyOriginalInserted() {

        // When
        Set<ProductPriceBetweenDates> newProductSet =
                newProductPriceWriter.saveNewProductPrice(NEW_PRODUCT_REQUEST);

        ProductPriceBetweenDates expectedNewProduct = ProductPriceBetweenDates.builder()
                .productIds(PRODUCT_IDS)
                .startDate(NEW_START_DATE)
                .endDate(NEW_END_DATE)
                .originalStartDate(NEW_START_DATE)
                .originalEndDate(NEW_END_DATE)
                .price(100.0)
                .priority(1)
                .build();

        // Then
        assertExpectedProducts(Set.of(expectedNewProduct), newProductSet);
    }

    private void assertExpectedProducts(
            Set<ProductPriceBetweenDates> expectedProductPrices, Set<ProductPriceBetweenDates> actualProductPrices) {

        assertNotNull(actualProductPrices);
        assertEquals(expectedProductPrices.size(), actualProductPrices.size());
        expectedProductPrices.forEach(product -> assertTrue(actualProductPrices.contains(product),
                String.format("not found: %s", product.toString())));

        List<ProductPriceJpa> jpsProducts =
                pricesJpaRepository.findByProductIdAndBrandId(PRODUCT_IDS.getProductId(), PRODUCT_IDS.getBrandId());

        assertEquals(expectedProductPrices.size(), jpsProducts.size());

        expectedProductPrices.stream()
                .map(this::mapToProductPriceJpa)
                .forEach(
                        expectedJpaProduct -> assertTrue(jpsProducts.contains(expectedJpaProduct),
                                String.format("not found: %s", expectedJpaProduct.toString()))
                );
    }

    private ProductPriceJpa mapToProductPriceJpa(ProductPriceBetweenDates product) {

        return ProductPriceJpa.builder()
                .productId(product.getProductIds().getProductId())
                .brandId(product.getProductIds().getBrandId())
                .startDate(product.getStartDate())
                .endDate(product.getEndDate())
                .originalStartDate(product.getOriginalStartDate())
                .originalEndDate(product.getOriginalEndDate())
                .price(product.getPrice())
                .priceList(product.getPriceList())
                .currency(product.getCurrency())
                .priority(product.getPriority())
                .build();
    }
}
