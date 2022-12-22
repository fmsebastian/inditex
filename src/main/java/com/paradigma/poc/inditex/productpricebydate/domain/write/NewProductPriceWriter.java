package com.paradigma.poc.inditex.productpricebydate.domain.write;

import com.paradigma.poc.inditex.productpricebydate.domain.model.NewProductRequest;
import com.paradigma.poc.inditex.productpricebydate.domain.model.NewProductRequestMapper;
import com.paradigma.poc.inditex.productpricebydate.domain.model.ProductPriceBetweenDates;
import com.paradigma.poc.inditex.productpricebydate.domain.ports.out.ProductPriceBetweenDatesWriterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class NewProductPriceWriter {

    private final ProductPriceBetweenDatesWriterRepository productPriceBetweenDatesRepository;

    private final NewProductPriceInserter newProductPriceInserter;

    private final NewProductRequestMapper newProductRequestMapper;

    public Set<ProductPriceBetweenDates> saveNewProductPrice(NewProductRequest newProduct) {

        ProductPriceBetweenDates newProductPrice = newProductRequestMapper.toProductBetweenDates(newProduct);

        Set<ProductPriceBetweenDates> existentProduct = Set.copyOf(
                productPriceBetweenDatesRepository.findByProductIds(newProductPrice.getProductIds()));

        Set<ProductPriceBetweenDates> productsWithNewInserted =
                newProductPriceInserter.insertNewProduct(newProductPrice, existentProduct);

        productPriceBetweenDatesRepository.replaceAllByIds(newProductPrice.getProductIds(), productsWithNewInserted);

        return productsWithNewInserted;
    }
}
