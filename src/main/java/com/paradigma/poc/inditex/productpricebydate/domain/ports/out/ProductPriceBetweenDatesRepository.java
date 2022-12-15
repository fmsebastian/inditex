package com.paradigma.poc.inditex.productpricebydate.domain.ports.out;

import com.paradigma.poc.inditex.productpricebydate.domain.model.ProductIds;
import com.paradigma.poc.inditex.productpricebydate.domain.model.ProductPriceBetweenDates;

import java.util.List;

public interface ProductPriceBetweenDatesRepository {

    List<ProductPriceBetweenDates> findByProductIds(ProductIds productIds);

}