/*
 * Created by Maximilian Flügel, 12a
 * 40. Bundeswettbewerb für Informatik - Runde 1
 * Gymnasium Stadtfeld Wernigerode
 */

/**
 * Hauptklasse des Programmes.
 */
public class Main {

    public static String INPUT_FILE = "src/main/resources/muellabfuhr0.txt";
    public static String OUTPUT_FILE = "output.txt";

    /**
     * Hauptmethode des Programmes.
     *
     * @param args Konsolenargumente des Programmes.
     */
    public static void main(String[] args) {
        if(args.length > 0) INPUT_FILE = args[0];
        if(args.length > 1) OUTPUT_FILE = args[1];

        System.out.println();
        System.out.println("Bundeswettbewerb für Informatik - Runde 2 - Aufgabe 1 (Müllabfuhr) - by Maximilian Flügel");
        System.out.println("Stadtplan " + INPUT_FILE + " wird geladen...");
    }

}
