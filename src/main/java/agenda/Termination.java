package agenda;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit; // Import nécessaire

public class Termination {

    private LocalDate terminationDate;
    private long numberOfOccurrences = -1; // -1 pour indiquer qu'elle n'est pas définie

    /**
     * @return La date de fin inclusive de la répétition (peut être null)
     */
    public LocalDate terminationDateInclusive() {
        return terminationDate;
    }

    /**
     * @return Le nombre maximal d'occurrences (0 si non défini)
     */
    public long numberOfOccurrences() {
        // Retourne 0 si l'occurrence n'est pas la méthode de terminaison (valeur initiale -1)
        return (numberOfOccurrences > 0) ? numberOfOccurrences : 0;
    }

    /**
     * «constructor»+Termination(dateInclusive: LocalDate)
     * @param dateInclusive La date de fin inclusive de la répétition
     */
    public Termination(LocalDate dateInclusive) {
        this.terminationDate = dateInclusive;
        // On s'assure que l'autre valeur est bien non utilisée
        this.numberOfOccurrences = -1; 
    }

    /**
     * «constructor»+Termination(numberOfOccurrences: long)
     * @param numberOfOccurrences Le nombre d'occurrences de cet événement répétitif
     */
    public Termination(long numberOfOccurrences) {
        if (numberOfOccurrences < 1) {
             throw new IllegalArgumentException("Le nombre d'occurrences doit être positif.");
        }
        this.numberOfOccurrences = numberOfOccurrences;
        // On s'assure que l'autre valeur est bien non utilisée
        this.terminationDate = null;
    }

    // Ajouté : Méthode interne pour vérifier si la date est dépassée
    public boolean isAfterTermination(LocalDate date, ChronoUnit frequency, LocalDate startDate) {
        // Logique de fin basée sur la Date
        if (terminationDate != null) {
            // Un événement est terminé si la date est strictement après la date d'inclusion
            return date.isAfter(terminationDate);
        }

        // Logique de fin basée sur le Nombre d'occurrences
        if (numberOfOccurrences > 0) {
            // Calcule le nombre de répétitions entre le début et la date demandée
            // On ajoute 1 car le calcul de between est exclusif du point de fin
            long actualOccurrences = frequency.between(startDate, date) + 1;
            
            // L'événement est terminé si on dépasse le nombre d'occurrences
            return actualOccurrences > numberOfOccurrences;
        }

        // Par défaut (ni date ni nombre d'occurrences spécifié), il n'y a pas de fin.
        return false;
    }

    /**
     * Calcule dynamiquement le nombre d'occurrences à partir de la date de début et de la date de terminaison
     */
    public long calculateNumberOfOccurrences(LocalDate startDate, ChronoUnit frequency) {
        if (terminationDate != null) {
            return frequency.between(startDate, terminationDate) + 1;
        }
        return numberOfOccurrences();
    }

    /**
     * Calcule dynamiquement la date de terminaison à partir de la date de début et du nombre d'occurrences
     */
    public LocalDate calculateTerminationDate(LocalDate startDate, ChronoUnit frequency) {
        if (numberOfOccurrences > 0) {
            return startDate.plus(numberOfOccurrences - 1, frequency);
        }
        return terminationDateInclusive();
    }
}