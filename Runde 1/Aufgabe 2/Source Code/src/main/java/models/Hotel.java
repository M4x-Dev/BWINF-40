/*
 * Created by Maximilian Flügel, 12a
 * 40. Bundeswettbewerb für Informatik - Runde 1
 * Gymnasium Stadtfeld Wernigerode
 */

package models;

/**
 * Klasse, welche ein Hotel auf der Route repräsentiert.
 */
public class Hotel {

    public long distanceFromStart;
    public float averageRating;

    /**
     * Konstruktor der Klasse.
     * Dieser Konstruktor erzeugt eine Instanz aus einem gegebenen Text.
     * Dieser Text stammt aus den Beispieldateien.
     *
     * @param rawText Text, welcher aus der Beispieldatei entnommen wurde.
     */
    public Hotel(String rawText) {
        distanceFromStart = Long.parseLong(rawText.split(" ")[0]);
        averageRating = Float.parseFloat(rawText.split(" ")[1]);
    }

}