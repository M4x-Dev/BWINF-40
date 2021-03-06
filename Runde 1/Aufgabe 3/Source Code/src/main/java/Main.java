/*
 * Created by Maximilian Flügel, 12a
 * 40. Bundeswettbewerb für Informatik - Runde 1
 * Gymnasium Stadtfeld Wernigerode
 */

import generator.EasyPatternGenerator;
import generator.HardPatternGenerator;
import generator.MediumPatternGenerator;

import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

/**
 * Hauptklasse des Programmes
 */
public class Main {

    public static String INPUT_FILE = "src/main/resources/worte2.txt";
    public static String OUTPUT_FILE = "output.txt";

    public static String OUTPUT_MODE = "all";

    public static final String ARGS_OUTPUT_MODE_ALL = "all";
    public static final String ARGS_OUTPUT_MODE_EASY = "easy";
    public static final String ARGS_OUTPUT_MODE_MEDIUM = "medium";
    public static final String ARGS_OUTPUT_MODE_HARD = "hard";

    /**
     * Hauptmethode des Programmes.
     *
     * @param args Konsolenargumente des Programmes.
     */
    public static void main(String[] args) {
        if(args.length > 0) INPUT_FILE = args[0];
        if(args.length > 1) OUTPUT_FILE = args[1];
        if(args.length > 2) OUTPUT_MODE = args[2];

        //Einlesen der Wortliste
        System.out.println();
        System.out.println("Bundeswettbewerb für Informatik - Aufgabe 3 (Wortsuche) - by Maximilian Flügel");
        System.out.println("------------------------------------------------------------------------------");
        System.out.println("Wortliste " + INPUT_FILE + " wird geladen...");

        switch(OUTPUT_MODE) {
            case ARGS_OUTPUT_MODE_ALL -> generateAllPatterns();
            case ARGS_OUTPUT_MODE_EASY -> generateEasyPattern();
            case ARGS_OUTPUT_MODE_MEDIUM -> generateMediumPattern();
            case ARGS_OUTPUT_MODE_HARD -> generateHardPattern();
        }
    }

    /**
     * Methode, welche die Wortfelder für alle Schwierigkeitsgrade generiert.
     */
    private static void generateAllPatterns() {
        EasyPatternGenerator easyGenerator = new EasyPatternGenerator(INPUT_FILE);
        String easyPattern = easyGenerator.generatePattern();

        MediumPatternGenerator mediumGenerator = new MediumPatternGenerator(INPUT_FILE);
        String mediumPattern = mediumGenerator.generatePattern();

        HardPatternGenerator hardGenerator = new HardPatternGenerator(INPUT_FILE);
        String hardPattern = hardGenerator.generatePattern();

        System.out.println();
        System.out.println("Wortliste erfolgreich geladen");
        System.out.println("Größe des Wortfeldes: " + easyGenerator.width + " x " + easyGenerator.height);
        System.out.println("Anzahl der Wörter: " + easyGenerator.wordCount);
        System.out.println();
        System.out.println("--- Schwierigkeitsgrad LEICHT ---");
        System.out.println();
        System.out.println(easyPattern);
        System.out.println();
        System.out.println("--- Schwierigkeitsgrad MITTEL ---");
        System.out.println();
        System.out.println(mediumPattern);
        System.out.println();
        System.out.println("--- Schwierigkeitsgrad SCHWER ---");
        System.out.println();
        System.out.println(hardPattern);

        try {
            PrintWriter combinedWriter = new PrintWriter(OUTPUT_FILE, StandardCharsets.UTF_8);
            combinedWriter.print(easyPattern);
            combinedWriter.println();
            combinedWriter.print(mediumPattern);
            combinedWriter.println();
            combinedWriter.print(hardPattern);
            combinedWriter.flush();
            combinedWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Something went wrong :/ - " + e.getMessage());
        }
    }

    /**
     * Methode, welche das Wortfeld der Schwierigkeit "LEICHT" erzeugt.
     */
    private static void generateEasyPattern() {
        EasyPatternGenerator easyGenerator = new EasyPatternGenerator(INPUT_FILE);
        System.out.println();
        System.out.println("Wortliste erfolgreich geladen");
        System.out.println("Größe des Wortfeldes: " + easyGenerator.width + " x " + easyGenerator.height);
        System.out.println("Anzahl der Wörter: " + easyGenerator.wordCount);
        System.out.println();
        System.out.println("--- Schwierigkeitsgrad: LEICHT ---");
        System.out.println();
        System.out.println(easyGenerator.generatePattern());
        easyGenerator.exportToFile(OUTPUT_FILE);
    }

    /**
     * Methode, welche das Wortfeld der Schwierigkeit "MITTEL" erzeugt.
     */
    private static void generateMediumPattern() {
        MediumPatternGenerator mediumGenerator = new MediumPatternGenerator(INPUT_FILE);
        System.out.println();
        System.out.println("Wortliste erfolgreich geladen");
        System.out.println("Größe des Wortfeldes: " + mediumGenerator.width + " x " + mediumGenerator.height);
        System.out.println("Anzahl der Wörter: " + mediumGenerator.wordCount);
        System.out.println();
        System.out.println("--- Schwierigkeitsgrad: MITTEL ---");
        System.out.println();
        System.out.println(mediumGenerator.generatePattern());
        mediumGenerator.exportToFile(OUTPUT_FILE);
    }

    /**
     * Methode, welche das Wortfeld der Schwierigkeit "SCHWER" erzeugt.
     */
    private static void generateHardPattern() {
        HardPatternGenerator hardGenerator = new HardPatternGenerator(INPUT_FILE);
        System.out.println();
        System.out.println("Wortliste erfolgreich geladen");
        System.out.println("Größe des Wortfeldes: " + hardGenerator.width + " x " + hardGenerator.height);
        System.out.println("Anzahl der Wörter: " + hardGenerator.wordCount);
        System.out.println();
        System.out.println("--- Schwierigkeitsgrad: SCHWER ---");
        System.out.println();
        System.out.println(hardGenerator.generatePattern());
        hardGenerator.exportToFile(OUTPUT_FILE);
    }

}
