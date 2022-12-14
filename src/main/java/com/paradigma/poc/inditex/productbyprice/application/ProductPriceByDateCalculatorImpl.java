package com.paradigma.poc.inditex.productbyprice.application;

import com.paradigma.poc.inditex.productbyprice.application.data.ProductPriceBetweenDatesRepository;
import com.paradigma.poc.inditex.productbyprice.domain.PriceByDateRequest;
import com.paradigma.poc.inditex.productbyprice.domain.ProductIds;
import com.paradigma.poc.inditex.productbyprice.domain.ProductPriceBetweenDates;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProductPriceByDateCalculatorImpl implements ProductPriceByDateCalculator {

    private final ProductPriceBetweenDatesRepository productPriceBetweenDatesRepository;

    @Override
    public Optional<ProductPriceBetweenDates> calculatePriceByDate(PriceByDateRequest priceByDateRequest) {

        ProductIds productIds = ProductIds.builder()
                .productId(priceByDateRequest.getProductId())
                .brandId(priceByDateRequest.getBrandId())
                .build();

        List<ProductPriceBetweenDates> productPrices = productPriceBetweenDatesRepository.findByProductIds(productIds);

        return productPrices.stream()
                .filter(productPriceBetweenDates ->
                        isRequestDateBetweenProductDates(priceByDateRequest.getDate(), productPriceBetweenDates))
                .max(Comparator.comparing(ProductPriceBetweenDates::getPriority));
    }

    private boolean isRequestDateBetweenProductDates(
            LocalDateTime requestDate, ProductPriceBetweenDates productPriceBetweenDates) {

        return productPriceBetweenDates.getStartDate().isBefore(requestDate)
                && productPriceBetweenDates.getEndDate().isAfter(requestDate);
    }
}
