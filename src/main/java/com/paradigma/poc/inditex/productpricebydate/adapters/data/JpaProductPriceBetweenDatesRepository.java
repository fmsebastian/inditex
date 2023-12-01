package com.paradigma.poc.inditex.productpricebydate.adapters.data;

import com.paradigma.poc.inditex.productpricebydate.adapters.data.entities.ProductPriceJpa;
import com.paradigma.poc.inditex.productpricebydate.adapters.data.entities.ProductPriceJpaMapper;
import com.paradigma.poc.inditex.productpricebydate.domain.exceptions.IllegalDataBaseException;
import com.paradigma.poc.inditex.productpricebydate.domain.model.ProductIds;
import com.paradigma.poc.inditex.productpricebydate.domain.model.ProductPriceBetweenDates;
import com.paradigma.poc.inditex.productpricebydate.domain.ports.out.ProductPriceBetweenDatesWriterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
    public Set<ProductPriceBetweenDates> replaceAllByIds(
            ProductIds productIds, Set<ProductPriceBetweenDates> productPriceBetweenDatesSet) {

        if (CollectionUtils.isEmpty(productPriceBetweenDatesSet)) {

            return productPriceBetweenDatesSet;
        }

        List<ProductPriceJpa> byProductIdAndBrandId =
                pricesJpaRepository.findByProductIdAndBrandId(productIds.getProductId(), productIds.getBrandId());

        pricesJpaRepository.deleteAll(byProductIdAndBrandId);

        List<ProductPriceJpa> productPriceJpas = pricesJpaRepository.saveAll(productPriceBetweenDatesSet.stream()
                .map(productPriceJpaMapper::fromProductBetweenDates)
                .collect(Collectors.toSet()));

        return productPriceJpas.stream()
                .map(productPriceJpaMapper::toProductBetweenDates)
                .collect(Collectors.toSet());
    }

    @Override
    public Optional<ProductPriceBetweenDates> findUniqueForDate(ProductIds productIds, LocalDateTime date) {

        List<ProductPriceJpa> pricesForDate = pricesJpaRepository
                .findByProductIdAndBrandIdAndStartDateBeforeAndEndDateAfter(
                        productIds.getProductId(), productIds.getBrandId(), date, date);

        if (pricesForDate.size() > 1) {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            throw new IllegalDataBaseException(
                    String.format("Should not find more than one option for date %s", date.format(formatter)));
        }

        return pricesForDate.stream()
                .findFirst()
                .map(productPriceJpaMapper::toProductBetweenDates);
    }
}
