import generator.EasyPatternGenerator;
import generator.HardPatternGenerator;

/**
 * Hauptklasse des Programmes
 */
public class Main {

    public static String INPUT_FILE = "src/main/resources/worte5.txt";
    public static String OUTPUT_FILE = "output.txt";

    /**
     * Hauptmethode des Programmes.
     *
     * @param args Konsolenargumente des Programmes.
     */
    public static void main(String[] args) {
        if(args.length > 0) INPUT_FILE = args[0];
        if(args.length > 1) OUTPUT_FILE = args[1];

        //Einlesen der Wortliste
        System.out.println();
        System.out.println("Bundeswettbewerb für Informatik - Aufgabe 3 (Wortsuche) - by Maximilian Flügel");
        System.out.println("------------------------------------------------------------------------------");
        System.out.println("Wortliste " + INPUT_FILE + " wird geladen...");

        //Ausgeben der Details über die eingelesenen Anweisungen
        HardPatternGenerator easyGenerator = new HardPatternGenerator(INPUT_FILE);
        System.out.println();
        System.out.println("Wortliste erfolgreich geladen");
        System.out.println("Größe des Wortfeldes: " + easyGenerator.width + " x " + easyGenerator.height);
        System.out.println("Anzahl der Wörter: " + easyGenerator.wordCount);
        System.out.println("Wortliste:");
        easyGenerator.words.forEach(word -> System.out.println(word));

        System.out.println();
        System.out.println(easyGenerator.generatePattern());
    }

}
