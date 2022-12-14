package com.paradigma.poc.inditex.productbyprice.infra.repositories;

import com.paradigma.poc.inditex.productbyprice.domain.ProductIds;
import com.paradigma.poc.inditex.productbyprice.domain.ProductPriceBetweenDates;
import com.paradigma.poc.inditex.productbyprice.infra.model.ProductPriceJpaMapper;
import com.paradigma.poc.inditex.productbyprice.infra.model.ProductPriceJpa;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class JpaProductPriceBetweenDatesRepositoryTest {

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

        given(pricesJpaRepository.findByProductIdAndBrandId(1L, 2)).willReturn(List.of(productPrice1, productPrice2));

        ProductPriceBetweenDates productPriceBetweenDates1 = mock(ProductPriceBetweenDates.class);
        ProductPriceBetweenDates productPriceBetweenDates2 = mock(ProductPriceBetweenDates.class);
        given(productPriceJpaMapper.toProductBetweenDates(productPrice1)).willReturn(productPriceBetweenDates1);
        given(productPriceJpaMapper.toProductBetweenDates(productPrice2)).willReturn(productPriceBetweenDates2);

        ProductIds productIds = ProductIds.builder()
                .productId(1)
                .brandId(2)
                .build();

        // when
        List<ProductPriceBetweenDates> foundedProductPrices = productPriceBetweenDatesRepository.findByProductIds(productIds);

        // then
        assertNotNull(foundedProductPrices);
        assertEquals(2, foundedProductPrices.size());
        assertTrue(foundedProductPrices.contains(productPriceBetweenDates1));
        assertTrue(foundedProductPrices.contains(productPriceBetweenDates2));
    }

}