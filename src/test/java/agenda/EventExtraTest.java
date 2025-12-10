package agenda;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import org.junit.jupiter.api.Test;

public class EventExtraTest {

    @Test
    void isInDay_overflow_to_next_day() {
        Event e = new Event("Late", LocalDateTime.of(2020, 11, 1, 23, 30), Duration.ofMinutes(120));
        assertTrue(e.isInDay(LocalDate.of(2020, 11, 2)));
        assertTrue(e.isInDay(LocalDate.of(2020, 11, 1)));
    }

    @Test
    void isInDay_simple_day_range() {
        Event e = new Event("Simple", LocalDateTime.of(2020, 11, 1, 10, 0), Duration.ofMinutes(120));
        assertTrue(e.isInDay(LocalDate.of(2020, 11, 1)));
        assertFalse(e.isInDay(LocalDate.of(2020, 11, 2)));
    }

    @Test
    void isInDay_overflow_to_previous_day() {
        Event e = new Event("Night", LocalDateTime.of(2020, 11, 2, 0, 30), Duration.ofMinutes(120));
        // Cet événement couvre 00:30 -> 02:30 le 2 novembre, il ne se produit pas le 1er novembre
        assertFalse(e.isInDay(LocalDate.of(2020, 11, 1)));
        assertTrue(e.isInDay(LocalDate.of(2020, 11, 2)));
    }

    @Test
    void isInDay_with_exception_on_repetition() {
        Event e = new Event("Daily", LocalDateTime.of(2020, 11, 1, 10, 0), Duration.ofMinutes(60));
        e.setRepetition(ChronoUnit.DAYS);
        e.addException(LocalDate.of(2020, 11, 5));
        assertTrue(e.isInDay(LocalDate.of(2020, 11, 4)));
        assertFalse(e.isInDay(LocalDate.of(2020, 11, 5)));
        assertTrue(e.isInDay(LocalDate.of(2020, 11, 6)));
    }

    @Test
    void setTermination_without_repetition_throws() {
        Event e = new Event("NoRep", LocalDateTime.of(2020, 11, 1, 10, 0), Duration.ofMinutes(30));
        assertThrows(IllegalStateException.class, () -> e.setTermination(3));
        assertThrows(IllegalStateException.class, () -> e.setTermination(LocalDate.of(2020, 11, 10)));
    }

    @Test
    void getters_when_no_repetition_return_defaults() {
        Event e = new Event("NoRep", LocalDateTime.of(2020, 11, 1, 10, 0), Duration.ofMinutes(30));
        assertNull(e.getTerminationDate());
        assertEquals(0, e.getNumberOfOccurrences());
    }

    @Test
    void addException_without_repetition_has_no_effect() {
        Event e = new Event("NoRep", LocalDateTime.of(2020, 11, 1, 10, 0), Duration.ofMinutes(30));
        e.addException(LocalDate.of(2020, 11, 2)); // should not throw
        assertTrue(e.isInDay(LocalDate.of(2020, 11, 1)));
    }

    @Test
    void toString_covers_method() {
        Event e = new Event("Str", LocalDateTime.of(2020, 11, 1, 10, 0), Duration.ofMinutes(30));
        String s = e.toString();
        assertTrue(s.contains("Event{"));
        assertTrue(s.contains("Str"));
    }
}
