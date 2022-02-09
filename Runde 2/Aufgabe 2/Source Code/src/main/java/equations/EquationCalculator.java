package equations;

import utils.Constants;
import utils.Utils;

import java.util.ArrayList;

public class EquationCalculator {

    public static int calculate(String equation) {
        return Integer.parseInt(transformEquation(equation, Constants.OPERATOR_HIERARCHY));
    }

    public static boolean calculatable(String equation) {
        //Entfernen aller Leerzeichen
        equation = equation.replaceAll(" ", "");

        //Hauptschleife des Algorithmus
        while(Utils.containsAny(equation, Constants.OPERATOR_LIST) && equation.length() > 2) {
            for(String operator : Constants.OPERATOR_HIERARCHY) {
                if (equation.contains(operator)) {
                    if(canSolveOperator(equation, operator)) {
                        equation = solveOperator(equation, operator);
                        break;
                    } else {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public static String transformEquation(String equation, ArrayList<String> replaceOperators) {
        //Entfernen aller Leerzeichen
        equation = equation.replaceAll(" ", "");

        //Hauptschleife des Algorithmus
        while(Utils.containsAny(equation, replaceOperators) && equation.length() > 2) {
            for(String operator : replaceOperators) {
                if(equation.contains(operator)) {
                    String inverseOperator = Constants.invertOperator(operator);
                    int operatorIndex = equation.indexOf(operator);
                    int inverseOperatorIndex = equation.indexOf(inverseOperator);

                    equation = solveOperator(equation, operatorIndex < inverseOperatorIndex || inverseOperatorIndex == -1 ? operator : inverseOperator);
                }
            }
        }

        return equation;
    }

    public static String solveOperator(String equation, String operator) {
        int operatorIndex = equation.indexOf(operator);
        int previousOperator = Utils.getNextOperatorIndex(equation, operatorIndex, false);
        int nextOperator = Utils.getNextOperatorIndex(equation, operatorIndex, true);

        String node = equation.length() > 3 ? equation.substring(previousOperator == 0 ? previousOperator : previousOperator + 1, nextOperator == equation.length() - 1 ? nextOperator + 1 : nextOperator) : equation;
        return equation.replace(node, solveNode(node, operator));
    }

    public static boolean canSolveOperator(String equation, String operator) {
        int operatorIndex = equation.indexOf(operator);
        int previousOperator = Utils.getNextOperatorIndex(equation, operatorIndex, false);
        int nextOperator = Utils.getNextOperatorIndex(equation, operatorIndex, true);

        String node = equation.substring(previousOperator == 0 ? previousOperator : previousOperator + 1, nextOperator == equation.length() - 1 ? nextOperator + 1 : nextOperator);
        return canSolveNode(node, operator);
    }

    public static String solveNode(String node, String operator) {
        int result = 0;

        int operatorIndex = node.indexOf(operator);
        int x = Integer.parseInt(node.substring(0, operatorIndex));
        int y = Integer.parseInt(node.substring(operatorIndex + 1));

        switch (operator) {
            case Constants.OPERATOR_ADD -> result = x + y;
            case Constants.OPERATOR_SUBTRACT -> result = x - y;
            case Constants.OPERATOR_MULTIPLY -> result = x * y;
            case Constants.OPERATOR_DIVIDE -> result = x / y;
        }

        return String.valueOf(result);
    }

    public static boolean canSolveNode(String node, String operator) {
        int operatorIndex = node.indexOf(operator);
        int x = Integer.parseInt(node.substring(0, operatorIndex));
        int y = Integer.parseInt(node.substring(operatorIndex + 1));

        return switch(operator) {
            case Constants.OPERATOR_SUBTRACT -> x - y > 0;
            case Constants.OPERATOR_DIVIDE -> x / y > 0;
            default -> true;
        };
    }

}
