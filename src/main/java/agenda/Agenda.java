package agenda;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Description : An agenda that stores events
 */
public class Agenda {
    // Rôle UML : #events (Composition, 0..*)
    List<Event> events = new ArrayList<>(); // Utilisation de List pour respecter le type Event[*]

    /**
     * Adds an event to this agenda
     *
     * @param e the event to add
     */
    public void addEvent(Event e) {
        events.add(e);
    }

    /**
     * Opération UML : +eventsInDay(day: LocalDate): Event[*]
     * Computes the events that occur on a given day
     *
     * @param day the day to test
     * @return a list of events that occur on that day
     */
    public List<Event> eventsInDay(LocalDate day) {
        ArrayList<Event> todayEvents = new ArrayList<Event>();
        for (Event e : events) {
            if (e.isInDay(day)){
                todayEvents.add(e);
            }
        }
        return todayEvents;
    }
    
    // --- Début des questions complémentaires (implémentées) ---

    /**
     * Trouver les événements de l'agenda en fonction de leur titre
     * @param title le titre à rechercher
     * @return les événements qui ont le même titre
     */
    public List<Event> findByTitle(String title) {
        List<Event> eventsWithTitle = new ArrayList<>();
        for (Event e : events) {
            if (e.getTitle().equals(title)) {
                eventsWithTitle.add(e);
            }
        }
        return eventsWithTitle;
    }
    
    /**
     * Déterminer s’il y a de la place dans l'agenda pour un événement (aucun autre événement au même moment)
     * @param e L'événement à tester (on se limitera aux événements sans répétition selon la consigne originale)
     * @return vrai s’il y a de la place dans l'agenda pour cet événement
     */
    public boolean isFreeFor(Event e) {
        // Pour les événements simples (sans répétition), on vérifie l'intervalle de temps.
        
        // Calculer l'intervalle de l'événement à tester.
        // Utilisation de .isAfter() et .isBefore() pour la comparaison de temps.
        LocalDateTime testStart = e.getStart();
        LocalDateTime testEnd = e.getStart().plus(e.getDuration());

        for (Event existingEvent : events) {
            // Limiter aux événements sans répétition comme demandé dans la consigne (pour la simplicité du test)
            // Pour être précis, cette méthode devrait fonctionner aussi avec des événements répétitifs,
            // mais l'implémentation est plus complexe (calculer toutes les occurrences).
            
            // On vérifie la superposition avec les événements existants
            LocalDateTime existingStart = existingEvent.getStart();
            LocalDateTime existingEnd = existingEvent.getStart().plus(existingEvent.getDuration());

            // Condition de chevauchement : (Début1 < Fin2) ET (Fin1 > Début2)
            if (testStart.isBefore(existingEnd) && testEnd.isAfter(existingStart)) {
                return false; // Il y a un conflit
            }
        }
        return true; // Aucune superposition trouvée
    }
}