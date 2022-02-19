package statistics;

import equations.EquationGenerator;
import equations.EquationVerifier;
import numbers.OperatorGenerator;

public class OperatorWeightOptimization {

    private static final int STEP_SIZE = 1;

    private int additionWeight;
    private int subtractionWeight;
    private int multiplicationWeight;
    private int divisionWeight;

    private final EquationGenerator generator = new EquationGenerator();
    private final EquationVerifier solver = new EquationVerifier();

    private float lastSolutionsMedian;
    private int tweakingIndex = 0;
    private int lastStep = STEP_SIZE;
    private boolean indexTweaked = false;

    public OperatorWeightOptimization(int startAddWeight, int startSubWeight, int startMultWeight, int startDivWeight) {
        this.additionWeight = startAddWeight;
        this.subtractionWeight = startSubWeight;
        this.multiplicationWeight = startMultWeight;
        this.divisionWeight = startDivWeight;
    }

    public void optimize(int operatorCount, int iterationCount) {
        System.out.println("Starting optimization process");
        boolean optimized = false;
        while(!optimized) {
            float currentSolutionsMedian = evaluateSolutionsMedian(operatorCount, iterationCount);
            float delta = currentSolutionsMedian - lastSolutionsMedian;

            if(currentSolutionsMedian == 1.0) {
                optimized = true;
                break;
            }

            System.out.println("Updating values with delta: " + delta);

            if(delta > 0) {
                updateWeight(tweakingIndex, lastStep);
            }

            if(delta <= 0) {
                if(indexTweaked) {
                    tweakingIndex++;
                    if(tweakingIndex > 3) optimized = true;
                } else {
                    lastStep = -lastStep;
                    updateWeight(tweakingIndex, lastStep);
                    indexTweaked = true;
                }
            }

            OperatorGenerator.WEIGHT_ADDITION = additionWeight;
            OperatorGenerator.WEIGHT_SUBTRACTION = subtractionWeight;
            OperatorGenerator.WEIGHT_MULTIPLICATION = multiplicationWeight;
            OperatorGenerator.WEIGHT_DIVISION = divisionWeight;

            System.out.println("Values have been updated: {" + additionWeight + ", " + subtractionWeight + ", " + multiplicationWeight + ", " + divisionWeight + "}");

            lastSolutionsMedian = currentSolutionsMedian;
        }

        System.out.println("Optimization complete. Final values:");
        System.out.println("Addition weight: " + additionWeight);
        System.out.println("Subtraction weight: " + subtractionWeight);
        System.out.println("Multiplication weight: " + multiplicationWeight);
        System.out.println("Division weight: " + divisionWeight);
        System.out.println("Highest achieved median: " + lastSolutionsMedian);
    }

    private void updateWeight(int index, int value) {
        switch (index) {
            case 0 -> additionWeight += value;
            case 1 -> subtractionWeight += value;
            case 2 -> multiplicationWeight += value;
            case 3 -> divisionWeight += value;
        }
    }

    private float evaluateSolutionsMedian(int operatorCount, int iterationCount) {
        int uniques = 0;
        for(int i = 0; i < iterationCount; i++) {
            System.out.println("Iterating: " + (i + 1));
            String equation = generator.generateRaw(operatorCount);
            if(solver.verifyMultithread(generator.hideSolution(equation), equation))
                uniques++;
        }
        return (float)uniques / iterationCount;
    }

}
