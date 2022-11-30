package com.paradigma.poc.inditex.productbyprice.web;

import com.paradigma.poc.inditex.productbyprice.application.ProductPriceByDateCalculator;
import com.paradigma.poc.inditex.productbyprice.domain.PriceByDateRequest;
import com.paradigma.poc.inditex.productbyprice.domain.ProductPriceBetweenDates;
import com.paradigma.poc.inditex.productbyprice.web.resources.ProductPriceForDateResponse;
import com.paradigma.poc.inditex.productbyprice.web.resources.ProductPriceForDateResponseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/productprices")
@RequiredArgsConstructor
public class ProductPriceByDateController {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    private final ProductPriceByDateCalculator productPriceByDateCalculator;
    private final ProductPriceForDateResponseMapper productPriceForDateResponseMapper;

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductPriceForDateResponse> getProductPriceForDate(
            @RequestParam Optional<Integer> brandId,
            @RequestParam Optional<Integer> productId,
            @RequestParam Optional<String> applicationDate) {

        validateParams(brandId, productId, applicationDate);

        PriceByDateRequest priceRequest = PriceByDateRequest.builder()
                .date(LocalDateTime.parse(applicationDate.get(), FORMATTER))
                .brandId(brandId.get())
                .productId(productId.get())
                .build();

        Optional<ProductPriceBetweenDates> productPriceBetweenDates =
                productPriceByDateCalculator.calculatePriceByDate(priceRequest);

        // TODO: improve by using ControlAdvice and ExceptionHandler
        if (productPriceBetweenDates.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Price not found for date %s", applicationDate.get()));
        }

        return ResponseEntity.of(
                productPriceBetweenDates.map(productPriceForDateResponseMapper::fromProductBetweenDates));
    }

    private void validateParams(
            Optional<Integer> brandId, Optional<Integer> productId, Optional<String> applicationDate) {
        // TODO: validations (existence and format)
    }

}
