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
}
