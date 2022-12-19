package com.paradigma.poc.inditex.productpricebydate;

import com.paradigma.poc.inditex.productpricebydate.adapters.data.PricesJpaRepository;
import com.paradigma.poc.inditex.productpricebydate.adapters.data.entities.ProductPriceJpa;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductPriceByDateIntegrationTest {

    private static final int PRODUCT_ID = 35455;
    private static final int BRAND_ID = 1;

    private static final int OTHER_PRODUCT_ID = 7894;
    private static final int OTHER_BRAND_ID = 55;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    private static ProductPriceJpa PRODUCT_PRICE_1 = ProductPriceJpa.builder()
            .productId((long) PRODUCT_ID)
            .brandId(BRAND_ID)
            .startDate(LocalDateTime.parse("2020-06-14T00:00:00"))
            .endDate(LocalDateTime.parse("2020-12-31T23:59:59"))
            .price(35.50)
            .currency("EUR")
            .priority(0)
            .priceList(1)
            .build();

    private static ProductPriceJpa PRODUCT_PRICE_2 = ProductPriceJpa.builder()
            .productId((long) PRODUCT_ID)
            .brandId(BRAND_ID)
            .startDate(LocalDateTime.parse("2020-06-14T15:00:00"))
            .endDate(LocalDateTime.parse("2020-06-14T18:30:00"))
            .price(25.45)
            .currency("EUR")
            .priority(1)
            .priceList(2)
            .build();

    private static ProductPriceJpa PRODUCT_PRICE_3 = ProductPriceJpa.builder()
            .productId((long) PRODUCT_ID)
            .brandId(BRAND_ID)
            .startDate(LocalDateTime.parse("2020-06-15T00:00:00"))
            .endDate(LocalDateTime.parse("2020-06-15T11:00:00"))
            .price(30.50)
            .currency("EUR")
            .priority(1)
            .priceList(3)
            .build();

    private static ProductPriceJpa PRODUCT_PRICE_4 = ProductPriceJpa.builder()
            .productId((long) PRODUCT_ID)
            .brandId(BRAND_ID)
            .startDate(LocalDateTime.parse("2020-06-15T16:00:00"))
            .endDate(LocalDateTime.parse("2020-06-30T23:59:59"))
            .price(38.95)
            .currency("EUR")
            .priority(1)
            .priceList(4)
            .build();

    private static ProductPriceJpa PRODUCT_PRICE_5 = ProductPriceJpa.builder()
            .productId((long) PRODUCT_ID)
            .brandId(BRAND_ID)
            .startDate(LocalDateTime.parse("2020-06-15T13:00:00"))
            .endDate(LocalDateTime.parse("2020-06-15T19:59:59"))
            .price(15.00)
            .currency("EUR")
            .priority(2)
            .priceList(4)
            .build();

    private static ProductPriceJpa DIFFERENT_PRODUCT_1 = ProductPriceJpa.builder()
            .productId((long) OTHER_PRODUCT_ID)
            .brandId(BRAND_ID)
            .startDate(LocalDateTime.parse("2020-06-15T00:00:00"))
            .endDate(LocalDateTime.parse("2020-06-30T23:59:59"))
            .price(10.00)
            .currency("EUR")
            .priority(0)
            .priceList(1)
            .build();

    private static ProductPriceJpa DIFFERENT_PRODUCT_2 = ProductPriceJpa.builder()
            .productId((long) OTHER_PRODUCT_ID)
            .brandId(BRAND_ID)
            .startDate(LocalDateTime.parse("2020-06-15T16:00:00"))
            .endDate(LocalDateTime.parse("2020-06-15T18:59:59"))
            .price(20.00)
            .currency("EUR")
            .priority(1)
            .priceList(2)
            .build();

    private static ProductPriceJpa DIFFERENT_BRAND_1 = ProductPriceJpa.builder()
            .productId((long) PRODUCT_ID)
            .brandId(OTHER_BRAND_ID)
            .startDate(LocalDateTime.parse("2020-06-15T00:00:00"))
            .endDate(LocalDateTime.parse("2020-06-30T23:59:59"))
            .price(10.00)
            .currency("EUR")
            .priority(0)
            .priceList(1)
            .build();

    private static ProductPriceJpa DIFFERENT_BRAND_2 = ProductPriceJpa.builder()
            .productId((long) PRODUCT_ID)
            .brandId(OTHER_BRAND_ID)
            .startDate(LocalDateTime.parse("2020-06-15T16:00:00"))
            .endDate(LocalDateTime.parse("2020-06-15T18:59:59"))
            .price(20.00)
            .currency("EUR")
            .priority(1)
            .priceList(2)
            .build();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PricesJpaRepository pricesJpaRepository;

    /**
     * Test 1: petición a las 10:00 del día 14 del producto 35455 para la brand 1 (ZARA)
     * Test 2: petición a las 16:00 del día 14 del producto 35455 para la brand 1 (ZARA)
     * Test 3: petición a las 21:00 del día 14 del producto 35455 para la brand 1 (ZARA)
     * Test 4: petición a las 10:00 del día 15 del producto 35455 para la brand 1 (ZARA)
     * Test 5: petición a las 21:00 del día 16 del producto 35455 para la brand 1 (ZARA)
     * <p>
     * BRAND_ID         START_DATE               END_DATE      PRICE_LIST   PRODUCT_ID     PRIORITY   PRICE   CURR
     * ------------------------------------------------------------------------------------------------------------
     * 1         2020-06-14-00.00.00   2020-12-31-23.59.59      1           35455                0    35.50    EUR
     * 1         2020-06-14-15.00.00   2020-06-14-18.30.00      2           35455                1    25.45    EUR
     * 1         2020-06-15-00.00.00   2020-06-15-11.00.00      3           35455                1    30.50    EUR
     * 1         2020-06-15-16.00.00   2020-06-30-23.59.59      4           35455                1    38.95    EUR
     * 1         2020-06-15-13.00.00   2020-06-15-19.59.59      4           35455                2    15.00    EUR
     */
    private static Stream<Arguments> defineCases() {

        return Stream.of(
                // test name, input product, input brand, input date, expected database product result
                // User cases
                Arguments.of("User case 1",
                        PRODUCT_ID, BRAND_ID, "2020-06-14T10:00:00", PRODUCT_PRICE_1),  // Test 1
                Arguments.of("User case 2",
                        PRODUCT_ID, BRAND_ID, "2020-06-14T16:00:00", PRODUCT_PRICE_2),  // Test 2
                Arguments.of("User case 3",
                        PRODUCT_ID, BRAND_ID, "2020-06-14T21:00:00", PRODUCT_PRICE_1),  // Test 3
                Arguments.of("User case 4",
                        PRODUCT_ID, BRAND_ID, "2020-06-15T10:00:00", PRODUCT_PRICE_3),  // Test 4
                Arguments.of("User case 5",
                        PRODUCT_ID, BRAND_ID, "2020-06-16T21:00:00", PRODUCT_PRICE_4),  // Test 5
                // Corner and extra cases
                Arguments.of("Corner case matching end_date",
                        PRODUCT_ID, BRAND_ID, "2020-06-15T11:00:00", PRODUCT_PRICE_1),
                Arguments.of("Corner case matching start_date",
                        PRODUCT_ID, BRAND_ID, "2020-06-15T00:00:00", PRODUCT_PRICE_3),
                Arguments.of("Priority 2 over priority 0",
                        PRODUCT_ID, BRAND_ID, "2020-06-15T14:00:00", PRODUCT_PRICE_5),
                Arguments.of("Priority 2 over priority 0 and 1",
                        PRODUCT_ID, BRAND_ID, "2020-06-15T18:00:00", PRODUCT_PRICE_5),
                Arguments.of("Test end of base interval",
                        PRODUCT_ID, BRAND_ID, "2020-08-15T11:00:00", PRODUCT_PRICE_1),
                // Checking other productId and other branchId
                Arguments.of("Other product id, priority 0",
                        OTHER_PRODUCT_ID, BRAND_ID, "2020-06-15T10:00:00", DIFFERENT_PRODUCT_1),   // Extra test
                Arguments.of("Other product id, priority 1",
                        OTHER_PRODUCT_ID, BRAND_ID, "2020-06-15T18:00:00", DIFFERENT_PRODUCT_2),   // Extra test
                Arguments.of("Other brand id, priority 0",
                        PRODUCT_ID, OTHER_BRAND_ID, "2020-06-15T10:00:00", DIFFERENT_BRAND_1),   // Extra test
                Arguments.of("Other brand id, priority 1",
                        PRODUCT_ID, OTHER_BRAND_ID, "2020-06-15T18:00:00", DIFFERENT_BRAND_2)
        );
    }

    @BeforeEach
    public void initRepository() {

        pricesJpaRepository.save(PRODUCT_PRICE_1);
        pricesJpaRepository.save(PRODUCT_PRICE_2);
        pricesJpaRepository.save(PRODUCT_PRICE_3);
        pricesJpaRepository.save(PRODUCT_PRICE_4);
        pricesJpaRepository.save(PRODUCT_PRICE_5);
        pricesJpaRepository.save(DIFFERENT_PRODUCT_1);
        pricesJpaRepository.save(DIFFERENT_PRODUCT_2);
        pricesJpaRepository.save(DIFFERENT_BRAND_1);
        pricesJpaRepository.save(DIFFERENT_BRAND_2);
    }

    @AfterEach
    public void resetRepository() {

        pricesJpaRepository.deleteAll();
    }

    @SneakyThrows
    @ParameterizedTest(name = "{0}")
    @MethodSource("defineCases")
    public void shouldReturnExpected(String testName, Integer productId, Integer brandId, String date, ProductPriceJpa expectedResponse) {

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/productprices")
                        .queryParam("brandId", String.valueOf(brandId))
                        .queryParam("productId", String.valueOf(productId))
                        .queryParam("applicationDate", date))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.brandId").value(expectedResponse.getBrandId()))
                .andExpect(jsonPath("$.productId").value(expectedResponse.getProductId()))
                .andExpect(jsonPath("$.startDate").value(expectedResponse.getStartDate().format(FORMATTER)))
                .andExpect(jsonPath("$.endDate").value(expectedResponse.getEndDate().format(FORMATTER)))
                .andExpect(jsonPath("$.priceList").value(expectedResponse.getPriceList()))
                .andExpect(jsonPath("$.price").value(expectedResponse.getPrice()))
                .andExpect(jsonPath("$.currency").value(expectedResponse.getCurrency()));
    }

}
