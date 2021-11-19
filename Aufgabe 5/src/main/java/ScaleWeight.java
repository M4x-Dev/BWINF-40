/*
 * Created by Maximilian Flügel, 12a
 * 40. Bundeswettbewerb für Informatik - Runde 1
 * Gymnasium Stadtfeld Wernigerode
 */

import java.util.Random;

/**
 * Klasse, welche ein Gewicht auf der Waage dieser Problemstellung darstellt.
 * Diese Klasse wird anstelle von Primitives verwendet, da die Gewichte, welche sich auf der Waage befinden eindeutig zugeordnet werden müssen.
 */
public class ScaleWeight {

    private final long id; //ID des Gewichtes (zur Zuordnung)
    public int value; //Wert bzw. tatsächliches Gewicht

    /**
     * Konstruktor der Klasse, welcher die ID generiert und das Gewicht zuweist.
     *
     * @param value Gewicht der Instanz.
     */
    public ScaleWeight(int value) {
        this.id = new Random().nextLong();
        this.value = value;
    }

    /**
     * Überschriebene Vergleichsfunktion, welche die ID in den Vorgang einbezieht.
     *
     * @param o Objekt, welches verglichen werden soll.
     *
     * @return Gibt zurück, ob das gegebene Objekt mit dieser Instanz identisch ist.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ScaleWeight that = (ScaleWeight) o;
        return id == that.id && value == that.value;
    }

    /**
     * Überschriebe toString-Funktion, welche die Instanz als String ausgibt.
     * Diese Funktion gibt nur das Gewicht dieses Objektes formatiert zurück.
     *
     * @return Gibt das formatierte Gewicht dieser Instanz zurück.
     */
    @Override
    public String toString() {
        return value + "g";
    }

}