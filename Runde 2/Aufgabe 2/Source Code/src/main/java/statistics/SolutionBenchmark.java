package statistics;

import equations.EquationGenerator;
import equations.EquationVerifier;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class SolutionBenchmark {

    private final Map<Integer, ArrayList<Integer>> solutionTable = new LinkedHashMap<>();

    public void benchmark(int maxOperatorCount, int medianAttempts) {
        EquationGenerator generator = new EquationGenerator();
        EquationVerifier verifier = new EquationVerifier();

        for(int i = 1; i <= maxOperatorCount; i++) {
            System.out.println("Running benchmark for operator count: " + i);
            for(int j = 0; j < medianAttempts; j++) {
                System.out.println("Running attempt " + j + " of " + medianAttempts);
                String equation = generator.generateRaw(i);
                if(!solutionTable.containsKey(i)) solutionTable.put(i, new ArrayList<>());
                ArrayList<Integer> current = solutionTable.get(i);
                current.add(verifier.solveMultithread(generator.hideSolution(equation), equation));
            }
        }

        System.out.println("--- Benchmark complete ---");
        for(Map.Entry<Integer, ArrayList<Integer>> entry : solutionTable.entrySet()) {
            int solutions = 0;
            for (Integer value : entry.getValue())
                solutions += value;
            solutions /= medianAttempts;

            System.out.println("Operators: " + entry.getKey() + ": " + solutions + " Solutions");
        }
    }

}