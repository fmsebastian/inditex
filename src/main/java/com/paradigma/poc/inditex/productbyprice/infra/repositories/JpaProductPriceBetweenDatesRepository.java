package com.paradigma.poc.inditex.productbyprice.infra.repositories;

import com.paradigma.poc.inditex.productbyprice.application.data.ProductPriceBetweenDatesRepository;
import com.paradigma.poc.inditex.productbyprice.domain.ProductIds;
import com.paradigma.poc.inditex.productbyprice.domain.ProductPriceBetweenDates;
import com.paradigma.poc.inditex.productbyprice.infra.model.ProductPriceJpaMapper;
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

        return pricesJpaRepository.findByProductIdAndBrandId(productIds.getProductId().longValue(), productIds.getBrandId())
                .stream()
                .map(productPriceJpaMapper::toProductBetweenDates)
                .collect(Collectors.toList());
    }
}
