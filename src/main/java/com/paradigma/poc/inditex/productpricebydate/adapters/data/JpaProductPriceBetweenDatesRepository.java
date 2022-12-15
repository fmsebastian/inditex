package com.paradigma.poc.inditex.productpricebydate.adapters.data;

import com.paradigma.poc.inditex.productpricebydate.domain.ports.out.ProductPriceBetweenDatesRepository;
import com.paradigma.poc.inditex.productpricebydate.domain.model.ProductIds;
import com.paradigma.poc.inditex.productpricebydate.domain.model.ProductPriceBetweenDates;
import com.paradigma.poc.inditex.productpricebydate.adapters.data.entities.ProductPriceJpaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class JpaProductPriceBetweenDatesRepository implements ProductPriceBetweenDatesRepository {

    private final PricesJpaRepository pricesJpaRepository;

    private final ProductPriceJpaMapper productPriceJpaMapper;

    @Override
    public List<ProductPriceBetweenDates> findByProductIds(ProductIds productIds) {

        return pricesJpaRepository.findByProductIdAndBrandId(productIds.getProductId(), productIds.getBrandId())
                .stream()
                .map(productPriceJpaMapper::toProductBetweenDates)
                .collect(Collectors.toList());
    }
}
