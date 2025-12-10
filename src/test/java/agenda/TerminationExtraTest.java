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
}
