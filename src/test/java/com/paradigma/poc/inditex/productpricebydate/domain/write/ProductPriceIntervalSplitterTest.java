package com.paradigma.poc.inditex.productpricebydate.domain.write;

import com.paradigma.poc.inditex.productpricebydate.domain.model.DateInterval;
import com.paradigma.poc.inditex.productpricebydate.domain.model.ProductIds;
import com.paradigma.poc.inditex.productpricebydate.domain.model.ProductPriceBetweenDates;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.time.Period;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@Import(value = {DateIntervalSplitter.class, ProductPriceIntervalSplitter.class})
public class ProductPriceIntervalSplitterTest {

    public static final ProductIds PRODUCT_IDS = ProductIds.builder().productId(1L).brandId(1).build();

    public static final int DAYS = 10;
    public static final Period PERIOD_PRODUCT_DATES = Period.ofDays(DAYS);
    public static final LocalDateTime ORIGINAL_START_DATE = LocalDateTime.parse("2022-10-10T00:00:00");
    public static final LocalDateTime ORIGINAL_END_DATE = ORIGINAL_START_DATE.plus(PERIOD_PRODUCT_DATES);

    public static final ProductPriceBetweenDates ORIGINAL_PRODUCT_PRICE = ProductPriceBetweenDates.builder()
            .productIds(PRODUCT_IDS)
            .startDate(ORIGINAL_START_DATE)
            .endDate(ORIGINAL_END_DATE)
            .price(100.0)
            .priority(0)
            .build();

    @Autowired
    private ProductPriceIntervalSplitter productPriceIntervalSplitter;

    private static Stream<Arguments> datesWithNoOverlap() {

        DateInterval intervalDateBeforeProductDates = DateInterval.builder()
                .startDate(ORIGINAL_START_DATE.minus(PERIOD_PRODUCT_DATES.multipliedBy(2)))
                .endDate(ORIGINAL_END_DATE.minus(PERIOD_PRODUCT_DATES.multipliedBy(2)))
                .build();

        DateInterval intervalDateAfterProductDates = DateInterval.builder()
                .startDate(ORIGINAL_START_DATE.plus(PERIOD_PRODUCT_DATES.multipliedBy(2)))
                .endDate(ORIGINAL_END_DATE.plus(PERIOD_PRODUCT_DATES.multipliedBy(2)))
                .build();

        return Stream.of(
                Arguments.of("product dates are lower range than split date interval", intervalDateBeforeProductDates),
                Arguments.of("product date is bigger range than split date interval", intervalDateAfterProductDates)
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource(value = "datesWithNoOverlap")
    public void shouldReturnSameIntervalWhenNoOverlap(String testName, DateInterval dateInterval) {


        // When
        Set<ProductPriceBetweenDates> productPriceSplitSet =
                productPriceIntervalSplitter.splitByDate(ORIGINAL_PRODUCT_PRICE, dateInterval);

        // Then
        assertExpectedProducts(Set.of(ORIGINAL_PRODUCT_PRICE), productPriceSplitSet);
    }

    @Test
    @DisplayName(value = "when interval date overlaps in the higher segment should return product with inferior interval")
    public void shouldReturnInferiorIntervalWhenOverlapsInHigher() {

        // Given
        LocalDateTime splitStartDate = ORIGINAL_START_DATE.minus(Period.ofDays(DAYS / 2));
        LocalDateTime splitEndDate = ORIGINAL_END_DATE.minus(Period.ofDays(DAYS / 2));

        DateInterval intervalDateShiftedToFuture = DateInterval.builder()
                .startDate(splitStartDate)
                .endDate(splitEndDate)
                .build();

        // When
        Set<ProductPriceBetweenDates> productPriceSplitSet =
                productPriceIntervalSplitter.splitByDate(ORIGINAL_PRODUCT_PRICE, intervalDateShiftedToFuture);

        // Then
        ProductPriceBetweenDates expectedProductPrice = ORIGINAL_PRODUCT_PRICE.toBuilder()
                .startDate(splitEndDate)
                .endDate(ORIGINAL_END_DATE)
                .build();
        assertExpectedProducts(Set.of(expectedProductPrice), productPriceSplitSet);
    }

    @Test
    @DisplayName(value = "when interval date overlaps in the lower segment should return product with superior interval")
    public void shouldReturnSuperiorIntervalWhenOverlapsInLower() {
        // Given
        LocalDateTime splitStartDate = ORIGINAL_START_DATE.plus(Period.ofDays(DAYS / 2));
        LocalDateTime splitEndDate = ORIGINAL_END_DATE.plus(Period.ofDays(DAYS / 2));

        DateInterval intervalDateShiftedToFuture = DateInterval.builder()
                .startDate(splitStartDate)
                .endDate(splitEndDate)
                .build();

        // When
        Set<ProductPriceBetweenDates> productPriceSplitSet =
                productPriceIntervalSplitter.splitByDate(ORIGINAL_PRODUCT_PRICE, intervalDateShiftedToFuture);

        // Then
        ProductPriceBetweenDates expectedProductPrice = ORIGINAL_PRODUCT_PRICE.toBuilder()
                .startDate(ORIGINAL_START_DATE)
                .endDate(splitStartDate)
                .build();
        assertExpectedProducts(Set.of(expectedProductPrice), productPriceSplitSet);
    }

    @Test
    @DisplayName(value = "when interval date overlaps in the middle segment should return two products")
    public void shouldReturnTwoProductsWhenOverlapInMiddle() {

        // Given
        LocalDateTime splitStartDate = ORIGINAL_START_DATE.plus(Period.ofDays(DAYS / 3));
        LocalDateTime splitEndDate = ORIGINAL_END_DATE.minus(Period.ofDays(DAYS / 3));

        DateInterval intervalDateShiftedToFuture = DateInterval.builder()
                .startDate(splitStartDate)
                .endDate(splitEndDate)
                .build();

        // When
        Set<ProductPriceBetweenDates> productPriceSplitSet =
                productPriceIntervalSplitter.splitByDate(ORIGINAL_PRODUCT_PRICE, intervalDateShiftedToFuture);

        // Then
        ProductPriceBetweenDates expectedInferiorProductPrice = ORIGINAL_PRODUCT_PRICE.toBuilder()
                .startDate(ORIGINAL_START_DATE)
                .endDate(splitStartDate)
                .build();
        ProductPriceBetweenDates expectedSuperiorProductPrice = ORIGINAL_PRODUCT_PRICE.toBuilder()
                .startDate(splitEndDate)
                .endDate(ORIGINAL_END_DATE)
                .build();
        assertExpectedProducts(Set.of(expectedInferiorProductPrice, expectedSuperiorProductPrice), productPriceSplitSet);
    }

    private void assertExpectedProducts(
            Set<ProductPriceBetweenDates> expectedProductPrices, Set<ProductPriceBetweenDates> actualProductPrices) {

        assertNotNull(actualProductPrices);
        assertEquals(expectedProductPrices.size(), actualProductPrices.size());
        expectedProductPrices.forEach(o -> assertTrue(actualProductPrices.contains(o)));
    }

}