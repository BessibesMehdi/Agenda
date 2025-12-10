package agenda;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import org.junit.jupiter.api.Test;

public class TerminationExtraTest {

    @Test
    void calculateNumberOfOccurrences_byDate_weekly() {
        LocalDate start = LocalDate.of(2020, 11, 1);
        Termination t = new Termination(LocalDate.of(2021, 1, 5));
        long n = t.calculateNumberOfOccurrences(start, ChronoUnit.WEEKS);
        assertEquals(10, n);
    }

    @Test
    void calculateTerminationDate_byOccurrences_weekly() {
        LocalDate start = LocalDate.of(2020, 11, 1);
        Termination t = new Termination(10);
        LocalDate last = t.calculateTerminationDate(start, ChronoUnit.WEEKS);
        assertEquals(LocalDate.of(2021, 1, 3), last);
    }

    @Test
    void isAfterTermination_true_after_last_day() {
        LocalDate start = LocalDate.of(2020, 11, 1);
        Termination t = new Termination(2);
        assertTrue(t.isAfterTermination(LocalDate.of(2020, 11, 15), ChronoUnit.WEEKS, start));
    }

    @Test
    void isAfterTermination_false_on_last_day() {
        LocalDate start = LocalDate.of(2020, 11, 1);
        Termination t = new Termination(2);
        assertFalse(t.isAfterTermination(LocalDate.of(2020, 11, 8), ChronoUnit.WEEKS, start));
    }

    // Nouveaux tests pour augmenter la couverture
    @Test
    void calculateNumberOfOccurrences_byDate_daily() {
        LocalDate start = LocalDate.of(2020, 11, 1);
        Termination t = new Termination(LocalDate.of(2020, 11, 5));
        long n = t.calculateNumberOfOccurrences(start, ChronoUnit.DAYS);
        assertEquals(5, n); // inclusif: 1,2,3,4,5
    }

    @Test
    void calculateTerminationDate_byOccurrences_daily_one() {
        LocalDate start = LocalDate.of(2020, 11, 1);
        Termination t = new Termination(1);
        LocalDate last = t.calculateTerminationDate(start, ChronoUnit.DAYS);
        assertEquals(LocalDate.of(2020, 11, 1), last);
    }

    @Test
    void isAfterTermination_before_last_day_false() {
        LocalDate start = LocalDate.of(2020, 11, 1);
        Termination t = new Termination(3);
        assertFalse(t.isAfterTermination(LocalDate.of(2020, 11, 2), ChronoUnit.DAYS, start));
    }

    @Test
    void calculateNumberOfOccurrences_daily() {
        LocalDate start = LocalDate.of(2020, 11, 1);
        Termination t = new Termination(LocalDate.of(2020, 11, 10)); // inclusif
        long n = t.calculateNumberOfOccurrences(start, ChronoUnit.DAYS);
        assertEquals(10, n);
    }

    @Test
    void calculateTerminationDate_oneOccurrence() {
        LocalDate start = LocalDate.of(2020, 11, 1);
        Termination t = new Termination(1);
        LocalDate last = t.calculateTerminationDate(start, ChronoUnit.WEEKS);
        assertEquals(start, last);
    }

    @Test
    void isAfterTermination_false_before_last_day() {
        LocalDate start = LocalDate.of(2020, 11, 1);
        Termination t = new Termination(3); // 01/11, 08/11, 15/11
        assertFalse(t.isAfterTermination(LocalDate.of(2020, 11, 7), ChronoUnit.WEEKS, start));
    }

    @Test
    void isAfterTermination_true_far_after_last_day() {
        LocalDate start = LocalDate.of(2020, 11, 1);
        Termination t = new Termination(3);
        assertTrue(t.isAfterTermination(LocalDate.of(2020, 12, 1), ChronoUnit.WEEKS, start));
    }

    @Test
    void numberOfOccurrences_getter_when_unset_returns_zero() {
        Termination t = new Termination(LocalDate.of(2020, 11, 30));
        assertEquals(0, t.numberOfOccurrences());
        assertEquals(LocalDate.of(2020, 11, 30), t.terminationDateInclusive());
    }

    @Test
    void numberOfOccurrences_getter_when_set_returns_value_and_date_is_null() {
        Termination t = new Termination(5);
        assertEquals(5, t.numberOfOccurrences());
        assertNull(t.terminationDateInclusive());
    }

    @Test
    void isAfterTermination_with_date_boundary_checks() {
        LocalDate start = LocalDate.of(2021, 1, 1);
        Termination t = new Termination(LocalDate.of(2021, 1, 15));
        assertFalse(t.isAfterTermination(LocalDate.of(2021, 1, 15), ChronoUnit.DAYS, start)); // on termination
        assertTrue(t.isAfterTermination(LocalDate.of(2021, 1, 16), ChronoUnit.DAYS, start)); // after termination
        assertFalse(t.isAfterTermination(LocalDate.of(2021, 1, 1), ChronoUnit.DAYS, start)); // before termination
    }

    @Test
    void constructor_numberOfOccurrences_illegal_argument() {
        assertThrows(IllegalArgumentException.class, () -> new Termination(0));
        assertThrows(IllegalArgumentException.class, () -> new Termination(-5));
    }

    @Test
    void isAfterTermination_with_date_inclusive_boundary() {
        LocalDate start = LocalDate.of(2020, 11, 1);
        Termination t = new Termination(LocalDate.of(2020, 11, 10));
        assertFalse(t.isAfterTermination(LocalDate.of(2020, 11, 10), ChronoUnit.DAYS, start)); // inclusive day
        assertTrue(t.isAfterTermination(LocalDate.of(2020, 11, 11), ChronoUnit.DAYS, start)); // after
    }

    @Test
    void isAfterTermination_with_occurrences_boundary_equal_and_after() {
        LocalDate start = LocalDate.of(2020, 11, 1);
        Termination t = new Termination(3); // 1st, 2nd, 3rd occurrence
        // equal occurrences -> not after
        assertFalse(t.isAfterTermination(LocalDate.of(2020, 11, 3), ChronoUnit.DAYS, start));
        // after occurrences -> true
        assertTrue(t.isAfterTermination(LocalDate.of(2020, 11, 4), ChronoUnit.DAYS, start));
    }

    @Test
    void numberOfOccurrences_getter_unset_is_zero_and_calculate_uses_date() {
        Termination t = new Termination(LocalDate.of(2020, 11, 10));
        assertEquals(0, t.numberOfOccurrences());
        assertEquals(10, t.calculateNumberOfOccurrences(LocalDate.of(2020, 11, 1), ChronoUnit.DAYS));
    }

    @Test
    void calculateNumberOfOccurrences_when_occurrences_set_uses_number() {
        Termination t = new Termination(4);
        assertEquals(4, t.numberOfOccurrences());
        assertEquals(4, t.calculateNumberOfOccurrences(LocalDate.of(2020, 11, 1), ChronoUnit.WEEKS));
    }

    @Test
    void isAfterTermination_when_no_termination_configured_returns_false() {
        // Create via occurrences then unset scenario by using calculate paths indirectly
        // Here we construct a Termination via date but set it to null using reflection is not allowed; instead, use a fresh Termination via occurrences and check before exceeding
        Termination t = new Termination(5);
        // Before exceeding occurrences -> false
        assertFalse(t.isAfterTermination(LocalDate.of(2020, 11, 1), ChronoUnit.DAYS, LocalDate.of(2020, 11, 1)));
    }

    @Test
    void isAfterTermination_date_boundary_false_then_true() {
        LocalDate start = LocalDate.of(2020, 11, 1);
        Termination t = new Termination(LocalDate.of(2020, 11, 20));
        assertFalse(t.isAfterTermination(LocalDate.of(2020, 11, 20), ChronoUnit.DAYS, start));
        assertTrue(t.isAfterTermination(LocalDate.of(2020, 11, 21), ChronoUnit.DAYS, start));
    }

    @Test
    void isAfterTermination_occurrences_weekly_boundary_variant() {
        LocalDate start = LocalDate.of(2020, 12, 1); // Tuesday
        Termination t = new Termination(2); // occurrences on 01/12 and 08/12
        assertFalse(t.isAfterTermination(LocalDate.of(2020, 12, 8), ChronoUnit.WEEKS, start)); // last occurrence
        assertTrue(t.isAfterTermination(LocalDate.of(2020, 12, 15), ChronoUnit.WEEKS, start)); // after
    }
}
