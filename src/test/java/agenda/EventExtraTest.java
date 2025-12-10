package agenda;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
}
