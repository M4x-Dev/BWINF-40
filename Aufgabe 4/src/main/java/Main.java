import evaluation.DiceEvaluator;
import simulation.LudoSimulation;

import java.util.ArrayList;

/**
 * Hauptklasse des Programmes
 */
public class Main {

    public static String INPUT_FILE = "src/main/resources/wuerfel0.txt"; //Eingabedatei des Programmes
    public static String OUTPUT_FILE = "output.txt"; //Ausgabedatei des Programmes

    public static boolean DEBUG_OUTPUT = false; //Ausgabe des Spielgeschehens

    /**
     * Hauptmethode des Programmes.
     *
     * @param args Konsolenargumente des Programmes.
     */
    public static void main(String[] args) {
        if(args.length > 0) INPUT_FILE = args[0]; //Eingabedatei kann auch als Konsolenparameter angegeben werden
        if(args.length > 1) OUTPUT_FILE = args[1]; //Ausgabedatei kann auch als Konsolenparameter angegeben werden
        if(args.length > 2) DEBUG_OUTPUT = Boolean.parseBoolean(args[2]); //Debug-Ausgabe kann eingeschaltet werden
        if(args.length > 3) DiceEvaluator.SIMULATION_RECURSIONS = Integer.parseInt(args[3]); //Wiederholungen der Simulationen können auch verändert werden, um die Genauigkeit zu erhöhen

        //Laden der Eingabedatei
        System.out.println();
        System.out.println("Bundeswettbewerb für Informatik - Aufgabe 4 (Würfelglück) - by Maximilian Flügel");
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("Würfel \"" + INPUT_FILE + "\" werden geladen...");

        //Starten der Auswertung
        LudoSimulation.debugOutput = DEBUG_OUTPUT;
        DiceEvaluator evaluator = new DiceEvaluator(INPUT_FILE);
        DiceEvaluator.EvaluatorResult result = evaluator.evaluate();
        result.export(OUTPUT_FILE);

        //Ausgabe des Ergebnisses
        System.out.println("Auswertung erfolgreich.");
        System.out.println();
        System.out.println("--- Ergebnisse ---");
        for(int i = 0; i < result.results.size(); i++) System.out.println("Mit Würfel " + (i + 1) + " " + new ArrayList<>(result.results.keySet()).get(i).sides + " wurde " + new ArrayList<>(result.results.values()).get(i) + " mal gewonnen");
        System.out.println();
        System.out.println("Der Würfel " + result.bestDice.sides + " hat demnach die besten Gewinnchancen (Gewonnen: " + result.results.get(result.bestDice) + ")");
    }

}
