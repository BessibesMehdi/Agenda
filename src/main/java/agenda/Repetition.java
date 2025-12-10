package agenda;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class Repetition {
    
    /**
     * Attribut myFrequency : La fréquence de cette répétition (DAYS, WEEKS, MONTHS).
     * Rôle UML : #frequency: ChronoUnit
     */
    private final ChronoUnit myFrequency;

    /**
     * Attribut exceptions : Dates auxquelles l'événement ne doit pas se répéter.
     * Rôle UML : #exceptions: LocalDate[*]
     */
    private final List<LocalDate> exceptions = new ArrayList<>();

    /**
     * Attribut termination : La terminaison optionnelle de la répétition.
     * Rôle UML : non nommé, multiplicité 0..1
     */
    private Termination termination;

    // Ajouté : Réf. à la date de début de l'Event pour le calcul de répétition
    private final LocalDate eventStartDate;

    /**
     * «constructor»+Repetition(f: ChronoUnit, startDate: LocalDate)
     * NOTE: Nous ajoutons startDate dans le constructeur pour le calcul correct
     * des occurrences dans Repetition/Termination, mais ce n'est pas un champ UML.
     * C'est la dépendance la plus propre.
     */
    public Repetition(ChronoUnit myFrequency, LocalDate eventStartDate) {
        this.myFrequency = myFrequency;
        this.eventStartDate = eventStartDate;
    }

    public ChronoUnit getFrequency() {
        return myFrequency;
    }

    public void addException(LocalDate date) {
        exceptions.add(date);
    }

    public void setTermination(Termination termination) {
        this.termination = termination;
    }

    public Termination getTermination() {
        return termination;
    }

    /**
     * Vérifie si l'événement se produit à une date donnée, en tenant compte :
     * 1. De la fréquence (répétition réelle)
     * 2. Des exceptions
     * 3. De la terminaison
     */
    public boolean isRecurringOn(LocalDate aDay) {
        // 1. Vérifie si la date est une exception
        if (exceptions.contains(aDay)) {
            return false;
        }
        
        // 2. Vérifie si la date est avant la date de début
        if (aDay.isBefore(eventStartDate)) {
            return false;
        }
        
        // 3. Vérifie la terminaison
        Termination term = termination;
        if (term != null) {
            // Calcule la dernière occurrence possible selon la terminaison
            LocalDate lastOccurrence = term.calculateTerminationDate(eventStartDate, myFrequency);
            // Si la date testée est exactement la dernière occurrence, c'est valide
            if (lastOccurrence != null && aDay.equals(lastOccurrence)) {
                return true;
            }
            // Si la date testée est après la terminaison, ce n'est pas valide
            if (term.isAfterTermination(aDay, myFrequency, eventStartDate)) {
                return false;
            }
        }

        // 4. Vérifie si la date correspond à la fréquence de répétition.
        // Calcule l'écart entre la date de début et la date testée selon la fréquence.
        long diff = myFrequency.between(eventStartDate, aDay);
        
        // Si la différence est un multiple de la fréquence, l'événement a lieu.
        // Pour DAYS (quotidien) ou WEEKS (hebdomadaire), le modulo doit être 0.
        // Pour MONTHS/YEARS (mensuel/annuel), la logique est plus complexe (même jour/mois).
        // Étant donné que le contexte se limite à DAYS/WEEKS/MONTHS, nous allons nous 
        // concentrer sur DAYS/WEEKS pour l'instant. Pour le mensuel, cela nécessiterait
        // une logique plus complexe (ex: vérifier le jour du mois).
        
        if (myFrequency == ChronoUnit.DAYS) {
            return true; // Si c'est quotidien, et qu'on a passé les checks de début/fin/exception.
        } else if (myFrequency == ChronoUnit.WEEKS) {
            // Si c'est hebdomadaire, on vérifie si le jour de la semaine est le même
            return aDay.getDayOfWeek() == eventStartDate.getDayOfWeek();
        } else if (myFrequency == ChronoUnit.MONTHS) {
            // Si c'est mensuel, on vérifie si le jour du mois est le même
            return aDay.getDayOfMonth() == eventStartDate.getDayOfMonth();
        }
        
        return diff >= 0 && (diff % 1 == 0); // Logique simplifiée pour d'autres ChronoUnits
    }
}