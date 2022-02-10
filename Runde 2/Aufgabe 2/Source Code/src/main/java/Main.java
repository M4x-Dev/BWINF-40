/*
 * Created by Maximilian Flügel, 12a
 * 40. Bundeswettbewerb für Informatik - Runde 1
 * Gymnasium Stadtfeld Wernigerode
 */

import equations.EquationGenerator;
import equations.EquationVerifier;

/**
 * Hauptklasse des Programmes
 */
public class Main {

    public static final int OPERATOR_COUNT = 5;

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

        /*MultiThreadingPerformanceTest test = new MultiThreadingPerformanceTest();
        test.startBenchmark(10);*/

        /*EquationGenerator generator = new EquationGenerator();
        String equation = generator.generate(5);
        System.out.println(equation);

        //EquationVerifier verifier = new EquationVerifier();
        //System.out.println(verifier.verify("5 o 5 o 1 o 1 o 4 o 1 o 2 = 14"));
        //System.out.println(verifier.verify("2 o 2 o 3 = 12"));*/

        EquationGenerator generator = new EquationGenerator();
        String equation = generator.generate(OPERATOR_COUNT);
        
        System.out.println();
        System.out.println("Das folgende Zahlenrätsel wurde generiert:");
        System.out.println(equation);

        /*
        Executor s = Executors.newFixedThreadPool(10);
        s.execute(() -> {
            EquationSolver solver = new EquationSolver();
            ArrayList<String> solutions = solver.solve(equation);

            System.out.println();
            System.out.println("Der Algorithmus konnte für das Zahlenrätsel folgende Lösungen bestimmen:");
            for(String solution : solutions) System.out.println(solution);
        });*/
    }

}