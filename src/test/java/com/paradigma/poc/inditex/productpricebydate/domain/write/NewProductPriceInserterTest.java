package com.paradigma.poc.inditex.productpricebydate.domain.write;

import com.paradigma.poc.inditex.productpricebydate.domain.exceptions.SamePriorityOverlapException;
import com.paradigma.poc.inditex.productpricebydate.domain.model.ProductIds;
import com.paradigma.poc.inditex.productpricebydate.domain.model.ProductPriceBetweenDates;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.Period;
import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class NewProductPriceInserterTest {

    private static final ProductIds PRODUCT_IDS = ProductIds.builder().productId(1L).brandId(1).build();

    private static final int DAYS = 10;
    private static final Period PERIOD_PRODUCT_DATES = Period.ofDays(DAYS);
    private static final LocalDateTime NEW_START_DATE = LocalDateTime.parse("2022-10-10T00:00:00");
    private static final LocalDateTime NEW_END_DATE = NEW_START_DATE.plus(PERIOD_PRODUCT_DATES);
    private static final ProductPriceBetweenDates NEW_PRODUCT_PRICE = ProductPriceBetweenDates.builder()
            .productIds(PRODUCT_IDS)
            .startDate(NEW_START_DATE)
            .endDate(NEW_END_DATE)
            .price(100.0)
            .priority(1)
            .build();

    @Autowired
    private NewProductPriceInserter newProductPriceInserter;

    @Test
    @DisplayName(value = "when no previous products, new product should be the only result")
    public void returnOnlyOriginalInserted() {

        // When
        Set<ProductPriceBetweenDates> newProductSet =
                newProductPriceInserter.insertNewProduct(NEW_PRODUCT_PRICE, Collections.emptySet());

        // Then
        assertExpectedProducts(Set.of(NEW_PRODUCT_PRICE), newProductSet);
    }

    @Test
    @DisplayName(value = "when no overlap with previous, new product should be added as it is")
    public void shouldAddNewToExistent() {
        ProductPriceBetweenDates productPriceBeforeNewDates = NEW_PRODUCT_PRICE.toBuilder()
                .startDate(NEW_START_DATE.minus(PERIOD_PRODUCT_DATES.multipliedBy(2)))
                .endDate(NEW_END_DATE.minus(PERIOD_PRODUCT_DATES.multipliedBy(2)))
                .build();

        ProductPriceBetweenDates productPriceAfterNewDates = NEW_PRODUCT_PRICE.toBuilder()
                .startDate(NEW_START_DATE.plus(PERIOD_PRODUCT_DATES.multipliedBy(2)))
                .endDate(NEW_END_DATE.plus(PERIOD_PRODUCT_DATES.multipliedBy(2)))
                .build();

        // When
        Set<ProductPriceBetweenDates> newProductSet =
                newProductPriceInserter.insertNewProduct(
                        NEW_PRODUCT_PRICE, Set.of(productPriceBeforeNewDates, productPriceAfterNewDates));

        // Then
        assertExpectedProducts(
                Set.of(NEW_PRODUCT_PRICE, productPriceAfterNewDates, productPriceBeforeNewDates), newProductSet);
    }

    @Test
    @DisplayName(value = "when overlap with inferior range and lower priority, existent must be split")
    public void shouldSplitExistent() {

        ProductPriceBetweenDates productPriceBeforeNewDates = NEW_PRODUCT_PRICE.toBuilder()
                .startDate(NEW_START_DATE.minus(PERIOD_PRODUCT_DATES.multipliedBy(2)))
                .endDate(NEW_END_DATE.minus(PERIOD_PRODUCT_DATES.multipliedBy(2)))
                .build();

        ProductPriceBetweenDates productPriceAfterNewDates = NEW_PRODUCT_PRICE.toBuilder()
                .startDate(NEW_START_DATE.plus(PERIOD_PRODUCT_DATES.multipliedBy(2)))
                .endDate(NEW_END_DATE.plus(PERIOD_PRODUCT_DATES.multipliedBy(2)))
                .build();


        Period periodToShiftDate = Period.ofDays(DAYS / 2);
        ProductPriceBetweenDates existentPriceOverlapInferior = NEW_PRODUCT_PRICE.toBuilder()
                .startDate(NEW_START_DATE.minus(periodToShiftDate))
                .endDate(NEW_END_DATE.minus(periodToShiftDate))
                .priority(0)
                .build();

        // When
        Set<ProductPriceBetweenDates> newProductSet =
                newProductPriceInserter.insertNewProduct(NEW_PRODUCT_PRICE,
                        Set.of(productPriceBeforeNewDates, productPriceAfterNewDates, existentPriceOverlapInferior));

        // Then
        ProductPriceBetweenDates existentSsplitOverlapInferior = existentPriceOverlapInferior.toBuilder()
                .endDate(NEW_START_DATE)
                .build();
        Set<ProductPriceBetweenDates> expectedProductPrices = Set.of(
                NEW_PRODUCT_PRICE, productPriceAfterNewDates, productPriceBeforeNewDates, existentSsplitOverlapInferior);
        assertExpectedProducts(expectedProductPrices, newProductSet);
    }

    @Test
    @DisplayName(value = "when overlap with inferior range and higher priority, new must be split")
    public void shouldSplitNew() {

        ProductPriceBetweenDates productPriceBeforeNewDates = NEW_PRODUCT_PRICE.toBuilder()
                .startDate(NEW_START_DATE.minus(PERIOD_PRODUCT_DATES.multipliedBy(2)))
                .endDate(NEW_END_DATE.minus(PERIOD_PRODUCT_DATES.multipliedBy(2)))
                .build();

        ProductPriceBetweenDates productPriceAfterNewDates = NEW_PRODUCT_PRICE.toBuilder()
                .startDate(NEW_START_DATE.plus(PERIOD_PRODUCT_DATES.multipliedBy(2)))
                .endDate(NEW_END_DATE.plus(PERIOD_PRODUCT_DATES.multipliedBy(2)))
                .build();


        Period periodToShiftDate = Period.ofDays(DAYS / 2);
        ProductPriceBetweenDates priceOverlapInferior = NEW_PRODUCT_PRICE.toBuilder()
                .startDate(NEW_START_DATE.minus(periodToShiftDate))
                .endDate(NEW_END_DATE.minus(periodToShiftDate))
                .priority(2)
                .build();

        // When
        Set<ProductPriceBetweenDates> newProductSet =
                newProductPriceInserter.insertNewProduct(NEW_PRODUCT_PRICE,
                        Set.of(productPriceBeforeNewDates, productPriceAfterNewDates, priceOverlapInferior));

        // Then
        ProductPriceBetweenDates newSplitOverlapInferior = NEW_PRODUCT_PRICE.toBuilder()
                .startDate(NEW_END_DATE.minus(periodToShiftDate))
                .build();
        Set<ProductPriceBetweenDates> expectedProductPrices = Set.of(
                productPriceAfterNewDates, productPriceBeforeNewDates, priceOverlapInferior, newSplitOverlapInferior);
        assertExpectedProducts(expectedProductPrices, newProductSet);
    }

    @Test
    @DisplayName(value = "when overlap with inferior range and higher priority, new must be split")
    public void shouldSplitNewInThree() {

        // Days [0 - 5)
        ProductPriceBetweenDates oneToFiveP1 = NEW_PRODUCT_PRICE.toBuilder()
                .startDate(NEW_START_DATE.withDayOfMonth(1))
                .endDate(NEW_START_DATE.withDayOfMonth(5))
                .build();

        // Days [5 - 12)
        ProductPriceBetweenDates fiveTo12P0 = NEW_PRODUCT_PRICE.toBuilder()
                .startDate(NEW_START_DATE.withDayOfMonth(5))
                .endDate(NEW_START_DATE.withDayOfMonth(12))
                .priority(0)
                .build();

        // Days [12 - 15) - p2
        ProductPriceBetweenDates twelveTo15P2 = NEW_PRODUCT_PRICE.toBuilder()
                .startDate(NEW_START_DATE.withDayOfMonth(12))
                .endDate(NEW_START_DATE.withDayOfMonth(15))
                .priority(2)
                .build();

        ProductPriceBetweenDates fifteenTo17P0 = NEW_PRODUCT_PRICE.toBuilder()
                .startDate(NEW_START_DATE.withDayOfMonth(15))
                .endDate(NEW_START_DATE.withDayOfMonth(17))
                .priority(0)
                .build();

        // Days [17 - 22) - p0
        ProductPriceBetweenDates seventeenTo22P2 = NEW_PRODUCT_PRICE.toBuilder()
                .startDate(NEW_START_DATE.withDayOfMonth(17))
                .endDate(NEW_START_DATE.withDayOfMonth(22))
                .priority(2)
                .build();

        // Days [22 - 30] - p1
        ProductPriceBetweenDates lastP1 = NEW_PRODUCT_PRICE.toBuilder()
                .startDate(NEW_START_DATE.withDayOfMonth(22))
                .endDate(NEW_START_DATE.withDayOfMonth(30))
                .build();
        // When
        Set<ProductPriceBetweenDates> newProductSet =
                newProductPriceInserter.insertNewProduct(NEW_PRODUCT_PRICE,
                        Set.of(oneToFiveP1,
                                fiveTo12P0,
                                twelveTo15P2,
                                fifteenTo17P0,
                                seventeenTo22P2,
                                lastP1));

        // Then
        ProductPriceBetweenDates fiveTo10P0 = fiveTo12P0.toBuilder()
                .endDate(NEW_START_DATE.withDayOfMonth(10))
                .build();

        ProductPriceBetweenDates tenTo12newPrice = NEW_PRODUCT_PRICE.toBuilder()
                .startDate(NEW_START_DATE.withDayOfMonth(10))
                .endDate(NEW_START_DATE.withDayOfMonth(12))
                .build();
        ProductPriceBetweenDates fifteenTo17newPrice = NEW_PRODUCT_PRICE.toBuilder()
                .startDate(NEW_START_DATE.withDayOfMonth(15))
                .endDate(NEW_START_DATE.withDayOfMonth(17))
                .build();

        Set<ProductPriceBetweenDates> expectedProductPrices = Set.of(
                oneToFiveP1, fiveTo10P0, tenTo12newPrice, twelveTo15P2, fifteenTo17newPrice, seventeenTo22P2, lastP1);
        assertExpectedProducts(expectedProductPrices, newProductSet);
    }

    @Test
    @DisplayName(value = "when overlap with same priority, should throw exception")
    public void shouldFailIfOverlapsWithSamePriority() {
        ProductPriceBetweenDates productPriceBeforeNewDates = NEW_PRODUCT_PRICE.toBuilder()
                .startDate(NEW_START_DATE.minus(PERIOD_PRODUCT_DATES.multipliedBy(2)))
                .endDate(NEW_END_DATE.minus(PERIOD_PRODUCT_DATES.multipliedBy(2)))
                .build();

        ProductPriceBetweenDates productPriceAfterNewDates = NEW_PRODUCT_PRICE.toBuilder()
                .startDate(NEW_START_DATE.plus(Period.ofDays(DAYS / 2)))
                .endDate(NEW_END_DATE.plus(PERIOD_PRODUCT_DATES.multipliedBy(2)))
                .build();

        // When
        Executable executable = () ->
                newProductPriceInserter.insertNewProduct(
                        NEW_PRODUCT_PRICE, Set.of(productPriceBeforeNewDates, productPriceAfterNewDates));

        // Then
        assertThrows(SamePriorityOverlapException.class, executable);
    }

    private void assertExpectedProducts(
            Set<ProductPriceBetweenDates> expectedProductPrices, Set<ProductPriceBetweenDates> actualProductPrices) {

        assertNotNull(actualProductPrices);
        assertEquals(expectedProductPrices.size(), actualProductPrices.size());
        expectedProductPrices.forEach(o -> assertTrue(actualProductPrices.contains(o),
                String.format("not found: %s", o.toString())));
    }
}