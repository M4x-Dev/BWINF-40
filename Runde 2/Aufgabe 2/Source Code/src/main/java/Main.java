/*
 * Created by Maximilian Flügel, 12a
 * 40. Bundeswettbewerb für Informatik - Runde 1
 * Gymnasium Stadtfeld Wernigerode
 */

import equations.EquationGenerator;
import equations.EquationVerifier;
import numbers.OperatorGenerator;
import statistics.OperatorBenchmark;
import statistics.OperatorWeightOptimization;

/**
 * Hauptklasse des Programmes
 */
public class Main {

    public static final int OPERATOR_COUNT = 15;
    public static final int MAX_ATTEMPTS = 10;

    public static String OUTPUT_FILE = "output.txt";

    /**
     * Hauptmethode des Programmes.
     * @param args Konsolenparameter des Programmes.
     */
    public static void main(String[] args) {
        //Ausgabedatei
        if(args.length > 0) OUTPUT_FILE = args[0];

        System.out.println();
        System.out.println("Bundeswettbewerb für Informatik - Runde 2 - Aufgabe 2 (Zahlenrätsel) - by Maximilian Flügel");
        System.out.println("-------------------------------------------------------------------------------------------");
        System.out.println();
        System.out.println("Zahlenrätsel mit " + OPERATOR_COUNT + " Operatoren wird generiert...");

        /*OperatorWeightOptimization optimization = new OperatorWeightOptimization(
                OperatorGenerator.WEIGHT_ADDITION,
                OperatorGenerator.WEIGHT_SUBTRACTION,
                OperatorGenerator.WEIGHT_MULTIPLICATION,
                OperatorGenerator.WEIGHT_DIVISION
        );
        optimization.optimize(10, 10);*/

        /*OperatorBenchmark benchmark = new OperatorBenchmark();
        benchmark.benchmark(100, 8);*/

        /*EquationVerifier verifier = new EquationVerifier();
        String equation = "4 o 3 o 2 o 6 o 3 o 9 o 7 o 8 o 2 o 9 o 4 o 4 o 6 o 4 o 4 o 5 = 4792";
        verifier.solveMultithread(equation, equation);*/

        //Generieren des Zahlenrätsels
        EquationGenerator generator = new EquationGenerator();
        String equation = generator.generate(OPERATOR_COUNT, MAX_ATTEMPTS);

        //Ausgabe des Zahlenrätsels
        System.out.println();
        System.out.println("Das folgende Zahlenrätsel wurde generiert:");
        System.out.println(generator.hideSolution(equation));
        System.out.println("Originallösung: " + equation);
    }

}