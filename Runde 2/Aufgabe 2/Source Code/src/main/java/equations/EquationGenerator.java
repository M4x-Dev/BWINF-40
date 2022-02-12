package equations;

import utils.Operators;
import utils.DebugUtils;
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
    public static final int NUM_UPPER_BOUND = 10;

    private StringBuilder equationBuilder;
    private final Random numberGenerator = new Random();

    private int currentResult = 0;
    private int currentOperators = 0;

    private int lastNumber;
    private String lastOperator = "";

    public String generate(int operatorCount, int maxAttempts) {
        boolean equationValid = false;
        String equation = "";
        int attempts = 0;

        EquationVerifier verifier = new EquationVerifier();

        while(attempts < maxAttempts && !equationValid) {
            attempts++;
            equation = generateRaw(operatorCount);
            equationValid = verifier.verifyMultithread(hideSolution(equation), equation);

            if(!equationValid) {
                System.out.println("Equation is not unique, generating again...");

                if(attempts == maxAttempts)
                    System.err.println("Error: Cannot generate unique equation for the given parameters");
            }
        }

        return equation;
    }

    public String generateRaw(int operatorCount) {
        equationBuilder = new StringBuilder();
        lastOperator = "";
        lastNumber = 0;
        currentResult = 0;
        currentOperators = 0;

        int startNumber = generateNumber();
        currentResult = startNumber;
        equationBuilder.append(startNumber);

        while(currentOperators < operatorCount) {
            equationBuilder.append(buildChainElement(null));
            currentResult = EquationCalculator.calculate(equationBuilder.toString());
            currentOperators++;
        }

        equationBuilder.append(" = ").append(currentResult);
        DebugUtils.println("Originallösung: " + equationBuilder);
        DebugUtils.println("Equation: " + equationBuilder.toString());

        return equationBuilder.toString();
    }

    public String buildChainElement(String forceOperator) {
        //Generieren der nächsten Operation
        ArrayList<String> nextOperationPool = new ArrayList<>(Arrays.asList(Operators.OPERATOR_ADD, Operators.OPERATOR_SUBTRACT, Operators.OPERATOR_MULTIPLY, Operators.OPERATOR_DIVIDE));

        String equationSumExpression = EquationCalculator.transformEquation(equationBuilder.toString(), Operators.excludeOperators(Operators.OPERATOR_ADD));
        String equationDifferenceExpression = EquationCalculator.transformEquation(equationBuilder.toString(), Operators.excludeOperators(Operators.OPERATOR_SUBTRACT));
        int sumExpressionLastNumber = Utils.extractLastNumber(equationSumExpression);

        DebugUtils.println("Difference: " + equationDifferenceExpression);

        if(!lastOperator.isEmpty()) nextOperationPool.remove(lastOperator);

        if(lastNumber <= 2) {
            nextOperationPool.remove(Operators.OPERATOR_DIVIDE);
            nextOperationPool.remove(Operators.OPERATOR_SUBTRACT);
        }

        if(!Utils.isEven(lastNumber) && nextOperationPool.size() > 1)
            nextOperationPool.remove(Operators.OPERATOR_DIVIDE);

        if(forceOperator != null) {
            nextOperationPool.clear();
            nextOperationPool.add(forceOperator);
        }

        int nextNumber;
        String nextOperator = nextOperationPool.get(numberGenerator.nextInt(nextOperationPool.size()));
        switch (nextOperator) {
            case Operators.OPERATOR_ADD -> {
                //Nächste Operation ist die Addition
                nextNumber = generateNumber();
            }
            case Operators.OPERATOR_SUBTRACT -> {
                //Nächste Operation ist die Subtraktion
                DebugUtils.println("Last: " + lastNumber + "; Last sum: " + sumExpressionLastNumber);
                nextNumber = generateNumber(NUM_LOWER_BOUND, Math.min(lastNumber, sumExpressionLastNumber), GeneratorMode.Ignore);
            }
            case Operators.OPERATOR_MULTIPLY -> {
                //Nächste Operation ist die Multiplikation
                int upperBound;
                int lastOperatorIndex = Utils.getLastOperatorIndex(equationBuilder.toString(), Operators.LINE_OPERATORS);
                int lastDifferenceOperator = Utils.getLastOperatorIndex(equationDifferenceExpression, Operators.LINE_OPERATORS);

                if (equationBuilder.length() > 1 && Utils.containsAny(equationBuilder.toString(), Operators.LINE_OPERATORS) && equationBuilder.substring(lastOperatorIndex, lastOperatorIndex + 1).equals(Operators.OPERATOR_SUBTRACT)) {
                    DebugUtils.println("Calculating bounds (multiplication)");

                    int x, y;
                    if (equationDifferenceExpression.length() == 3) {
                        x = Integer.parseInt(equationDifferenceExpression.substring(0, lastDifferenceOperator));
                    } else {
                        int previousOperatorIndex = Utils.getNextOperatorIndex(equationDifferenceExpression, lastDifferenceOperator, false);
                        DebugUtils.println("Previous index: " + previousOperatorIndex + " | Last operator index: " + lastDifferenceOperator);
                        x = Integer.parseInt(equationDifferenceExpression.substring(previousOperatorIndex + 1, lastDifferenceOperator));
                    }

                    y = Integer.parseInt(equationDifferenceExpression.substring(lastDifferenceOperator + 1));
                    upperBound = Math.min(Math.min((x / y) - 1, NUM_UPPER_BOUND), lastNumber);
                    DebugUtils.println("Upper bound: " + upperBound);

                    if(upperBound < 2)
                        return buildChainElement(Operators.OPERATOR_ADD);
                } else {
                    DebugUtils.println("Using default bounds (multiplication)");
                    upperBound = NUM_UPPER_BOUND;
                }
                nextNumber = generateNumber(NUM_LOWER_BOUND, upperBound, GeneratorMode.Ignore);
            }
            case Operators.OPERATOR_DIVIDE -> {
                //Nächste Operation ist die Division
                ArrayList<Integer> dividers = getDividers(lastNumber);
                nextNumber = dividers.get(generateNumber(0, dividers.size(), GeneratorMode.Ignore));

                if(equationDifferenceExpression.contains(Operators.OPERATOR_SUBTRACT)) {
                    int lastDifferenceOperator = Utils.getLastOperatorIndex(equationDifferenceExpression, Operators.LINE_OPERATORS);

                    int x, y;
                    if (equationDifferenceExpression.length() == 3) {
                        x = Integer.parseInt(equationDifferenceExpression.substring(0, lastDifferenceOperator));
                    } else {
                        int previousOperatorIndex = Utils.getNextOperatorIndex(equationDifferenceExpression, lastDifferenceOperator, false);
                        DebugUtils.println("Previous index: " + previousOperatorIndex + " | Last operator index: " + lastDifferenceOperator);
                        x = Integer.parseInt(equationDifferenceExpression.substring(previousOperatorIndex + 1, lastDifferenceOperator));
                    }

                    y = Integer.parseInt(equationDifferenceExpression.substring(lastDifferenceOperator + 1));

                    if(x < y * nextNumber) {
                        if(dividers.indexOf(nextNumber) < dividers.size() - 1)
                            nextNumber = dividers.get(dividers.indexOf(nextNumber) + 1);
                        else
                            return buildChainElement(Operators.OPERATOR_ADD);
                    }
                }
            }
            default -> nextNumber = 0;
        }

        lastNumber = nextNumber;
        lastOperator = nextOperator;

        return nextOperator + nextNumber;
    }

    public String hideSolution(String equation) {
        return equation.replaceAll("[//+\"-//*//:]", " " + Operators.OPERATOR_PLACEHOLDER + " ");
    }

    public int generateNumber() {
        return generateNumber(NUM_LOWER_BOUND, NUM_UPPER_BOUND, GeneratorMode.Ignore);
    }

    public int generateNumber(int lower, int upper, GeneratorMode mode) {
        int range = upper - lower;
        int number = numberGenerator.nextInt(range > 0 ? range : 1) + lower;

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

    public ArrayList<Integer> getDividers(int num) {
        ArrayList<Integer> dividers = new ArrayList<>();
        for(int i = 1; i <= num; i++) {
            if(num % i == 0)
                dividers.add(i);
        }
        dividers.remove((Integer)num);
        return dividers;
    }

}