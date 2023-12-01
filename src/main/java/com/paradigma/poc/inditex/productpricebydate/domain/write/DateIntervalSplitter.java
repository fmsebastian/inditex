package com.paradigma.poc.inditex.productpricebydate.domain.write;

import com.paradigma.poc.inditex.productpricebydate.domain.model.DateInterval;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class DateIntervalSplitter {

    public Set<DateInterval> removeInterval(DateInterval initialInterval, DateInterval intervalToRemove) {

        if (intervalToRemove.getEndDate().isBefore(initialInterval.getStartDate())
                || intervalToRemove.getStartDate().isAfter(initialInterval.getEndDate())) {

            return Set.of(initialInterval);
        }

        HashSet<DateInterval> splitProducts = new HashSet<>();

        if (initialInterval.getStartDate().isBefore(intervalToRemove.getStartDate())) {

            splitProducts.add(DateInterval.builder()
                    .startDate(initialInterval.getStartDate())
                    .endDate(intervalToRemove.getStartDate())
                    .build());
        }

        if (initialInterval.getEndDate().isAfter(intervalToRemove.getEndDate())) {

            splitProducts.add(DateInterval.builder()
                    .startDate(intervalToRemove.getEndDate())
                    .endDate(initialInterval.getEndDate())
                    .build());
        }

        return splitProducts;
    }
}
