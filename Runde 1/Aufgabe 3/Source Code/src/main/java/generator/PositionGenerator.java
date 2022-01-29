/*
 * Created by Maximilian Flügel, 12a
 * 40. Bundeswettbewerb für Informatik - Runde 1
 * Gymnasium Stadtfeld Wernigerode
 */

package generator;

import java.util.ArrayList;
import java.util.Random;

/**
 * Benutzerdefinierte Generator-Klasse, welche Ganzzahlen (Integer) generiert.
 * Dabei existiert eine Ausnahmeliste an Zahlen, welche nicht generiert werden sollen.
 */
public class PositionGenerator {

    private static final int MAX_ATTEMPTS_GENERATOR = 3;

    private final Random instanceRandom;
    private final ArrayList<Integer> exclusions = new ArrayList<>();

    /**
     * Konstruktor der Generator-Klasse.
     * Dieser Konstruktor initialisiert das Random-Objekt der Klasse.
     *
     * @param instanceRandom Random-Objekt, welches von diesem Generator verwendet wird.
     */
    public PositionGenerator(Random instanceRandom) {
        this.instanceRandom = instanceRandom;
    }

    /**
     * Methode, welche eine Zahl der Ausnahmeliste des Generators hinzufügt.
     *
     * @param exclusion Zahl, welche nicht mehr generiert werden soll.
     */
    public void addExclusion(Integer exclusion) {
        if(!this.exclusions.contains(exclusion)) this.exclusions.add(exclusion);
    }

    /**
     * Methode, welche eine Zahl aus der Ausnahmeliste des Generators entfernt.
     *
     * @param exclusion Zahl, welche wieder generiert werden darf.
     */
    public void removeExclusion(Integer exclusion) {
        this.exclusions.remove(exclusion);
    }

    /**
     * Methode, welche die Ausnahmeliste des Generators leert.
     * Damit dürfen alle Zahlen innerhalb der Vorgabe wieder generiert werden.
     */
    public void clearExclusions() {
        this.exclusions.clear();
    }

    /**
     * Hauptfunktion des Generators, welche die Zufallszahl entsprechend der Ausnahmeliste generiert.
     *
     * @param bounds Range, in welcher die Zahl liegen soll, welche generiert wird.
     *
     * @return Gibt die generierte Zufallszahl zurück.
     */
    public int generate(int bounds) {
        return generateNumberInternal(bounds, 0);
    }

    /**
     * Interne Funktion, welche von der Generierungsfunktion verwendet wird, um die Zufallszahl zu berechnen.
     * Wenn die Anzahl der Versuche des Generators eine bestimmte Zahl überschreitet, dann wird der Vorgang abgebrochen.
     *
     * @param bounds Range, in welcher die Zahl liegen soll, welche generiert wird.
     * @param attempt Zahl des momentanen Anlaufes des Generators.
     *
     * @return Gibt die generierte Zufallszahl zurück.
     */
    private int generateNumberInternal(int bounds, int attempt) {
        int number = instanceRandom.nextInt(bounds);
        if(attempt > MAX_ATTEMPTS_GENERATOR) return number;
        return exclusions.contains(number) ? generateNumberInternal(bounds, attempt + 1) : number;
    }

}