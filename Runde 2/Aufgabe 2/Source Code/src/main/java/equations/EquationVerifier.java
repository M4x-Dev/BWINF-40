package equations;

import utils.DebugUtils;
import utils.Operators;
import utils.Utils;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class EquationVerifier {

    public interface VerifierActionResultListener {
        void onVerificationComplete(ArrayList<String> solutions);
    }

    private static final AtomicInteger SolutionCounter = new AtomicInteger(1);
    private static final AtomicInteger IterationCounter = new AtomicInteger(0);
    private static int theoreticalIterations;
    private static int theoreticalSinglePercentage;
    private final ArrayList<String> allSolutions = new ArrayList<>();

    private VerifierActionResultListener localListener;
    private ExecutorService verifierExecutorService = Executors.newFixedThreadPool(4);

    public boolean verifySinglethread(String equation) {
        try {
            allSolutions.clear();
            allSolutions.add(equation);
            SolutionCounter.set(1);
            localListener = solutions -> solutions.forEach(solution -> {
                if(!allSolutions.contains(solution)) {
                    allSolutions.add(solution);
                }
            });

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

    public boolean verifyMultithread(String equation, String originalSolution) {
        try {
            long startTime = System.currentTimeMillis();
            allSolutions.clear();
            allSolutions.add(originalSolution.replace(" ", "").split("=")[0]);
            SolutionCounter.set(1);
            verifierExecutorService = Executors.newFixedThreadPool(4);
            localListener = solutions -> solutions.forEach(solution -> {
                if(!allSolutions.contains(solution))
                    allSolutions.add(solution);
            });

            String[] equationParts = equation.split(" = ");
            String equationBody = equationParts[0].replaceAll(" " + Operators.OPERATOR_PLACEHOLDER + " ", Operators.OPERATOR_ADD);
            int solution = Integer.parseInt(equationParts[1]);
            int operatorCount = (equationBody.length() - 1) / 2;
            theoreticalIterations = (int)Math.pow(4, operatorCount);
            theoreticalSinglePercentage = theoreticalIterations / 100;

            DebugUtils.println("--- Starting verification ---");
            DebugUtils.println("Theoretical iterations: " + theoreticalIterations);
            DebugUtils.println("Estimated time: " + Math.round(((double)theoreticalIterations / 400000)) + " seconds");

            //Multithread solution
            verifierExecutorService.submit(new VerifierRunnable(localListener, equationBody, solution, operatorCount, Operators.OPERATOR_ADD, Operators.OPERATOR_SUBTRACT));
            verifierExecutorService.submit(new VerifierRunnable(localListener, equationBody, solution, operatorCount, Operators.OPERATOR_SUBTRACT, Operators.OPERATOR_MULTIPLY));
            verifierExecutorService.submit(new VerifierRunnable(localListener, equationBody, solution, operatorCount, Operators.OPERATOR_MULTIPLY, Operators.OPERATOR_DIVIDE));
            verifierExecutorService.submit(new VerifierRunnable(localListener, equationBody, solution, operatorCount, Operators.OPERATOR_DIVIDE, Operators.OPERATOR_ADD));

            verifierExecutorService.shutdown();
            if(verifierExecutorService.awaitTermination(5, TimeUnit.MINUTES)) {
                //Verification completed successfully
                DebugUtils.println("Verification complete (" + allSolutions.size() + "): ");
                DebugUtils.println("Theoretical combination count: " + Math.pow(4, operatorCount));
                DebugUtils.println("Total time needed: " + (System.currentTimeMillis() - startTime) + "ms");
                DebugUtils.println("Total iterations: " + IterationCounter.get());
                for(String s : allSolutions)
                    DebugUtils.println(s);
                return allSolutions.size() == 1;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public int solveMultithread(String equation, String originalSolution) {
        try {
            long startTime = System.currentTimeMillis();
            allSolutions.clear();
            SolutionCounter.set(0);
            IterationCounter.set(0);
            verifierExecutorService = Executors.newFixedThreadPool(4);
            localListener = solutions -> solutions.forEach(solution -> {
                if (!allSolutions.contains(solution))
                    allSolutions.add(solution);
            });

            String[] equationParts = equation.split(" = ");
            String equationBody = equationParts[0].replaceAll(" " + Operators.OPERATOR_PLACEHOLDER + " ", Operators.OPERATOR_ADD);
            int solution = Integer.parseInt(equationParts[1]);
            int operatorCount = (equationBody.length() - 1) / 2;
            theoreticalIterations = (int) Math.pow(4, operatorCount);
            theoreticalSinglePercentage = theoreticalIterations / 100;

            System.out.println("--- Starting Solving ---");
            System.out.println("Theoretical iterations: " + theoreticalIterations);
            System.out.println("Estimated time: " + Math.round(((double) theoreticalIterations / 400000)) + " seconds");

            //Multithread solution
            verifierExecutorService.submit(new SolverRunnable(localListener, equationBody, solution, operatorCount, Operators.OPERATOR_ADD, Operators.OPERATOR_SUBTRACT));
            verifierExecutorService.submit(new SolverRunnable(localListener, equationBody, solution, operatorCount, Operators.OPERATOR_SUBTRACT, Operators.OPERATOR_MULTIPLY));
            verifierExecutorService.submit(new SolverRunnable(localListener, equationBody, solution, operatorCount, Operators.OPERATOR_MULTIPLY, Operators.OPERATOR_DIVIDE));
            verifierExecutorService.submit(new SolverRunnable(localListener, equationBody, solution, operatorCount, Operators.OPERATOR_DIVIDE, Operators.OPERATOR_ADD));

            verifierExecutorService.shutdown();
            if (verifierExecutorService.awaitTermination(5, TimeUnit.MINUTES)) {
                //Verification completed successfully
                System.out.println("Solving complete (" + allSolutions.size() + "): ");
                System.out.println("Theoretical combination count: " + Math.pow(4, operatorCount));
                System.out.println("Total time needed: " + (System.currentTimeMillis() - startTime) + "ms");
                System.out.println("Total iterations: " + IterationCounter.get());
                for (String s : allSolutions)
                    System.out.println(s);
                return allSolutions.size();
            }
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static class VerifierRunnable implements Runnable {

        protected final VerifierActionResultListener listener;

        protected String equationBody;
        protected final int solution, operatorCount;
        protected final String startOperatorConfiguration;
        protected final String endOperatorConfiguration;

        protected final ArrayList<String> solutions = new ArrayList<>();

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

                while (equationBody != null && SolutionCounter.get() < 2) {
                    IterationCounter.incrementAndGet();
                    if(IterationCounter.get() % theoreticalSinglePercentage == 0) {
                        String pattern = "\r|<bar>| (<percentage>)";
                        int completed = IterationCounter.get() / theoreticalSinglePercentage;
                        StringBuilder constructedBar = new StringBuilder();
                        for(int i = 0; i < 100; i++) {
                            if(i > completed) constructedBar.append(" ");
                            else if(i < completed) constructedBar.append("=");
                            else constructedBar.append(">");
                        }
                        pattern = pattern.replace("<bar>", constructedBar.toString())
                                .replace("<percentage>", completed + "%");

                        System.out.print(pattern);
                    }
                    if (validEquation(equationBody, operatorCount) && EquationCalculator.calculatable(equationBody) && EquationCalculator.calculate(equationBody) == solution) {
                        if(!solutions.contains(equationBody)) {
                            System.out.println("\nSolution found: " + equationBody);
                            solutions.add(equationBody);
                            SolutionCounter.incrementAndGet();
                        }
                    }

                    equationBody = iterateOperators(equationBody, 0, operatorCount, endOperatorConfiguration);
                }

                listener.onVerificationComplete(solutions);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static class SolverRunnable extends VerifierRunnable {

        public SolverRunnable(VerifierActionResultListener listener, String equationBody, int solution, int operatorCount, String startOperatorConfiguration, String endOperatorConfiguration) {
            super(listener, equationBody, solution, operatorCount, startOperatorConfiguration, endOperatorConfiguration);
        }

        @Override
        public void run() {
            try {
                int lastOperatorIndex = Utils.getLastOperatorIndex(equationBody, Operators.OPERATOR_LIST);
                equationBody = equationBody.substring(0, lastOperatorIndex) + startOperatorConfiguration + equationBody.substring(lastOperatorIndex + 1);

                while (equationBody != null) {
                    IterationCounter.incrementAndGet();
                    if(IterationCounter.get() % theoreticalSinglePercentage == 0) {
                        String pattern = "\r|<bar>| (<percentage>)";
                        int completed = IterationCounter.get() / theoreticalSinglePercentage;
                        StringBuilder constructedBar = new StringBuilder();
                        for(int i = 0; i < 100; i++) {
                            if(i > completed) constructedBar.append(" ");
                            else if(i < completed) constructedBar.append("=");
                            else constructedBar.append(">");
                        }
                        pattern = pattern.replace("<bar>", constructedBar.toString())
                                .replace("<percentage>", completed + "%");

                        System.out.print(pattern);
                    }
                    if (validEquation(equationBody, operatorCount) && EquationCalculator.calculatable(equationBody) && EquationCalculator.calculate(equationBody) == solution) {
                        if(!solutions.contains(equationBody)){
                            solutions.add(equationBody);
                            SolutionCounter.incrementAndGet();
                        }
                    }

                    equationBody = iterateOperators(equationBody, 0, operatorCount, endOperatorConfiguration);
                }

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

        if(Objects.equals(newOperator, Operators.OPERATOR_ADD)) {
            if(index == maxOperators - 2) {
                if(getDescendantOperator(extractOperator(equation, maxOperators - 1)).equals(endConfiguration))
                    return null;
            }
            if(index < maxOperators - 1) {
                return iterateOperators(equation, index + 1, maxOperators, endConfiguration);
            }
        }

        return equation;
    }

    private static boolean validEquation(String equation, int operatorCount) {
        String lastOperator = "";
        int lastOperatorCount = 0;
        for(int i = 0; i < operatorCount; i++) {
            String operator = extractOperator(equation, i);
            if(lastOperatorCount > 2) return false;
            if(!lastOperator.equals(operator)) lastOperatorCount = 0;
            lastOperatorCount++;
            lastOperator = operator;

            //??berpr??fen der Berechenbarkeit der Gleichung (Ergebnisse nat??rliche Zahlen)
            String node = extractNode(equation, i * 2 + 1);
            int x = Integer.parseInt(node.substring(0, 1));
            int y = Integer.parseInt(node.substring(2, 3));

            //??berpr??fung der kritischen Operatoren
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