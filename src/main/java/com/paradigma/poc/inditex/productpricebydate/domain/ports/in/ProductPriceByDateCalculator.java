package com.paradigma.poc.inditex.productpricebydate.domain.ports.in;

import com.paradigma.poc.inditex.productpricebydate.domain.model.PriceByDateRequest;
import com.paradigma.poc.inditex.productpricebydate.domain.model.ProductPriceBetweenDates;

import java.util.Optional;

public interface ProductPriceByDateCalculator {

    Optional<ProductPriceBetweenDates> calculatePriceByDate(PriceByDateRequest priceByDateRequest);
}
