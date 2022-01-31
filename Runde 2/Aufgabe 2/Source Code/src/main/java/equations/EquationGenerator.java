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

    public static final String OPERATOR_PLACEHOLDER = " o ";

    private final Random numberGenerator = new Random();

    private int currentResult = 0;
    private int currentOperators = 0;

    private int lastNumber;
    private String lastOperator = "";

    public String generate(int operatorCount) {
        StringBuilder equationBuilder = new StringBuilder();

        int startNumber = generateNumber();
        currentResult = startNumber;
        equationBuilder.append(startNumber);

        while(currentOperators <= operatorCount) {
            equationBuilder.append(buildChainElement());
            EquationCalculator.calculate(equationBuilder.toString());
            currentOperators++;
        }

        equationBuilder.append(" = ").append(currentResult);

        System.out.println("Originallösung: " + equationBuilder);

        return hideSolution(equationBuilder.toString());
    }

    public String buildChainElement() {
        //Generieren der nächsten Operation
        //System.out.println("Teilelement wird generiert");
        ArrayList<String> nextOperationPool = new ArrayList<>(Arrays.asList(Constants.OPERATOR_ADD, Constants.OPERATOR_SUBTRACT, Constants.OPERATOR_MULTIPLY, Constants.OPERATOR_DIVIDE));

        if(!lastOperator.isEmpty()) nextOperationPool.remove(lastOperator);

        nextOperationPool.remove(Constants.OPERATOR_MULTIPLY);
        nextOperationPool.remove(Constants.OPERATOR_DIVIDE);

        String nextOperator = nextOperationPool.get(numberGenerator.nextInt(nextOperationPool.size()));
        int nextNumber = 0;
        
        switch(nextOperator) {
            case Constants.OPERATOR_ADD:
                //Nächste Operation ist die Addition
                nextNumber = generateNumber();
                currentResult = currentResult + nextNumber;
                break;
            case Constants.OPERATOR_SUBTRACT:
                //Nächste Operation ist die Subtraktion
                nextNumber = generateNumber(NUM_LOWER_BOUND, Math.min(currentResult, NUM_UPPER_BOUND), GeneratorMode.Ignore);
                currentResult = currentResult - nextNumber;
                break;
            case Constants.OPERATOR_MULTIPLY:
                //Nächste Operation ist die Multiplikation
                nextNumber = generateNumber(1, NUM_UPPER_BOUND, GeneratorMode.Ignore);

                break;
            case Constants.OPERATOR_DIVIDE:
                //Nächste Operation ist die Division
                nextNumber = generateNumber(
                        NUM_LOWER_BOUND,
                        lastNumber,
                        Utils.isEven(lastNumber) ? GeneratorMode.Even : GeneratorMode.Odd
                );
                break;
        }

        lastNumber = nextNumber;
        lastOperator = nextOperator;

        return nextOperator + nextNumber;
    }

    public String hideSolution(String equation) {
        return equation.replaceAll("[//+\"-//*//:]", OPERATOR_PLACEHOLDER);
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