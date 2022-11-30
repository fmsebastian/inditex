package com.paradigma.poc.inditex.productbyprice.application;

import com.paradigma.poc.inditex.productbyprice.application.repositories.ProductPriceBetweenDatesRepository;
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
public class ProductPriceByDateCalculator {

    private final ProductPriceBetweenDatesRepository productPriceBetweenDatesRepository;

    public Optional<ProductPriceBetweenDates> calculatePriceByDate(PriceByDateRequest priceByDateRequest) {

        ProductIds productIds = ProductIds.builder()
                .productId(priceByDateRequest.getProductId())
                .brandId(priceByDateRequest.getBrandId())
                .build();

        List<ProductPriceBetweenDates> productPrices = productPriceBetweenDatesRepository.findByProductIds(productIds);

        Optional<ProductPriceBetweenDates> maxPriorityPrice = productPrices.stream()
                .filter(productPriceBetweenDates ->
                        isRequestDateBetweenProductDates(priceByDateRequest.getDate(), productPriceBetweenDates))
                .max(Comparator.comparing(ProductPriceBetweenDates::getPriority));

        return maxPriorityPrice;
    }

    private boolean isRequestDateBetweenProductDates(
            LocalDateTime requestDate, ProductPriceBetweenDates productPriceBetweenDates) {

        return productPriceBetweenDates.getStartDate().isBefore(requestDate)
                && productPriceBetweenDates.getEndDate().isAfter(requestDate);
    }
}
