package com.paradigma.poc.inditex.productbyprice.application.repositories;

import com.paradigma.poc.inditex.productbyprice.domain.ProductIds;
import com.paradigma.poc.inditex.productbyprice.domain.ProductPriceBetweenDates;

import java.util.List;

public interface ProductPriceBetweenDatesRepository {

    List<ProductPriceBetweenDates> findByProductIds(ProductIds productIds);

}