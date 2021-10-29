package simulation;

import java.util.Random;

/**
 * Klasse, welche einen Würfel darstellt.
 * Dieser Würfel kann geworfen werden und wird aus einer Datei ausgelesen.
 */
public class LudoDice {

    private int[] sides; //Seiten des Würfels

    /**
     * Konstruktor, welcher einen Würfel aus einer Zeile aus einer Textdatei einließt.
     *
     * @param fileLine Zeile aus der gegebenen Datei.
     */
    public LudoDice(String fileLine) {
        try {
            String[] rawSides = fileLine.split(" ");
            sides = new int[rawSides.length];
            for(int i = 0; i < rawSides.length; i++) sides[i] = Integer.parseInt(rawSides[i]);
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
        return sides[new Random().nextInt(sides.length)];
    }

    /**
     * Funktion, welche den normalen Spielwürfel mit 6 Seiten zurückgibt.
     *
     * @return Gibt den normalen Spielwürfel zurück.
     */
    public static LudoDice sixSided() {
        return new LudoDice("1 2 3 4 5 6");
    }

}