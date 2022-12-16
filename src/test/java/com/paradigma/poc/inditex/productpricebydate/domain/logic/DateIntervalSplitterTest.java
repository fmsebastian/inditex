package com.paradigma.poc.inditex.productpricebydate.domain.logic;

import com.paradigma.poc.inditex.productpricebydate.domain.model.DateInterval;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.Period;
import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DateIntervalSplitterTest {

    public static final int DAYS_BETWEEN_INITIAL_DATES = 10;
    public static final Period PERIOD_BETWEEN_INITIAL_DATES = Period.ofDays(DAYS_BETWEEN_INITIAL_DATES);
    public static final LocalDateTime INITIAL_START_DATE = LocalDateTime.parse("2022-10-10T00:00:00");
    public static final LocalDateTime INITIAL_END_DATE = INITIAL_START_DATE.plus(PERIOD_BETWEEN_INITIAL_DATES);

    public static final DateInterval INITIAL_INTERVAL = DateInterval.builder()
            .startDate(INITIAL_START_DATE)
            .endDate(INITIAL_END_DATE)
            .build();

    @InjectMocks
    private DateIntervalSplitter dateIntervalSplitter;

    @Test
    public void shouldReturnInitialDateWhenIntervalToRemoveIsSuperiorAndNoOverlap() {

        // Given
        DateInterval intervalSuperiorToInitial = DateInterval.builder()
                .startDate(INITIAL_START_DATE.plus(PERIOD_BETWEEN_INITIAL_DATES.multipliedBy(2)))
                .endDate(INITIAL_END_DATE.plus(PERIOD_BETWEEN_INITIAL_DATES.multipliedBy(2)))
                .build();

        // When
        Set<DateInterval> dateIntervals =
                dateIntervalSplitter.removeInterval(INITIAL_INTERVAL, intervalSuperiorToInitial);

        // Then
        assertDateIntervalsContainsExpected(dateIntervals, Set.of(INITIAL_INTERVAL));
    }

    @Test
    public void shouldReturnInitialIntervalWhenIntervalToRemoveIsInferiorAndNoOverlap() {

        // Given
        DateInterval intervalInferiorToInitial = DateInterval.builder()
                .startDate(INITIAL_START_DATE.minus(PERIOD_BETWEEN_INITIAL_DATES.multipliedBy(2)))
                .endDate(INITIAL_END_DATE.minus(PERIOD_BETWEEN_INITIAL_DATES.multipliedBy(2)))
                .build();

        // When
        Set<DateInterval> dateIntervals =
                dateIntervalSplitter.removeInterval(INITIAL_INTERVAL, intervalInferiorToInitial);

        // Then
        assertDateIntervalsContainsExpected(dateIntervals, Set.of(INITIAL_INTERVAL));
    }

    @Test
    public void shouldReturnInitialTruncateByUpperLimit() {

        Period periodToShiftDate = Period.ofDays(DAYS_BETWEEN_INITIAL_DATES / 2);
        DateInterval intervalShiftedToUpperRange = DateInterval.builder()
                .startDate(INITIAL_START_DATE.plus(periodToShiftDate))
                .endDate(INITIAL_END_DATE.plus(periodToShiftDate))
                .build();

        Set<DateInterval> dateIntervals =
                dateIntervalSplitter.removeInterval(INITIAL_INTERVAL, intervalShiftedToUpperRange);

        DateInterval expectedInterval = DateInterval.builder()
                .startDate(INITIAL_START_DATE)
                .endDate(INITIAL_START_DATE.plus(periodToShiftDate))
                .build();

        assertDateIntervalsContainsExpected(dateIntervals, Set.of(expectedInterval));
    }

    @Test
    public void shouldReturnInitialTruncateByLowerLimit() {

        Period periodToShiftDate = Period.ofDays(DAYS_BETWEEN_INITIAL_DATES / 2);
        DateInterval intervalShiftedToLowerRange = DateInterval.builder()
                .startDate(INITIAL_START_DATE.minus(periodToShiftDate))
                .endDate(INITIAL_END_DATE.minus(periodToShiftDate))
                .build();

        Set<DateInterval> dateIntervals =
                dateIntervalSplitter.removeInterval(INITIAL_INTERVAL, intervalShiftedToLowerRange);

        DateInterval expectedInterval = DateInterval.builder()
                .startDate(INITIAL_END_DATE.minus(periodToShiftDate))
                .endDate(INITIAL_END_DATE)
                .build();

        assertDateIntervalsContainsExpected(dateIntervals, Set.of(expectedInterval));
    }

    @Test
    public void shouldReturnEmptyWhenOverlapCompletely() {

        Period periodToShiftDate = Period.ofDays(DAYS_BETWEEN_INITIAL_DATES / 2);
        DateInterval intervalOverlap = DateInterval.builder()
                .startDate(INITIAL_START_DATE.minus(periodToShiftDate))
                .endDate(INITIAL_END_DATE.plus(periodToShiftDate))
                .build();

        Set<DateInterval> dateIntervals =
                dateIntervalSplitter.removeInterval(INITIAL_INTERVAL, intervalOverlap);

        assertDateIntervalsContainsExpected(dateIntervals, Collections.emptySet());
    }

    @Test
    public void shouldReturnTwoIntervalsWhenOverlapingInTheMiddle() {

        Period periodToShiftDate = Period.ofDays(DAYS_BETWEEN_INITIAL_DATES / 3);
        DateInterval intervalShiftedToLowerRange = DateInterval.builder()
                .startDate(INITIAL_START_DATE.plus(periodToShiftDate))
                .endDate(INITIAL_END_DATE.minus(periodToShiftDate))
                .build();

        Set<DateInterval> dateIntervals =
                dateIntervalSplitter.removeInterval(INITIAL_INTERVAL, intervalShiftedToLowerRange);


        DateInterval expectedLowerInterval = DateInterval.builder()
                .startDate(INITIAL_START_DATE)
                .endDate(INITIAL_START_DATE.plus(periodToShiftDate))
                .build();

        DateInterval expectedUpperInterval = DateInterval.builder()
                .startDate(INITIAL_END_DATE.minus(periodToShiftDate))
                .endDate(INITIAL_END_DATE)
                .build();

        assertDateIntervalsContainsExpected(dateIntervals, Set.of(expectedLowerInterval, expectedUpperInterval));
    }

    private void assertDateIntervalsContainsExpected(Set<DateInterval> dateIntervals, Set<DateInterval> expectedIntervals) {
        assertNotNull(dateIntervals);
        assertEquals(expectedIntervals.size(), dateIntervals.size());
        expectedIntervals.forEach(
                expectedInterval -> assertTrue(dateIntervals.contains(expectedInterval)));
    }

}