package utils;

import equations.EquationGenerator;
import equations.EquationVerifier;

import java.util.ArrayList;

public class PerformanceBenchmark {

    private final ArrayList<Long> singleThreadBenchmark = new ArrayList<>();
    private final ArrayList<Long> multiThreadBenchmark = new ArrayList<>();

    private EquationGenerator generator;
    private EquationVerifier singleThreadVerifier, multiThreadVerifier;

    public void benchmark(int range) {
        generator = new EquationGenerator();
        singleThreadVerifier = new EquationVerifier();
        multiThreadVerifier = new EquationVerifier();

        int operators = 1;
        while(operators <= range) {
            boolean result = evaluate(operators);
            System.out.println("Evaluation complete: " + result);
            operators++;
        }

        System.out.println("Benchmark complete");
        System.out.println("Single thread results:");
        for(int a = 0; a < range; a++)
            System.out.println((a + 1) + " Operators: " + singleThreadBenchmark.get(a) + "ms");
        System.out.println();
        System.out.println("Multi thread results:");
        for(int b = 0; b < range; b++)
            System.out.println((b + 1) + " Operators: " + multiThreadBenchmark.get(b) + "ms");
        System.out.println();
    }

    private boolean evaluate(int operators) {
        System.out.println("Operator: " + operators);
        String equation = generator.generate(operators);
        System.out.println("Verifying equation: " + equation);

        long singleThreadStart = System.currentTimeMillis();
        boolean singleThreadResult = singleThreadVerifier.verifySinglethread(equation);
        singleThreadBenchmark.add(System.currentTimeMillis() - singleThreadStart);

        long multiThreadStart = System.currentTimeMillis();
        //boolean multiThreadResult = multiThreadVerifier.verifyMultithread(equation);
        multiThreadBenchmark.add(System.currentTimeMillis() - multiThreadStart);

        //return singleThreadResult && multiThreadResult;
        return false;
    }
}