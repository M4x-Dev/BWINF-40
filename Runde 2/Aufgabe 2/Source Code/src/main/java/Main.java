/*
 * Created by Maximilian Flügel, 12a
 * 40. Bundeswettbewerb für Informatik - Runde 1
 * Gymnasium Stadtfeld Wernigerode
 */

import equations.EquationGenerator;
import equations.EquationVerifier;
import utils.PerformanceBenchmark;

/**
 * Hauptklasse des Programmes
 */
public class Main {

    public static final int OPERATOR_COUNT = 15;

    public static String OUTPUT_FILE = "output.txt";

    /**
     * Hauptmethode des Programmes.
     * @param args Konsolenparameter des Programmes.
     */
    public static void main(String[] args) {
        if(args.length > 0) OUTPUT_FILE = args[0];

        System.out.println();
        System.out.println("Bundeswettbewerb für Informatik - Runde 2 - Aufgabe 2 (Zahlenrätsel) - by Maximilian Flügel");
        System.out.println("-------------------------------------------------------------------------------------------");
        System.out.println();
        System.out.println("Zahlenrätsel mit " + OPERATOR_COUNT + " Operatoren wird generiert...");

        /*EquationGenerator generator = new EquationGenerator();
        String equation = generator.generate(OPERATOR_COUNT);
        
        System.out.println();
        System.out.println("Das folgende Zahlenrätsel wurde generiert:");
        System.out.println(equation);*/

        EquationGenerator generator = new EquationGenerator();
        String equation = generator.generate(OPERATOR_COUNT);
        EquationVerifier verifier = new EquationVerifier();
        System.out.println("Is unique? " + verifier.verifyMultithread(equation));
        //System.out.println("Is unique? " + verifier.verifyMultithread("4 o 3 o 2 o 6 o 3 o 9 o 7 o 8 o 2 o 9 o 4 o 4 o 6 o 4 o 4 o 5 = 4792"));

        /*PerformanceBenchmark benchmark = new PerformanceBenchmark();
        benchmark.benchmark(5);*/
    }

}