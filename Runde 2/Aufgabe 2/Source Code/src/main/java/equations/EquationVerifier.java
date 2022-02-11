package equations;

import utils.Operators;
import utils.Utils;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class EquationVerifier {

    public interface VerifierActionResultListener {
        void onVerificationComplete(ArrayList<String> solutions);
        void onSolutionFound(String solution);
    }

    private final ArrayList<String> allSolutions = new ArrayList<>();

    private VerifierActionResultListener localListener;
    private ExecutorService verifierExecutorService = Executors.newFixedThreadPool(4);

    public boolean verifySinglethread(String equation) {
        try {
            allSolutions.clear();
            AtomicInteger counter = new AtomicInteger(4);
            localListener = new VerifierActionResultListener() {
                @Override
                public void onVerificationComplete(ArrayList<String> solutions) {

                }

                @Override
                public void onSolutionFound(String solution) {

                }
            };

            //Singlethread solution
            verifierExecutorService = Executors.newFixedThreadPool(4);
            //verifierExecutorService.submit(new VerifierRunnable(localListener, equation, Operators.OPERATOR_ADD, null));

            verifierExecutorService.shutdown();
            if(verifierExecutorService.awaitTermination(5, TimeUnit.MINUTES))
                return allSolutions.size() == 1;
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean verifyMultithread(String equation) {
        try {
            allSolutions.clear();
            AtomicInteger counter = new AtomicInteger(4);
            localListener = new VerifierActionResultListener() {
                @Override
                public void onVerificationComplete(ArrayList<String> solutions) {
                    counter.decrementAndGet();
                }

                @Override
                public void onSolutionFound(String solution) {
                    allSolutions.add(solution);

                    if(allSolutions.size() > 1)
                        verifierExecutorService.shutdownNow();
                }
            };

            String[] equationParts = equation.split(" = ");
            String equationBody = equationParts[0].replaceAll(" " + Operators.OPERATOR_PLACEHOLDER + " ", Operators.OPERATOR_ADD);
            int solution = Integer.parseInt(equationParts[1]);
            int operatorCount = (equationBody.length() - 1) / 2;

            //Multithread solution
            verifierExecutorService.submit(new VerifierRunnable(localListener, equationBody, solution, operatorCount, Operators.OPERATOR_ADD, Operators.OPERATOR_SUBTRACT));
            verifierExecutorService.submit(new VerifierRunnable(localListener, equationBody, solution, operatorCount, Operators.OPERATOR_SUBTRACT, Operators.OPERATOR_MULTIPLY));
            verifierExecutorService.submit(new VerifierRunnable(localListener, equationBody, solution, operatorCount, Operators.OPERATOR_MULTIPLY, Operators.OPERATOR_DIVIDE));
            verifierExecutorService.submit(new VerifierRunnable(localListener, equationBody, solution, operatorCount, Operators.OPERATOR_DIVIDE, Operators.OPERATOR_ADD));

            verifierExecutorService.shutdown();
            if(verifierExecutorService.awaitTermination(5, TimeUnit.MINUTES)) {
                //Verification completed successfully
                System.out.println("Verification complete (" + allSolutions.size() + "): ");
                for(String s : allSolutions)
                    System.out.println(s);
                return allSolutions.size() == 1;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static class VerifierRunnable implements Runnable {

        private final VerifierActionResultListener listener;

        private String equationBody;
        private final int solution, operatorCount;
        private final String startOperatorConfiguration;
        private final String endOperatorConfiguration;

        private final ArrayList<String> solutions = new ArrayList<>();

        public VerifierRunnable(VerifierActionResultListener listener, String equationBody, int solution, int operatorCount, String startOperatorConfiguration, String endOperatorConfiguration) {
            this.listener = listener;
            this.equationBody = equationBody;
            this.solution = solution;
            this.operatorCount = operatorCount;
            this.startOperatorConfiguration = startOperatorConfiguration;
            this.endOperatorConfiguration = endOperatorConfiguration;
        }

        @Override
        public void run() {
            try {
                int lastOperatorIndex = Utils.getLastOperatorIndex(equationBody, Operators.OPERATOR_LIST);
                equationBody = equationBody.substring(0, lastOperatorIndex) + startOperatorConfiguration + equationBody.substring(lastOperatorIndex + 1);

                while (equationBody != null && solutions.size() < 2) {
                    if (EquationCalculator.calculate(equationBody) == solution) {
                        System.out.println("Solution found");
                        solutions.add(equationBody);
                        listener.onSolutionFound(equationBody);
                    }

                    System.out.println("Checking equation: " + equationBody);

                    equationBody = iterateOperators(equationBody, 0, operatorCount, endOperatorConfiguration);
                }

                System.out.println("Solutions found: " + solutions.size());

                listener.onVerificationComplete(solutions);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static String iterateOperators(String equation, int index, int maxOperators, String endConfiguration) {
        String currentOperator = extractOperator(equation, index);
        String newOperator = getDescendantOperator(currentOperator);

        equation = equation.substring(0, index * 2 + 1) + newOperator + equation.substring(index * 2 + 2);

        if(!validEquation(equation, maxOperators) || !EquationCalculator.calculatable(equation)) return iterateOperators(equation, index, maxOperators, endConfiguration);

        if(Objects.equals(newOperator, Operators.OPERATOR_ADD)) {
            if(index < maxOperators - 1) {
                if(extractOperator(equation, maxOperators - 1).equals(endConfiguration)) return null;
                else return iterateOperators(equation, index + 1, maxOperators, endConfiguration);
            } else {
                return null;
            }
        }

        return equation;
    }

    private static boolean validEquation(String equation, int operatorCount) {
        //Statische Regeln
        int OPERATOR_COUNT_THRESHOLD = 4; //Maximale Anzahl an gleichen aufeinanderfolgenden Operatoren

        int lastOperatorCount = 0;
        String lastOperator = "";
        for(int i = 0; i < operatorCount; i++) {
            String operator = extractOperator(equation, i);

            //Überprüfen der Berechenbarkeit der Gleichung (Ergebnisse natürliche Zahlen)
            String node = extractNode(equation, i * 2 + 1);
            int x = Integer.parseInt(node.substring(0, 1));
            int y = Integer.parseInt(node.substring(2, 3));

            //Überprüfung der kritischen Operatoren
            switch(operator) {
                case Operators.OPERATOR_SUBTRACT:
                    if(y >= x) return false;
                    break;
                case Operators.OPERATOR_DIVIDE:
                    if(x % y != 0) return false;
                    break;
            }
        }

        return true;
    }

    private static String getDescendantOperator(String operator) {
        int operatorIndex = Operators.OPERATOR_LIST.indexOf(operator) + 1;
        return Operators.OPERATOR_LIST.get(operatorIndex > Operators.OPERATOR_LIST.size() - 1 ? 0 : operatorIndex);
    }

    private static String extractOperator(String equation, int index) {
        return equation.substring(index * 2 + 1, index * 2 + 2);
    }

    private static String extractNode(String equation, int operatorIndex) {
        return equation.substring(operatorIndex - 1, operatorIndex + 2);
    }

}