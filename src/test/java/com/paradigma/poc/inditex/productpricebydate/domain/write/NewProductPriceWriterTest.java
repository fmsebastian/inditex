package com.paradigma.poc.inditex.productpricebydate.domain.write;

import com.paradigma.poc.inditex.productpricebydate.domain.model.NewProductRequest;
import com.paradigma.poc.inditex.productpricebydate.domain.model.NewProductRequestMapper;
import com.paradigma.poc.inditex.productpricebydate.domain.model.ProductIds;
import com.paradigma.poc.inditex.productpricebydate.domain.model.ProductPriceBetweenDates;
import com.paradigma.poc.inditex.productpricebydate.domain.ports.out.ProductPriceBetweenDatesWriterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class NewProductPriceWriterTest {

    public static final ProductIds PRODUCT_IDS = ProductIds.builder().productId(1L).brandId(1).build();

    @Mock
    private ProductPriceBetweenDatesWriterRepository productPriceBetweenDatesWriterRepository;
    @Mock
    private NewProductRequestMapper newProductRequestMapper;
    @Mock
    private NewProductPriceInserter newProductPriceInserter;

    private NewProductPriceWriter newProductPriceWriter;

    @BeforeEach
    public void setUp() {

        newProductPriceWriter = new NewProductPriceWriter(
                productPriceBetweenDatesWriterRepository,
                newProductPriceInserter,
                newProductRequestMapper);
    }

    @Test
    public void insertNewProduct() {

        NewProductRequest newProductRequest = mock(NewProductRequest.class);
        ProductPriceBetweenDates newProductPrice = mock(ProductPriceBetweenDates.class);

        given(newProductRequestMapper.toProductBetweenDates(newProductRequest)).willReturn(newProductPrice);
        given(newProductPrice.getProductIds()).willReturn(PRODUCT_IDS);

        ProductPriceBetweenDates existentProduct1 = mock(ProductPriceBetweenDates.class);
        ProductPriceBetweenDates existentProduct2 = mock(ProductPriceBetweenDates.class);
        List<ProductPriceBetweenDates> existentProducts = List.of(existentProduct1, existentProduct2);

        given(productPriceBetweenDatesWriterRepository.findByProductIds(PRODUCT_IDS)).willReturn(existentProducts);
        Set<ProductPriceBetweenDates> existentProductsAsSet = Set.of(existentProduct1, existentProduct2);

        Set<ProductPriceBetweenDates> newProductPricesSet = mock(Set.class);
        given(newProductPriceInserter.insertNewProduct(newProductPrice, existentProductsAsSet))
                .willReturn(newProductPricesSet);

        Set<ProductPriceBetweenDates> savedProductPrices = newProductPriceWriter.saveNewProductPrice(newProductRequest);

        assertNotNull(savedProductPrices);
        assertEquals(newProductPricesSet, savedProductPrices);
        then(productPriceBetweenDatesWriterRepository).should().replaceAllByIds(newProductPricesSet);
    }

}