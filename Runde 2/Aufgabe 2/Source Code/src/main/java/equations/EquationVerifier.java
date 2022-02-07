package equations;

import utils.Constants;

import java.util.ArrayList;
import java.util.Objects;

public class EquationVerifier {

    private final ArrayList<String> solutions = new ArrayList<>();

    public boolean verify(String equation) {
        String[] equationParts = equation.split(" = ");
        String equationBody = equationParts[0].replaceAll(" " + Constants.OPERATOR_PLACEHOLDER + " ", Constants.OPERATOR_ADD);

        int solution = Integer.parseInt(equationParts[1]);
        int operatorCount = (equationBody.length() - 1) / 2;

        while(true) {
            //Abbruch der Schleife, wenn alle Kombinationen getestet wurden
            if(equationBody == null)
                break;
            else
                System.out.println(equationBody);

            //Berechnen des Ergebnisses des Termes
            if(EquationCalculator.calculate(equationBody) == solution)
                solutions.add(equationBody);

            //Umformen der Gleichung
            equationBody = iterateOperators(equationBody, 0, operatorCount);
        }

        System.out.println("Solutions:");
        for(String s : solutions) System.out.println(s);
        System.out.println();

        return solutions.size() == 1;
    }

    private static String iterateOperators(String equation, int index, int maxOperators) {
        String currentOperator = extractOperator(equation, index);
        String newOperator = getDescendantOperator(equation, currentOperator, index);

        equation = equation.substring(0, index * 2 + 1) + newOperator + equation.substring(index * 2 + 2);

        //if(!checkEquation(equation, maxOperators)) return iterateOperators(equation, index, maxOperators);

        if(Objects.equals(newOperator, Constants.OPERATOR_LIST.get(0))) {
            if(index < maxOperators - 1)
                return iterateOperators(equation, index + 1, maxOperators);
            else
                return null;
        }

        return equation;
    }

    private static String getDescendantOperator(String equation, String operator, int index) {
        /*int operatorIndex = Constants.OPERATOR_LIST.indexOf(currentOperator);
        return Constants.OPERATOR_LIST.get(operatorIndex < 3 ? operatorIndex + 1 : 0);*/

        String lineEquation = EquationCalculator.toLineCalculation(equation);
        int primaryCluster = EquationCalculator.calculate(lineEquation.substring(0, index * 2 + 1));
        int secondaryCluster = EquationCalculator.calculate(lineEquation.substring(index * 2 + 2));

        switch(operator) {
            case Constants.OPERATOR_ADD:
                //Neuer Operator: Subtrahieren
                return secondaryCluster < primaryCluster ? Constants.OPERATOR_SUBTRACT : getDescendantOperator(equation, Constants.OPERATOR_SUBTRACT, index);
            case Constants.OPERATOR_SUBTRACT:
                //Neuer Operator: Multiplizieren
                return Constants.OPERATOR_MULTIPLY;
            case Constants.OPERATOR_MULTIPLY:
                //Neuer Operator: Dividieren
                boolean validDivision = secondaryCluster <= primaryCluster && primaryCluster % secondaryCluster == 0;
                return validDivision ? Constants.OPERATOR_DIVIDE : getDescendantOperator(equation, Constants.OPERATOR_DIVIDE, index);
            default:
                return Constants.OPERATOR_ADD;
        }
    }

    private static boolean checkEquation(String equation, int operatorCount) {
        System.out.println("Checking: " + equation);
        String lastOperator = null;
        for(int i = 0; i < operatorCount; i++) {
            String operator = extractOperator(equation, i);
            if(!Objects.equals(lastOperator, operator)) lastOperator = operator;
            else return false;
        }
        return true;
    }

    private static String extractOperator(String equation, int index) {
        return equation.substring(index * 2 + 1, index * 2 + 2);
    }

}