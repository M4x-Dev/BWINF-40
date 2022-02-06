package equations;

import utils.Constants;
import utils.Utils;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class EquationSolver {

    private final ArrayList<String> iteratedSolutions = new ArrayList<>();
    private final ArrayList<String> solutions = new ArrayList<>();

    private Executor executor = Executors.newFixedThreadPool(10);

    public ArrayList<String> solve(String equation) {
        iteratedSolutions.clear();
        solutions.clear();

        String[] equationParts = equation.split(" = ");
        String equationBody = equationParts[0].replaceAll(" " + Constants.OPERATOR_PLACEHOLDER + " ", Constants.OPERATOR_ADD);

        int solution = Integer.parseInt(equationParts[1]);
        int operatorCount = (equationBody.length() - 1) / 2;
        int solutionCount = 0;

        for(int i = 1; i <= Math.pow(4, operatorCount + 1); i++) {
            System.out.println("Solving: " + i);
            for(int o = 0; o < operatorCount; o++) {
                if(i % Math.pow(4, o) == 0) {
                    String currentOperator = equationBody.substring(o * 2 + 1, o * 2 + 2);
                    String node = equationBody.substring(o * 2, o * 2 + 3);

                    int x = Integer.parseInt(node.substring(0, 1));
                    int y = Integer.parseInt(node.substring(2, 3));

                    boolean nextSuitableOperatorAvailable = nextOperatorSuitable(currentOperator, x, y);
                    String newOperator = findNextSuitableOperator(currentOperator, x, y);
                    equationBody = equationBody.substring(0, o * 2 + 1) + newOperator + equationBody.substring(o * 2 + 2);

                    if(iteratedSolutions.contains(equationBody)) continue;
                    iteratedSolutions.add(equationBody);
                    if(!nextSuitableOperatorAvailable) continue;

                    int tempSolution = EquationCalculator.calculate(equationBody);
                    if(tempSolution == solution) {
                        solutionCount++;
                        solutions.add(equationBody + " = " + tempSolution);
                        if(solutionCount > 1) break;
                    }
                }
            }
        }

        return solutions;
    }

    private static String findNextSuitableOperator(String operator, int x, int y) {
        return switch (operator) {
            case Constants.OPERATOR_ADD -> nextOperatorSuitable(operator, x, y) ? Constants.OPERATOR_SUBTRACT : findNextSuitableOperator(Constants.OPERATOR_SUBTRACT, x, y);
            case Constants.OPERATOR_SUBTRACT -> nextOperatorSuitable(operator, x, y) ? Constants.OPERATOR_MULTIPLY : findNextSuitableOperator(Constants.OPERATOR_MULTIPLY, x, y);
            case Constants.OPERATOR_MULTIPLY -> nextOperatorSuitable(operator, x, y) ? Constants.OPERATOR_DIVIDE : findNextSuitableOperator(Constants.OPERATOR_DIVIDE, x, y);
            default -> nextOperatorSuitable(operator, x, y) ? Constants.OPERATOR_ADD : findNextSuitableOperator(Constants.OPERATOR_ADD, x, y);
        };
    }

    private static boolean nextOperatorSuitable(String operator, int x, int y) {
        return switch (operator) {
            case Constants.OPERATOR_ADD -> y < x;
            case Constants.OPERATOR_MULTIPLY -> ((Utils.isEven(x) && Utils.isEven(y)) || (!Utils.isEven(x) && !Utils.isEven(y))) && (y <= x);
            default -> true;
        };
    }

}
