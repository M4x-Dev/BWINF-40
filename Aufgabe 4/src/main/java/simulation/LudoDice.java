package simulation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Klasse, welche einen Würfel darstellt.
 * Dieser Würfel kann geworfen werden und wird aus einer Datei ausgelesen.
 */
public class LudoDice {

    public ArrayList<Integer> sides; //Seiten des Würfels

    /**
     * Konstruktor, welcher einen Würfel aus einer Zeile aus einer Textdatei einließt.
     *
     * @param fileLine Zeile aus der gegebenen Datei.
     */
    public LudoDice(String fileLine) {
        try {
            //Wegschneiden der ersten Zahl, da diese die Anzahl an Seiten angibt
            String[] rawSides = fileLine.substring(fileLine.indexOf(" ") + 1).split(" ");
            sides = new ArrayList<>();

            //Speichern aller Seiten des Würfels
            for (String rawSide : rawSides) sides.add(Integer.parseInt(rawSide));
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Something went wrong :/ - " + e.getMessage());
        }
    }

    /**
     * Funktion, welche das "Werfen" eines Würfels darstellt.
     * Diese Funktion gibt eine der Seiten des Würfels zufällig zurück.
     *
     * @return Gibt eine Seite des Würfels zurück.
     */
    public int roll() {
        //Zufällige Seite des Würfels wird ermittelt
        return sides.get(new Random().nextInt(sides.size()));
    }

    /**
     * Funktion, welche einen Würfel validiert.
     * Damit ein Würfel gültig ist, muss mindestens eine der Seiten des Würfels eine 6 haben.
     * Andernfalls kann das Spiel nicht gespielt werden, da die B-Felder nicht verlassen werden können.
     *
     * @return Gibt zurück, ob der Würfel gültig ist.
     */
    public boolean validate() {
        return sides.contains(6);
    }

    /**
     * Funktion, welche die kleinste Augenzahl auf dem Würfel zurückgibt.
     *
     * @return Gibt die kleinste Augenzahl auf dem Würfel zurück.
     */
    public int getSmallestSide() {
        ArrayList<Integer> sideCopy = new ArrayList<>(sides);

        //Seiten werden der Größe nach sortiert, sodass die kleinste Zahl an erster Stelle steht
        Collections.sort(sideCopy);
        return sideCopy.get(0);
    }

    /**
     * Funktion, welche den normalen Spielwürfel mit 6 Seiten zurückgibt.
     *
     * @return Gibt den normalen Spielwürfel zurück.
     */
    public static LudoDice sixSided() {
        return new LudoDice("6 1 2 3 4 5 6");
    }

}