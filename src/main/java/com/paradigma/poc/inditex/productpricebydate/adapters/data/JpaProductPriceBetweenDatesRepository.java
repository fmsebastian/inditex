package com.paradigma.poc.inditex.productpricebydate.adapters.data;

import com.paradigma.poc.inditex.productpricebydate.adapters.data.entities.ProductPriceJpaMapper;
import com.paradigma.poc.inditex.productpricebydate.domain.model.ProductIds;
import com.paradigma.poc.inditex.productpricebydate.domain.model.ProductPriceBetweenDates;
import com.paradigma.poc.inditex.productpricebydate.domain.ports.out.ProductPriceBetweenDatesWriterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class JpaProductPriceBetweenDatesRepository implements ProductPriceBetweenDatesWriterRepository {

    private final PricesJpaRepository pricesJpaRepository;

    private final ProductPriceJpaMapper productPriceJpaMapper;

    @Override
    public List<ProductPriceBetweenDates> findByProductIds(ProductIds productIds) {

        return pricesJpaRepository.findByProductIdAndBrandId(productIds.getProductId(), productIds.getBrandId())
                .stream()
                .map(productPriceJpaMapper::toProductBetweenDates)
                .collect(Collectors.toList());
    }

    @Override
    public Set<ProductPriceBetweenDates> replaceAllByIds(Set<ProductPriceBetweenDates> productPriceBetweenDatesSet) {

        return null;
    }
}
