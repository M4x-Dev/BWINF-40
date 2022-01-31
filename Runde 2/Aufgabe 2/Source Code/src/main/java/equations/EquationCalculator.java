package equations;

import utils.Constants;

import java.util.ArrayList;

public class EquationCalculator {

    private static final ArrayList<String> OPERATOR_HIERARCHY = new ArrayList<>() {{
        add(Constants.OPERATOR_ADD);
        add(Constants.OPERATOR_SUBTRACT);
        add(Constants.OPERATOR_MULTIPLY);
        add(Constants.OPERATOR_DIVIDE);
    }};

    public static int calculate(String equation) {
        //Entfernen aller Leerzeichen
        equation = equation.replaceAll(" ", "");

        //Hauptschleife des Algorithmus
        while(equation.contains(Constants.OPERATOR_ADD)
                || equation.contains(Constants.OPERATOR_SUBTRACT)
                || equation.contains(Constants.OPERATOR_MULTIPLY)
                || equation.contains(Constants.OPERATOR_DIVIDE)) {
            for(String operator : OPERATOR_HIERARCHY) {
                if(equation.contains(operator))
                    equation = solveOperator(equation, operator);
            }

            System.out.println("Solved step: " + equation);
        }

        return Integer.parseInt(equation);
    }

    public static String solveOperator(String equation, String operator) {
        int operatorIndex = equation.indexOf(operator);
        String node = equation.substring(operatorIndex - 1, operatorIndex + 2);
        return equation.replace(node, solveNode(node));
    }

    public static String solveNode(String node) {
        int result = 0;

        String operator = node.substring(1, 2);
        int x = Integer.parseInt(node.substring(0, 1));
        int y = Integer.parseInt(node.substring(2, 3));

        switch (operator) {
            case Constants.OPERATOR_ADD -> result = x + y;
            case Constants.OPERATOR_SUBTRACT -> result = x - y;
            case Constants.OPERATOR_MULTIPLY -> result = x * y;
            case Constants.OPERATOR_DIVIDE -> result = x / y;
        }

        return String.valueOf(result);
    }

}
