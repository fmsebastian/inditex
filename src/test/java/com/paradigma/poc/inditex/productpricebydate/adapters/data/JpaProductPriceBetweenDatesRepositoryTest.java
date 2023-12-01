package com.paradigma.poc.inditex.productpricebydate.adapters.data;

import com.paradigma.poc.inditex.productpricebydate.adapters.data.entities.ProductPriceJpa;
import com.paradigma.poc.inditex.productpricebydate.adapters.data.entities.ProductPriceJpaMapper;
import com.paradigma.poc.inditex.productpricebydate.domain.exceptions.IllegalDataBaseException;
import com.paradigma.poc.inditex.productpricebydate.domain.model.ProductIds;
import com.paradigma.poc.inditex.productpricebydate.domain.model.ProductPriceBetweenDates;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class JpaProductPriceBetweenDatesRepositoryTest {

    public static final ProductIds PRODUCT_IDS = ProductIds.builder()
            .productId(1L)
            .brandId(2)
            .build();
    @Mock
    private PricesJpaRepository pricesJpaRepository;
    @Mock
    private ProductPriceJpaMapper productPriceJpaMapper;
    @InjectMocks
    private JpaProductPriceBetweenDatesRepository productPriceBetweenDatesRepository;

    @Test
    public void givenIdsShouldReturnProperProducts() {

        // given
        ProductPriceJpa productPrice1 = mock(ProductPriceJpa.class);
        ProductPriceJpa productPrice2 = mock(ProductPriceJpa.class);

        given(pricesJpaRepository.findByProductIdAndBrandId(PRODUCT_IDS.getProductId(), PRODUCT_IDS.getBrandId()))
                .willReturn(List.of(productPrice1, productPrice2));

        ProductPriceBetweenDates productPriceBetweenDates1 = mock(ProductPriceBetweenDates.class);
        ProductPriceBetweenDates productPriceBetweenDates2 = mock(ProductPriceBetweenDates.class);
        given(productPriceJpaMapper.toProductBetweenDates(productPrice1)).willReturn(productPriceBetweenDates1);
        given(productPriceJpaMapper.toProductBetweenDates(productPrice2)).willReturn(productPriceBetweenDates2);

        // when
        List<ProductPriceBetweenDates> foundedProductPrices =
                productPriceBetweenDatesRepository.findByProductIds(PRODUCT_IDS);

        // then
        assertNotNull(foundedProductPrices);
        assertEquals(2, foundedProductPrices.size());
        assertTrue(foundedProductPrices.contains(productPriceBetweenDates1));
        assertTrue(foundedProductPrices.contains(productPriceBetweenDates2));
    }

    @Test
    public void shouldReturnUniqueResult() {

        LocalDateTime searchDate = mock(LocalDateTime.class);

        ProductPriceJpa foundPrice = mock(ProductPriceJpa.class);
        given(pricesJpaRepository.findByProductIdAndBrandIdAndStartDateBeforeAndEndDateAfter(
                PRODUCT_IDS.getProductId(), PRODUCT_IDS.getBrandId(), searchDate, searchDate))
                .willReturn(List.of(foundPrice));

        ProductPriceBetweenDates expectedProduct = mock(ProductPriceBetweenDates.class);
        given(productPriceJpaMapper.toProductBetweenDates(foundPrice)).willReturn(expectedProduct);

        Optional<ProductPriceBetweenDates> uniqueForDate =
                productPriceBetweenDatesRepository.findUniqueForDate(PRODUCT_IDS, searchDate);

        assertTrue(uniqueForDate.isPresent());
        assertEquals(expectedProduct, uniqueForDate.get());
    }

    @Test
    public void shouldReturnEmptyWhenNoResult() {

        LocalDateTime searchDate = mock(LocalDateTime.class);

        given(pricesJpaRepository.findByProductIdAndBrandIdAndStartDateBeforeAndEndDateAfter(
                PRODUCT_IDS.getProductId(), PRODUCT_IDS.getBrandId(), searchDate, searchDate))
                .willReturn(Collections.emptyList());

        Optional<ProductPriceBetweenDates> uniqueForDate =
                productPriceBetweenDatesRepository.findUniqueForDate(PRODUCT_IDS, searchDate);

        assertFalse(uniqueForDate.isPresent());
    }

    @Test
    public void shouldThrowExceptionWhenNotUniqueResult() {

        LocalDateTime searchDate = mock(LocalDateTime.class);

        List foundedPrices = mock(List.class);
        given(pricesJpaRepository.findByProductIdAndBrandIdAndStartDateBeforeAndEndDateAfter(
                PRODUCT_IDS.getProductId(), PRODUCT_IDS.getBrandId(), searchDate, searchDate))
                .willReturn(foundedPrices);

        given(foundedPrices.size()).willReturn(2);

        Executable executable =
                () -> productPriceBetweenDatesRepository.findUniqueForDate(PRODUCT_IDS, searchDate);

        assertThrows(IllegalDataBaseException.class, executable);
    }
}