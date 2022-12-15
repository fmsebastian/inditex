package com.paradigma.poc.inditex.productpricebydate.adapters.web;

import com.paradigma.poc.inditex.productpricebydate.domain.ports.in.ProductPriceByDateCalculator;
import com.paradigma.poc.inditex.productpricebydate.domain.model.PriceByDateRequest;
import com.paradigma.poc.inditex.productpricebydate.domain.model.ProductPriceBetweenDates;
import com.paradigma.poc.inditex.productpricebydate.adapters.web.resources.ProductPriceForDateResponse;
import com.paradigma.poc.inditex.productpricebydate.adapters.web.resources.ProductPriceForDateResponseMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ProductPriceByDateControllerTest {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    private static Integer DEFAULT_BRAND_ID = 1;
    private static Long DEFAULT_PRODUCT_ID = 1234L;
    private static LocalDateTime DEFAULT_APPLICATION_DATE = LocalDateTime.parse("2020-06-14T15:30:00");

    @Mock
    private ProductPriceByDateCalculator productPriceByDateCalculator;
    @Mock
    private ProductPriceForDateResponseMapper productPriceForDateResponseMapper;

    @InjectMocks
    private ProductPriceByDateController productPriceByDateController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {

        mockMvc = MockMvcBuilders
                .standaloneSetup(productPriceByDateController)
                .build();
    }

    @SneakyThrows
    @Test
    public void shouldReturnProperProductId() {

        PriceByDateRequest priceByDateRequest = PriceByDateRequest.builder()
                .brandId(DEFAULT_BRAND_ID)
                .productId(DEFAULT_PRODUCT_ID)
                .date(DEFAULT_APPLICATION_DATE)
                .build();

        ProductPriceBetweenDates productPriceBetweenDates = mock(ProductPriceBetweenDates.class);

        given(productPriceByDateCalculator.calculatePriceByDate(priceByDateRequest))
                .willReturn(Optional.of(productPriceBetweenDates));

        ProductPriceForDateResponse responseMock =
                ProductPriceForDateResponse.builder()
                        .brandId(DEFAULT_BRAND_ID)
                        .productId(DEFAULT_PRODUCT_ID)
                        .startDate(DEFAULT_APPLICATION_DATE)
                        .price(20.0)
                        .build();

        given(productPriceForDateResponseMapper.fromProductBetweenDates(productPriceBetweenDates))
                .willReturn(responseMock);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/productprices")
                        .queryParam("brandId", DEFAULT_BRAND_ID.toString())
                        .queryParam("productId", DEFAULT_PRODUCT_ID.toString())
                        .queryParam("applicationDate", DEFAULT_APPLICATION_DATE.format(FORMATTER)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.brandId").value(DEFAULT_BRAND_ID))
                .andExpect(jsonPath("$.productId").value(DEFAULT_PRODUCT_ID))
                .andExpect(jsonPath("$.price").value(20.0));
        // TODO: test dates (why mockMvc response traits them as array instead of string?
    }

    @SneakyThrows
    @Test
    public void shouldReturn404WhenNoProductFound() {

        PriceByDateRequest priceByDateRequest = PriceByDateRequest.builder()
                .brandId(DEFAULT_BRAND_ID)
                .productId(DEFAULT_PRODUCT_ID)
                .date(DEFAULT_APPLICATION_DATE)
                .build();

        given(productPriceByDateCalculator.calculatePriceByDate(priceByDateRequest))
                .willReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/productprices")
                        .queryParam("brandId", DEFAULT_BRAND_ID.toString())
                        .queryParam("productId", DEFAULT_PRODUCT_ID.toString())
                        .queryParam("applicationDate", DEFAULT_APPLICATION_DATE.format(FORMATTER)))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals(
                        String.format("Price not found for date %s", DEFAULT_APPLICATION_DATE.format(FORMATTER)),
                        result.getResponse().getErrorMessage()));
    }
}