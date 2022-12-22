package com.paradigma.poc.inditex.productpricebydate.domain.ports.out;

import com.paradigma.poc.inditex.productpricebydate.domain.model.ProductPriceBetweenDates;

import java.util.Set;

public interface ProductPriceBetweenDatesWriterRepository extends ProductPriceBetweenDatesRepository {

    Set<ProductPriceBetweenDates> replaceAllByIds(Set<ProductPriceBetweenDates> productPriceBetweenDatesSet);

}