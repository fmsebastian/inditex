package com.paradigma.poc.inditex.productpricebydate.adapters.data;

import com.paradigma.poc.inditex.productpricebydate.adapters.data.entities.PricesIdsJpa;
import com.paradigma.poc.inditex.productpricebydate.adapters.data.entities.ProductPriceJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PricesJpaRepository extends JpaRepository<ProductPriceJpa, PricesIdsJpa> {

    List<ProductPriceJpa> findByProductIdAndBrandId(Long productId, Integer brandId);

    List<ProductPriceJpa> findByProductIdAndBrandIdAndStartDateBeforeAndEndDateAfter(
            Long productId, Integer brandId, LocalDateTime afterStartDate, LocalDateTime beforeEndDate);

}
