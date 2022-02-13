package statistics;

import equations.EquationGenerator;
import equations.EquationVerifier;
import utils.Operators;

import java.util.ArrayList;

public class OperatorBenchmark {

    public OperatorBenchmarkResult benchmark(int plots, int operatorCount) {
        OperatorBenchmarkResult result = new OperatorBenchmarkResult();
        EquationGenerator generator = new EquationGenerator();
        EquationVerifier solver = new EquationVerifier();

        for(int i = 1; i <= plots; i++) {
            String equation = generator.generateRaw(operatorCount);
            int solutions = solver.solveMultithread(generator.hideSolution(equation), equation);
            result.additionResults.add(new OperatorBenchmarkResultEntry(Operators.OPERATOR_ADD, countOperator(equation, Operators.OPERATOR_ADD), solutions));
            result.subtractionResults.add(new OperatorBenchmarkResultEntry(Operators.OPERATOR_SUBTRACT, countOperator(equation, Operators.OPERATOR_SUBTRACT), solutions));
            result.multiplicationResults.add(new OperatorBenchmarkResultEntry(Operators.OPERATOR_MULTIPLY, countOperator(equation, Operators.OPERATOR_MULTIPLY), solutions));
            result.divisionResults.add(new OperatorBenchmarkResultEntry(Operators.OPERATOR_DIVIDE, countOperator(equation, Operators.OPERATOR_DIVIDE), solutions));
        }

        System.out.println("--- Benchmark complete ---");

        ArrayList<String> resultData = new ArrayList<>();
        for(int i = 0; i < result.additionResults.size(); i++) {
            OperatorBenchmarkResultEntry addResult = result.additionResults.get(i);
            OperatorBenchmarkResultEntry subtractResult = result.subtractionResults.get(i);
            OperatorBenchmarkResultEntry multiplyResult = result.multiplicationResults.get(i);
            OperatorBenchmarkResultEntry divideResult = result.divisionResults.get(i);

            resultData.add(addResult.operatorCount + " " + subtractResult.operatorCount + " " + multiplyResult.operatorCount + " " + divideResult.operatorCount + " " + addResult.solutionCount);
        }

        resultData.sort((s1, s2) -> {
            String[] s1Parts = s1.split(" ");
            String[] s2Parts = s2.split(" ");

            return Integer.compare(Integer.parseInt(s1Parts[s1Parts.length - 1]), Integer.parseInt(s2Parts[s2Parts.length - 1]));
        });

        for(String resultEntry : resultData)
            System.out.println(resultEntry);

        return result;
    }

    public int countOperator(String equation, String operator) {
        int operatorCount = 0;
        for(int i = 0; i < (equation.length() - 1) / 2; i++) {
            if(equation.substring(i * 2 + 1, i * 2 + 2).equalsIgnoreCase(operator))
                operatorCount++;
        }
        return operatorCount;
    }

    public static class OperatorBenchmarkResult {
        public final ArrayList<OperatorBenchmarkResultEntry>
                additionResults = new ArrayList<>(),
                subtractionResults = new ArrayList<>(),
                multiplicationResults = new ArrayList<>(),
                divisionResults = new ArrayList<>();
    }

    public static class OperatorBenchmarkResultEntry {
        public String operator;
        public int operatorCount;
        public int solutionCount;

        public OperatorBenchmarkResultEntry(String operator, int operatorCount, int solutionCount) {
            this.operator = operator;
            this.operatorCount = operatorCount;
            this.solutionCount = solutionCount;
        }
    }

}