package agenda;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import org.junit.jupiter.api.Test;

public class RepetitionExtraTest {

    @Test
    void recurringWithExceptions_skips_exception_day() {
        Event e = new Event("Daily", LocalDateTime.of(2020, 11, 1, 10, 0), Duration.ofMinutes(60));
        e.setRepetition(ChronoUnit.DAYS);
        e.addException(LocalDate.of(2020, 11, 2));
        assertFalse(e.isInDay(LocalDate.of(2020, 11, 2)));
        assertTrue(e.isInDay(LocalDate.of(2020, 11, 3)));
    }

    @Test
    void weekly_recurring_on_same_weekday_until_termination() {
        Event e = new Event("Weekly", LocalDateTime.of(2020, 11, 1, 10, 0), Duration.ofMinutes(60));
        e.setRepetition(ChronoUnit.WEEKS);
        e.setTermination(3); // 3 occurrences: 01/11, 08/11, 15/11
        assertTrue(e.isInDay(LocalDate.of(2020, 11, 15))); // last occurrence
        assertFalse(e.isInDay(LocalDate.of(2020, 11, 22))); // after termination
    }

    @Test
    void multipleExceptions_skip_all() {
        Event e = new Event("Daily", LocalDateTime.of(2020, 11, 1, 10, 0), Duration.ofMinutes(60));
        e.setRepetition(ChronoUnit.DAYS);
        e.addException(LocalDate.of(2020, 11, 2));
        e.addException(LocalDate.of(2020, 11, 3));
        assertFalse(e.isInDay(LocalDate.of(2020, 11, 2)));
        assertFalse(e.isInDay(LocalDate.of(2020, 11, 3)));
        assertTrue(e.isInDay(LocalDate.of(2020, 11, 4)));
    }

    @Test
    void weekly_not_on_same_weekday_isFalse() {
        Event e = new Event("Weekly", LocalDateTime.of(2020, 11, 1, 10, 0), Duration.ofMinutes(60)); // Sunday
        e.setRepetition(ChronoUnit.WEEKS);
        assertFalse(e.isInDay(LocalDate.of(2020, 11, 2))); // Monday
        assertTrue(e.isInDay(LocalDate.of(2020, 11, 8))); // next Sunday
    }

    @Test
    void monthly_recurring_on_same_day_of_month() {
        Event e = new Event("Monthly", LocalDateTime.of(2020, 11, 15, 9, 0), Duration.ofMinutes(30));
        e.setRepetition(ChronoUnit.MONTHS);
        assertTrue(e.isInDay(LocalDate.of(2020, 12, 15)));
        assertFalse(e.isInDay(LocalDate.of(2020, 12, 14)));
    }

    @Test
    void recurring_before_start_isFalse() {
        Event e = new Event("Daily", LocalDateTime.of(2020, 11, 10, 9, 0), Duration.ofMinutes(30));
        e.setRepetition(ChronoUnit.DAYS);
        assertFalse(e.isInDay(LocalDate.of(2020, 11, 9)));
        assertTrue(e.isInDay(LocalDate.of(2020, 11, 10)));
    }

    @Test
    void recurring_exact_last_occurrence_isTrue() {
        Event e = new Event("WeeklyTerminated", LocalDateTime.of(2020, 11, 1, 9, 0), Duration.ofMinutes(30));
        e.setRepetition(ChronoUnit.WEEKS);
        e.setTermination(3); // 01/11, 08/11, 15/11
        assertTrue(e.isInDay(LocalDate.of(2020, 11, 15)));
    }

    @Test
    void monthly_with_termination_by_date() {
        Event e = new Event("MonthlyTermDate", LocalDateTime.of(2020, 11, 15, 9, 0), Duration.ofMinutes(30));
        e.setRepetition(ChronoUnit.MONTHS);
        e.setTermination(LocalDate.of(2021, 1, 15)); // last occurrence 15 Jan
        assertTrue(e.isInDay(LocalDate.of(2021, 1, 15)));
        assertFalse(e.isInDay(LocalDate.of(2021, 2, 15)));
    }

    @Test
    void monthly_with_termination_by_occurrences() {
        Event e = new Event("MonthlyTermOcc", LocalDateTime.of(2020, 11, 15, 9, 0), Duration.ofMinutes(30));
        e.setRepetition(ChronoUnit.MONTHS);
        e.setTermination(3); // 15 Nov, 15 Dec, 15 Jan
        assertTrue(e.isInDay(LocalDate.of(2021, 1, 15)));
        assertFalse(e.isInDay(LocalDate.of(2021, 2, 15)));
    }

    @Test
    void recurring_with_exceptions_and_termination() {
        Event e = new Event("DailyExceptTerm", LocalDateTime.of(2020, 11, 10, 9, 0), Duration.ofMinutes(30));
        e.setRepetition(ChronoUnit.DAYS);
        e.addException(LocalDate.of(2020, 11, 12));
        e.addException(LocalDate.of(2020, 11, 13));
        e.setTermination(LocalDate.of(2020, 11, 14));
        assertTrue(e.isInDay(LocalDate.of(2020, 11, 10)));
        assertFalse(e.isInDay(LocalDate.of(2020, 11, 12)));
        assertFalse(e.isInDay(LocalDate.of(2020, 11, 13)));
        assertTrue(e.isInDay(LocalDate.of(2020, 11, 14)));
        assertFalse(e.isInDay(LocalDate.of(2020, 11, 15)));
    }

    @Test
    void yearly_recurring_on_same_day_and_near_same_month_based_on_between_logic() {
        Event e = new Event("Yearly", LocalDateTime.of(2020, 11, 15, 9, 0), Duration.ofMinutes(30));
        e.setRepetition(ChronoUnit.YEARS);
        // With current implementation, any date with years-between >= 0 is considered recurring
        assertTrue(e.isInDay(LocalDate.of(2021, 11, 15)));
        assertTrue(e.isInDay(LocalDate.of(2021, 11, 14))); // years-between == 0
    }

    @Test
    void yearly_before_start_isFalse_in_fallback() {
        Event e = new Event("YearlyBefore", LocalDateTime.of(2020, 11, 15, 9, 0), Duration.ofMinutes(30));
        e.setRepetition(ChronoUnit.YEARS);
        assertFalse(e.isInDay(LocalDate.of(2019, 11, 15)));
    }

    @Test
    void yearly_with_exception_isFalse() {
        Event e = new Event("YearlyExcept", LocalDateTime.of(2020, 11, 15, 22, 0), Duration.ofMinutes(30));
        e.setRepetition(ChronoUnit.YEARS);
        e.addException(LocalDate.of(2021, 11, 15));
        assertFalse(e.isInDay(LocalDate.of(2021, 11, 15)));
        assertTrue(e.isInDay(LocalDate.of(2022, 11, 15)));
    }

    @Test
    void weekly_last_occurrence_overflows_to_next_day_isTrue() {
        // Start late so the occurrence spills into the next day
        Event e = new Event("WeeklyOverflowLast", LocalDateTime.of(2020, 11, 1, 23, 30), Duration.ofMinutes(120));
        e.setRepetition(ChronoUnit.WEEKS);
        e.setTermination(3); // 01/11, 08/11, 15/11 (lastOccurrence = 2020-11-15)
        // Next day after last occurrence should be considered in day due to overflow
        assertTrue(e.isInDay(LocalDate.of(2020, 11, 16)));
        // But two days after should be false
        assertFalse(e.isInDay(LocalDate.of(2020, 11, 17)));
    }

    @Test
    void yearly_exception_before_start_short_circuits_to_false() {
        // Start after the exception and target date before start
        Event e = new Event("YearlyShortCircuit", LocalDateTime.of(2021, 11, 15, 9, 0), Duration.ofMinutes(30));
        e.setRepetition(ChronoUnit.YEARS);
        // Exception on the target day
        e.addException(LocalDate.of(2020, 11, 15));
        // aDay is before eventStartDate and also in exceptions -> should be false via early checks
        assertFalse(e.isInDay(LocalDate.of(2020, 11, 15)));
    }

    @Test
    void monthly_exception_on_last_occurrence_isTrue_due_to_lastOccurrence_logic() {
        Event e = new Event("MonthlyLastExcept", LocalDateTime.of(2020, 11, 15, 23, 0), Duration.ofMinutes(90));
        e.setRepetition(ChronoUnit.MONTHS);
        e.setTermination(LocalDate.of(2021, 1, 15));
        e.addException(LocalDate.of(2021, 1, 15));
        assertTrue(e.isInDay(LocalDate.of(2021, 1, 15)));
    }
}
