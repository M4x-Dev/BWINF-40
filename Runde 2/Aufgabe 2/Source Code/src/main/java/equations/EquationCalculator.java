package equations;

import utils.Constants;

public class EquationCalculator {

    public static int calculate(String equation) {
        //Entfernen aller Leerzeichen
        equation = equation.replaceAll(" ", "");

        //Hauptschleife des Algorithmus
        while(equation.contains(Constants.OPERATOR_ADD)
                || equation.contains(Constants.OPERATOR_SUBTRACT)
                || equation.contains(Constants.OPERATOR_MULTIPLY)
                || equation.contains(Constants.OPERATOR_DIVIDE)) {
            for(String operator : Constants.OPERATOR_HIERARCHY) {
                if(equation.contains(operator)) {
                    equation = solveOperator(equation, operator);
                    break;
                }
            }
        }

        return Integer.parseInt(equation);
    }

    public static String solveOperator(String equation, String operator) {
        int operatorIndex = equation.indexOf(operator);
        int previousOperator = findNextOperatorIndex(equation, operatorIndex, false);
        int nextOperator = findNextOperatorIndex(equation, operatorIndex, true);

        String node = equation.substring(previousOperator == 0 ? previousOperator : previousOperator + 1, nextOperator == equation.length() - 1 ? nextOperator + 1 : nextOperator);
        return equation.replace(node, solveNode(node, operator));
    }

    private static int findNextOperatorIndex(String equation, int start, boolean forward) {
        for(int i = start + (forward ? 1 : -1); forward ? i < equation.length() : i >= 0; i += forward ? 1 : -1) {
            if(Constants.OPERATOR_HIERARCHY.contains(equation.substring(i, i + 1)))
                return i;
        }

        return forward ? equation.length() - 1 : 0;
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

}
