package com.paradigma.poc.inditex.productpricebydate.domain.logic;

import com.paradigma.poc.inditex.productpricebydate.domain.ports.out.ProductPriceBetweenDatesRepository;
import com.paradigma.poc.inditex.productpricebydate.domain.model.PriceByDateRequest;
import com.paradigma.poc.inditex.productpricebydate.domain.model.ProductIds;
import com.paradigma.poc.inditex.productpricebydate.domain.model.ProductPriceBetweenDates;
import com.paradigma.poc.inditex.productpricebydate.domain.ports.in.ProductPriceByDateCalculator;
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

        boolean isProductStartDateBeforeOrEqualsRequestDate =
                productPriceBetweenDates.getStartDate().isBefore(requestDate)
                        || productPriceBetweenDates.getStartDate().isEqual(requestDate);

        return isProductStartDateBeforeOrEqualsRequestDate && productPriceBetweenDates.getEndDate().isAfter(requestDate);
    }
}
