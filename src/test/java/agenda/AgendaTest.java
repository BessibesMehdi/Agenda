package agenda;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AgendaTest {
    Agenda agenda;

    // November 1st, 2020
    LocalDate nov_1_2020 = LocalDate.of(2020, 11, 1);

    // January 5, 2021
    LocalDate jan_5_2021 = LocalDate.of(2021, 1, 5);

    // November 1st, 2020, 22:30
    LocalDateTime nov_1_2020_22_30 = LocalDateTime.of(2020, 11, 1, 22, 30);

    // 120 minutes
    Duration min_120 = Duration.ofMinutes(120);

    // Un événement simple
    // November 1st, 2020, 22:30, 120 minutes
    Event simple;

    // Un événement qui se répète toutes les semaines et se termine à une date
    // donnée
    Event fixedTermination;

    // Un événement qui se répète toutes les semaines et se termine après un nombre
    // donné d'occurrences
    Event fixedRepetitions;

    // A daily repetitive event, never ending
    // Un événement répétitif quotidien, sans fin
    // November 1st, 2020, 22:30, 120 minutes
    Event neverEnding;

    @BeforeEach
    public void setUp() {
        simple = new Event("Simple event", nov_1_2020_22_30, min_120);

        fixedTermination = new Event("Fixed termination weekly", nov_1_2020_22_30, min_120);
        fixedTermination.setRepetition(ChronoUnit.WEEKS);
        fixedTermination.setTermination(jan_5_2021);

        fixedRepetitions = new Event("Fixed termination weekly", nov_1_2020_22_30, min_120);
        fixedRepetitions.setRepetition(ChronoUnit.WEEKS);
        fixedRepetitions.setTermination(10);

        neverEnding = new Event("Never Ending", nov_1_2020_22_30, min_120);
        neverEnding.setRepetition(ChronoUnit.DAYS);

        agenda = new Agenda();
        agenda.addEvent(simple);
        agenda.addEvent(fixedTermination);
        agenda.addEvent(fixedRepetitions);
        agenda.addEvent(neverEnding);
    }

    @Test
    public void testMultipleEventsInDay() {
        assertEquals(4, agenda.eventsInDay(nov_1_2020).size(),
                "Il y a 4 événements ce jour là");
        assertTrue(agenda.eventsInDay(nov_1_2020).contains(neverEnding));
        
    }

    
//implementations demandée dans les questions complémentaires
    @Test
    public void testFindByTitle() {
        Agenda agenda = new Agenda();
        Event e1 = new Event("Réunion", LocalDateTime.of(2025, 12, 10, 10, 0), Duration.ofMinutes(60));
        Event e2 = new Event("Réunion", LocalDateTime.of(2025, 12, 11, 14, 0), Duration.ofMinutes(30));
        Event e3 = new Event("Déjeuner", LocalDateTime.of(2025, 12, 10, 12, 0), Duration.ofMinutes(45));
        agenda.addEvent(e1);
        agenda.addEvent(e2);
        agenda.addEvent(e3);

        List<Event> result = agenda.findByTitle("Réunion");
        assertEquals(2, result.size());
        assertTrue(result.contains(e1));
        assertTrue(result.contains(e2));
        assertFalse(result.contains(e3));
    }

    @Test
    public void testIsFreeFor() {
        Agenda agenda = new Agenda();
        Event e1 = new Event("Réunion", LocalDateTime.of(2025, 12, 10, 10, 0), Duration.ofMinutes(60));
        Event e2 = new Event("Déjeuner", LocalDateTime.of(2025, 12, 10, 12, 0), Duration.ofMinutes(45));
        agenda.addEvent(e1);
        agenda.addEvent(e2);

        // Chevauchement : 10h30–11h30 (e1 occupe 10h–11h)
        Event conflict = new Event("Appel", LocalDateTime.of(2025, 12, 10, 10, 30), Duration.ofMinutes(60));
        assertFalse(agenda.isFreeFor(conflict));

        // Pas de chevauchement : 11h30–12h
        Event free = new Event("Pause", LocalDateTime.of(2025, 12, 10, 11, 30), Duration.ofMinutes(30));
        assertTrue(agenda.isFreeFor(free));
    }

}
