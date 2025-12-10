package agenda;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class Event {

    // Rôles UML : +title, +start, +duration
    private String title;
    private LocalDateTime start;
    private Duration duration;

    // Rôle UML : Représente l'association de composition avec Repetition (0..1)
    private Repetition repetition;

    /**
     * «constructor»+Event(t: String, st: LocalDateTime, d: Duration)
     */
    public Event(String title, LocalDateTime start, Duration duration) {
        this.title = title;
        this.start = start;
        this.duration = duration;
    }

    /**
     * Opération UML : +setRepetition(frequency : ChronoUnit)
     */
    public void setRepetition(ChronoUnit frequency) {
        // Crée Repetition en lui passant la date de début de l'Event (pour la logique)
        this.repetition = new Repetition(frequency, this.start.toLocalDate());
    }

    /**
     * Opération UML : +addException(day: LocalDate)
     */
    public void addException(LocalDate date) {
        if (repetition != null) {
            repetition.addException(date);
        }
    }

    /**
     * Opération UML : +setTermination(dateInclusive : LocalDate)
     * Crée un objet Termination avec le constructeur simple.
     */
    public void setTermination(LocalDate terminationInclusive) {
        if (repetition == null) {
            throw new IllegalStateException("Impossible de fixer une terminaison sans d'abord définir la répétition.");
        }
        // Utilisation du constructeur simple et conforme du UML
        repetition.setTermination(new Termination(terminationInclusive));
    }

    /**
     * Opération UML : +setTermination(numberOfOccurrences: long)
     * Crée un objet Termination avec le constructeur simple.
     */
    public void setTermination(long numberOfOccurrences) {
        if (repetition == null) {
            throw new IllegalStateException("Impossible de fixer une terminaison sans d'abord définir la répétition.");
        }
        // Utilisation du constructeur simple et conforme du UML
        repetition.setTermination(new Termination(numberOfOccurrences));
    }
    
    // --- Getters conformes pour la Question 2 / Tests unitaires ---

    public String getTitle() {
        return title;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public Duration getDuration() {
        return duration;
    }

    // Ajouté pour les tests unitaires et la lisibilité du code original

    public LocalDate getTerminationDate() {
        if (repetition != null && repetition.getTermination() != null) {
            Termination term = repetition.getTermination();
            return term.calculateTerminationDate(start.toLocalDate(), repetition.getFrequency());
        }
        return null;
    }

    public long getNumberOfOccurrences() {
        if (repetition != null && repetition.getTermination() != null) {
            Termination term = repetition.getTermination();
            return term.calculateNumberOfOccurrences(start.toLocalDate(), repetition.getFrequency());
        }
        return 0;
    }

    /**
     * Opération UML : +isInDay(d: LocalDate): boolean
     * Gère la répétition, la durée et les exceptions.
     */
    public boolean isInDay(LocalDate aDay) {
        LocalDate eventStartDay = start.toLocalDate();
        LocalDate eventEndDay = start.plus(duration).minusNanos(1).toLocalDate();

        if (repetition == null) {
            boolean occursSingle = !aDay.isBefore(eventStartDay) && !aDay.isAfter(eventEndDay);
            return occursSingle;
        }

        Termination term = repetition.getTermination();
        LocalDate lastOccurrence = null;
        if (term != null) {
            lastOccurrence = term.calculateTerminationDate(eventStartDay, repetition.getFrequency());
        }

        boolean recurring = repetition.isRecurringOn(aDay);
        boolean isLastOccurrence = lastOccurrence != null && (aDay.equals(lastOccurrence) ||
            // Si la durée déborde sur le jour suivant la dernière occurrence
            (aDay.isAfter(lastOccurrence) && lastOccurrence != null &&
                lastOccurrence.atTime(start.toLocalTime()).plus(duration).toLocalDate().equals(aDay)));
        if (recurring || isLastOccurrence) {
            LocalDateTime startOfOccurrence = aDay.atTime(start.toLocalTime());
            LocalDateTime endOfOccurrence = startOfOccurrence.plus(duration);
            return endOfOccurrence.toLocalDate().isAfter(aDay.minusDays(1)) && startOfOccurrence.toLocalDate().isBefore(aDay.plusDays(1));
        }
        return false;
    }

    @Override
    public String toString() {
        return "Event{title='%s', start=%s, duration=%s}".formatted(title, start, duration);
    }
}