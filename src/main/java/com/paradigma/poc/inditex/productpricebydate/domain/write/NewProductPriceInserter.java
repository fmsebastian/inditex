package com.paradigma.poc.inditex.productpricebydate.domain.write;

import com.paradigma.poc.inditex.productpricebydate.domain.exceptions.SamePriorityOverlapException;
import com.paradigma.poc.inditex.productpricebydate.domain.model.DateInterval;
import com.paradigma.poc.inditex.productpricebydate.domain.model.ProductPriceBetweenDates;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NewProductPriceInserter {

    private final ProductPriceIntervalSplitter productPriceIntervalSplitter;

    public Set<ProductPriceBetweenDates> insertNewProduct(
            ProductPriceBetweenDates newProductPrice, Set<ProductPriceBetweenDates> originalProducts) {

        Set<ProductPriceBetweenDates> splitProducts = new HashSet<>();

        Map<Integer, Set<ProductPriceBetweenDates>> originalProductsSplitByPriority = originalProducts.stream()
                .collect(Collectors.groupingBy(
                        product -> product.getPriority().compareTo(newProductPrice.getPriority()),
                        Collectors.toSet()));

        checkNewPriceDoesNotOverlap(newProductPrice,
                originalProductsSplitByPriority.getOrDefault(0, Collections.emptySet()));

        Set<ProductPriceBetweenDates> lowerPrioritySplitByNew = splitLowerPriorityByNew(
                newProductPrice, originalProductsSplitByPriority.getOrDefault(-1, Collections.emptySet()));

        Set<ProductPriceBetweenDates> newSplitByHigherPriority = splitNewByHigherPriority(
                newProductPrice, originalProductsSplitByPriority.getOrDefault(1, Collections.emptySet()));

        splitProducts.addAll(lowerPrioritySplitByNew);
        splitProducts.addAll(newSplitByHigherPriority);
        splitProducts.addAll(originalProductsSplitByPriority.getOrDefault(0, Collections.emptySet()));
        splitProducts.addAll(originalProductsSplitByPriority.getOrDefault(1, Collections.emptySet()));

        return splitProducts;
    }

    private void checkNewPriceDoesNotOverlap(ProductPriceBetweenDates newProductPrice, Set<ProductPriceBetweenDates> originalProductsSamePriority) {
        originalProductsSamePriority.stream()
                .filter(product -> doesProductDatesOverlapNewPriceDates(newProductPrice, product))
                .findAny()
                .ifPresent(p -> {
                    throw new SamePriorityOverlapException(String.format(
                            "There is already a price with same priority that overlaps the new one " +
                                    "with application dates %s - %s", p.getOriginalStartDate(), p.getOriginalEndDate()));
                });
    }

    private boolean doesProductDatesOverlapNewPriceDates(
            ProductPriceBetweenDates newProductPrice, ProductPriceBetweenDates product) {

        return product.getStartDate().isBefore(newProductPrice.getEndDate())
                && product.getEndDate().isAfter(newProductPrice.getStartDate());
    }

    private Set<ProductPriceBetweenDates> splitLowerPriorityByNew(ProductPriceBetweenDates newProductPrice, Set<ProductPriceBetweenDates> originalProducts) {

        Function<ProductPriceBetweenDates, Set<ProductPriceBetweenDates>> newProductSplitter =
                splitProductByOtherProductDatesFunction(newProductPrice);

        Set<ProductPriceBetweenDates> lowerPrioritySet = originalProducts.stream()
                .filter(originalProduct -> originalProduct.getPriority() < newProductPrice.getPriority())
                .collect(Collectors.toSet());

        return applyFunctionToSet(newProductSplitter, lowerPrioritySet);
    }

    private Set<ProductPriceBetweenDates> splitNewByHigherPriority(
            ProductPriceBetweenDates newProductPrice, Set<ProductPriceBetweenDates> originalProducts) {

        Function<ProductPriceBetweenDates, Set<ProductPriceBetweenDates>> splitProductByHigherPriorityRecursively =
                originalProducts.stream()
                        .filter(originalProduct -> originalProduct.getPriority() > newProductPrice.getPriority())
                        .map(this::splitProductByOtherProductDatesFunction)
                        .reduce(Set::of,
                                (f1, f2) -> f1.andThen(set -> applyFunctionToSet(f2, set)));

        return splitProductByHigherPriorityRecursively.apply(newProductPrice);
    }

    private Set<ProductPriceBetweenDates> applyFunctionToSet(
            Function<ProductPriceBetweenDates, Set<ProductPriceBetweenDates>> f, Set<ProductPriceBetweenDates> set) {

        return set.stream()
                .map(f)
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
    }

    private Function<ProductPriceBetweenDates, Set<ProductPriceBetweenDates>> splitProductByOtherProductDatesFunction(
            ProductPriceBetweenDates productSplitter) {

        DateInterval splitterDateInterval = DateInterval.builder()
                .startDate(productSplitter.getStartDate())
                .endDate(productSplitter.getEndDate())
                .build();

        return (p) -> productPriceIntervalSplitter.splitByDate(p, splitterDateInterval);
    }
}
