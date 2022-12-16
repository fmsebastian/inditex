package com.paradigma.poc.inditex.productpricebydate.domain.logic;

import com.paradigma.poc.inditex.productpricebydate.domain.ports.out.ProductPriceBetweenDatesRepository;
import com.paradigma.poc.inditex.productpricebydate.domain.model.PriceByDateRequest;
import com.paradigma.poc.inditex.productpricebydate.domain.model.ProductIds;
import com.paradigma.poc.inditex.productpricebydate.domain.model.ProductPriceBetweenDates;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ProductPriceByDateCalculatorTest {

    private static final long PRODUCT_ID = 35455;
    private static final int BRAND_ID = 1;

    /**
     * 1         2020-06-14-00.00.00                        2020-12-31-23.59.59                        1                        35455                0                        35.50            EUR
     * 1         2020-06-14-15.00.00                        2020-06-14-18.30.00                        2                        35455                1                        25.45            EUR
     * 1         2020-06-15-00.00.00                        2020-06-15-11.00.00                        3                        35455                1                        30.50            EUR
     * 1         2020-06-15-16.00.00                        2020-06-30-23.59.59                        4                        35455                1                        38.95            EUR
     */
    private static ProductIds PRODUCT_IDS = ProductIds.builder().productId(PRODUCT_ID).brandId(BRAND_ID).build();

    private static ProductPriceBetweenDates PRODUCT_1 = ProductPriceBetweenDates.builder()
            .productIds(PRODUCT_IDS)
            .startDate(LocalDateTime.parse("2020-06-14T00:00:00"))
            .endDate(LocalDateTime.parse("2020-12-31T23:59:59"))
            .price(35.50)
            .currency("EUR")
            .priority(0)
            .priceList(1)
            .build();
    private static ProductPriceBetweenDates PRODUCT_2 = ProductPriceBetweenDates.builder()
            .productIds(PRODUCT_IDS)
            .startDate(LocalDateTime.parse("2020-06-14T15:00:00"))
            .endDate(LocalDateTime.parse("2020-06-14T18:30:00"))
            .price(25.45)
            .currency("EUR")
            .priority(1)
            .priceList(2)
            .build();
    private static ProductPriceBetweenDates PRODUCT_3 = ProductPriceBetweenDates.builder()
            .productIds(PRODUCT_IDS)
            .startDate(LocalDateTime.parse("2020-06-15T00:00:00"))
            .endDate(LocalDateTime.parse("2020-06-15T11:00:00"))
            .price(30.50)
            .currency("EUR")
            .priority(1)
            .priceList(3)
            .build();
    private static ProductPriceBetweenDates PRODUCT_4 = ProductPriceBetweenDates.builder()
            .productIds(PRODUCT_IDS)
            .startDate(LocalDateTime.parse("2020-06-15T16:00:00"))
            .endDate(LocalDateTime.parse("2020-06-30T23:59:59"))
            .price(38.95)
            .currency("EUR")
            .priority(1)
            .priceList(4)
            .build();
    private static ProductPriceBetweenDates PRODUCT_5 = ProductPriceBetweenDates.builder()
            .productIds(PRODUCT_IDS)
            .startDate(LocalDateTime.parse("2020-06-15T13:00:00"))
            .endDate(LocalDateTime.parse("2020-06-15T19:59:59"))
            .price(15.00)
            .currency("EUR")
            .priority(2)
            .priceList(4)
            .build();

    @Mock
    private ProductPriceBetweenDatesRepository productPriceBetweenDatesRepository;

    @InjectMocks
    private ProductPriceByDateCalculatorImpl productPriceByDateCalculator;

    @BeforeEach
    private void setUpRepository() {

        List<ProductPriceBetweenDates> productPriceBetweenDatesList = List.of(
                PRODUCT_1,
                PRODUCT_2,
                PRODUCT_3,
                PRODUCT_4,
                PRODUCT_5
        );

        given(productPriceBetweenDatesRepository.findByProductIds(PRODUCT_IDS)).willReturn(productPriceBetweenDatesList);
    }

    @ParameterizedTest(name = "should return proper product when input date is {0}")
    @MethodSource("defineCases")
    void shouldCalculateCorrectDate(String inputDate, ProductPriceBetweenDates expectedProductPrice) {

        // Given
        PriceByDateRequest priceByDateRequest = PriceByDateRequest.builder()
                .date(LocalDateTime.parse(inputDate))
                .productId(expectedProductPrice.getProductIds().getProductId())
                .brandId(expectedProductPrice.getProductIds().getBrandId())
                .build();

        Optional<ProductPriceBetweenDates> returnedPriceByDate =
                productPriceByDateCalculator.calculatePriceByDate(priceByDateRequest);

        assertTrue(returnedPriceByDate.isPresent());
        assertEquals(expectedProductPrice, returnedPriceByDate.get());
    }

    /**
     * Test 1: petición a las 10:00 del día 14 del producto 35455   para la brand 1 (ZARA)
     * Test 2: petición a las 16:00 del día 14 del producto 35455   para la brand 1 (ZARA)
     * Test 3: petición a las 21:00 del día 14 del producto 35455   para la brand 1 (ZARA)
     * Test 4: petición a las 10:00 del día 15 del producto 35455   para la brand 1 (ZARA)
     * Test 5: petición a las 21:00 del día 16 del producto 35455   para la brand 1 (ZARA)
     */
    private static Stream<Arguments> defineCases() {

        return Stream.of(
                // Input date,      expected result
                Arguments.of("2020-06-14T10:00:00", PRODUCT_1),  // Test 1
                Arguments.of("2020-06-14T16:00:00", PRODUCT_2),  // Test 2
                Arguments.of("2020-06-14T21:00:00", PRODUCT_1),  // Test 3
                Arguments.of("2020-06-15T10:00:00", PRODUCT_3),  // Test 4
                Arguments.of("2020-06-15T12:00:00", PRODUCT_1),  // Extra test
                Arguments.of("2020-06-16T21:00:00", PRODUCT_4),  // Test 5
                Arguments.of("2020-07-10T15:00:00", PRODUCT_1),   // Extra test
                Arguments.of("2020-06-15T11:00:00", PRODUCT_1),   // Extra test
                Arguments.of("2020-06-15T00:00:00", PRODUCT_3),   // Extra test
                Arguments.of("2020-06-15T14:00:00", PRODUCT_5),   // Extra test
                Arguments.of("2020-06-15T18:00:00", PRODUCT_5),   // Extra test
                Arguments.of("2020-08-15T11:00:00", PRODUCT_1)   // Extra test
        );
    }
}