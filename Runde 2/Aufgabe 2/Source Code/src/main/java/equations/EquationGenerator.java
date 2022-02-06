package equations;

import utils.Constants;
import utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class EquationGenerator {

    public enum GeneratorMode {
        Ignore,
        Even,
        Odd
    }

    public static final int NUM_LOWER_BOUND = 1;
    public static final int NUM_UPPER_BOUND = 9;

    private final Random numberGenerator = new Random();

    private int currentResult = 0;
    private int currentOperators = 0;

    private int lastNumber;
    private String lastOperator = "";

    private final ArrayList<String> clusters = new ArrayList<>();
    private StringBuilder currentCluster = new StringBuilder();

    public String generate(int operatorCount) {
        StringBuilder equationBuilder = new StringBuilder();

        int startNumber = generateNumber();
        currentResult = startNumber;
        equationBuilder.append(startNumber);

        currentCluster = new StringBuilder(String.valueOf(startNumber));
        clusters.clear();

        while(currentOperators <= operatorCount) {
            String newChainElement = buildChainElement();

            if((Utils.containsAny(currentCluster.toString(), Constants.LINE_OPERATORS)
                    && Utils.containsAny(newChainElement, Constants.POINT_OPERATORS))
                    || (Utils.containsAny(currentCluster.toString(), Constants.POINT_OPERATORS)
                    && Utils.containsAny(newChainElement, Constants.LINE_OPERATORS))) {
                clusters.add(currentCluster.toString());
                currentCluster = new StringBuilder(currentCluster.substring(currentCluster.length() - 1) + newChainElement);
            } else currentCluster.append(newChainElement);

            equationBuilder.append(newChainElement);

            currentResult = EquationCalculator.calculate(equationBuilder.toString());
            currentOperators++;
        }

        clusters.add(currentCluster.toString());
        equationBuilder.append(" = ").append(currentResult);

        System.out.println("Originallösung: " + equationBuilder);
        System.out.print("Cluster: ");
        for(String cluster : clusters) System.out.print("[" + cluster + "]");

        return hideSolution(equationBuilder.toString());
    }

    public String buildChainElement() {
        //Generieren der nächsten Operation
        ArrayList<String> nextOperationPool = new ArrayList<>(Arrays.asList(Constants.OPERATOR_ADD, Constants.OPERATOR_SUBTRACT, Constants.OPERATOR_MULTIPLY, Constants.OPERATOR_DIVIDE));

        if(!lastOperator.isEmpty()) nextOperationPool.remove(lastOperator);
        if(lastNumber <= 2) {
            nextOperationPool.remove(Constants.OPERATOR_DIVIDE);
            nextOperationPool.remove(Constants.OPERATOR_SUBTRACT);
        }

        String nextOperator = nextOperationPool.get(numberGenerator.nextInt(nextOperationPool.size()));
        int nextNumber = switch (nextOperator) {
            case Constants.OPERATOR_ADD ->
                    //Nächste Operation ist die Addition
                    generateNumber();
            case Constants.OPERATOR_SUBTRACT ->
                    //Nächste Operation ist die Subtraktion
                    generateNumber(NUM_LOWER_BOUND, Math.min(currentCluster.length() > 1 ? EquationCalculator.calculate(currentCluster.toString()) : lastNumber, NUM_UPPER_BOUND), GeneratorMode.Ignore);
            case Constants.OPERATOR_MULTIPLY ->
                    //Nächste Operation ist die Multiplikation
                    generateNumber(1, NUM_UPPER_BOUND, GeneratorMode.Ignore);
            case Constants.OPERATOR_DIVIDE ->
                    //Nächste Operation ist die Division
                    generateNumber(
                            NUM_LOWER_BOUND,
                            lastNumber,
                            Utils.isEven(lastNumber) ? GeneratorMode.Even : GeneratorMode.Odd
                    );
            default -> 0;
        };

        lastNumber = nextNumber;
        lastOperator = nextOperator;

        return nextOperator + nextNumber;
    }

    public String hideSolution(String equation) {
        return equation.replaceAll("[//+\"-//*//:]", " " + Constants.OPERATOR_PLACEHOLDER + " ");
    }

    public int generateNumber() {
        return generateNumber(NUM_LOWER_BOUND, NUM_UPPER_BOUND, GeneratorMode.Ignore);
    }

    public int generateNumber(int lower, int upper, GeneratorMode mode) {
        int number = numberGenerator.nextInt(upper - lower) + lower;

        if(mode.equals(GeneratorMode.Ignore)) return number;

        if(mode.equals(GeneratorMode.Even)) {
            if(Utils.isEven(number)) return number;

            if(number - 1 < lower) return number + 1;
            else return number - 1;
        } else if(mode.equals(GeneratorMode.Odd)) {
            if(!Utils.isEven(number)) return number;

            if(number - 1 < lower) return number + 1;
            else return number - 1;
        }

        return number;
    }

}