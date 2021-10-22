/**
 * Hauptklasse des Programmes
 */
public class Main {

    public static String INPUT_FILE = "";
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
        System.out.println("Bundeswettbewerb für Informatik - Aufgabe 3 (Wortsuche) - by Maximilian Flügel");
        System.out.println("------------------------------------------------------------------------------");
        System.out.println("");
    }

}
