package com.paradigma.poc.inditex.productbyprice.application;

import com.paradigma.poc.inditex.productbyprice.domain.PriceByDateRequest;
import com.paradigma.poc.inditex.productbyprice.domain.ProductPriceBetweenDates;

import java.util.Optional;

public interface ProductPriceByDateCalculator {

    Optional<ProductPriceBetweenDates> calculatePriceByDate(PriceByDateRequest priceByDateRequest);
}
