/*
 * Created by Maximilian Flügel, 12a
 * 40. Bundeswettbewerb für Informatik - Runde 1
 * Gymnasium Stadtfeld Wernigerode
 */

/**
 * Hauptklasse des Programmes.
 */
public class Main {

    public static boolean DEBUG_MODE = false; //Debugmodus

    public static String INPUT_FILE = "src/main/resources/gewichtsstuecke0.txt"; //Eingabedatei
    public static String OUTPUT_FILE = "output.txt"; //Ausgabedatei

    /**
     * Hauptmethode des Programmes.
     *
     * @param args Konsolenargumente des Programmes.
     */
    public static void main(String[] args) {
        if(args.length > 0) INPUT_FILE = args[0]; //Eingabedatei kann auch als Konsolenparameter angegeben werden
        if(args.length > 1) OUTPUT_FILE = args[1]; //Ausgabedatei kann auch als Konsolenparameter angegeben werden
        if(args.length > 2) DEBUG_MODE = Boolean.parseBoolean(args[2]); //Debug-Modus kann durch Konsolenparameter aktiviert werden

        //Laden der Eingabedatei
        System.out.println();
        System.out.println("Bundeswettbewerb für Informatik - Aufgabe 5 (Marktwaage) - by Maximilian Flügel");
        System.out.println("-------------------------------------------------------------------------------");
        System.out.println("Gewichte \"" + INPUT_FILE + "\" werden geladen...");

        ScaleEvaluator evaluator = new ScaleEvaluator(INPUT_FILE);
        ScaleEvaluator.EvaluationResult result = evaluator.evaluate();

        System.out.println();
        System.out.println("--- Ergebnis ---");
        for(ScaleEvaluator.EvaluationResultEntry entry : result.resultEntries) {
            if(entry.scaleBalanced()) System.out.println("Gewicht: " + entry.targetWeight() + "g - " + entry.printState());
            else System.out.println("Gewicht: " + entry.targetWeight() + "g: " + entry.achievedDifference() + " - " + entry.printState());
        }
        System.out.println();
        long resolvedWeights = result.resultEntries.stream().filter(ScaleEvaluator.EvaluationResultEntry::scaleBalanced).count();
        System.out.println("Von allen Gewichten zwischen 10g und 10kg konnte" + (resolvedWeights == 1 ? "" : "n") + " insgesamt " + resolvedWeights + " Gewicht" + (resolvedWeights == 1 ? "" : "e") + " abgebildet werden.");
        result.export(OUTPUT_FILE);
    }

}