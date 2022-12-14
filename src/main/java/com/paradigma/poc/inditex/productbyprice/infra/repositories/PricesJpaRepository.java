package com.paradigma.poc.inditex.productbyprice.infra.repositories;

import com.paradigma.poc.inditex.productbyprice.infra.model.PricesIdsJpa;
import com.paradigma.poc.inditex.productbyprice.infra.model.ProductPriceJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PricesJpaRepository extends JpaRepository<ProductPriceJpa, PricesIdsJpa> {

    List<ProductPriceJpa> findByProductIdAndBrandId(Long productId, Integer brandId);

}
