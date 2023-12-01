package com.paradigma.poc.inditex.productpricebydate.domain.read;

import com.paradigma.poc.inditex.productpricebydate.domain.model.PriceByDateRequest;
import com.paradigma.poc.inditex.productpricebydate.domain.model.ProductIds;
import com.paradigma.poc.inditex.productpricebydate.domain.model.ProductPriceBetweenDates;
import com.paradigma.poc.inditex.productpricebydate.domain.ports.in.ProductPriceByDateCalculator;
import com.paradigma.poc.inditex.productpricebydate.domain.ports.out.ProductPriceBetweenDatesWriterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@ConditionalOnProperty("inditex.write-control")
public class WriteControlProductPriceCalculator implements ProductPriceByDateCalculator {

    private final ProductPriceBetweenDatesWriterRepository productPriceBetweenDatesRepository;

    @Override
    public Optional<ProductPriceBetweenDates> calculatePriceByDate(PriceByDateRequest priceByDateRequest) {

        ProductIds productIds = ProductIds.builder()
                .productId(priceByDateRequest.getProductId())
                .brandId(priceByDateRequest.getBrandId())
                .build();

        return productPriceBetweenDatesRepository.findUniqueForDate(productIds, priceByDateRequest.getDate());
    }
}
