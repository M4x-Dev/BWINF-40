package equations;

import utils.Operators;

public class ClusteredEquationGenerator {

    public String generateFifteen() {
        EquationGenerator clusterGenerator = new EquationGenerator();
        EquationVerifier clusterVerifier = new EquationVerifier();

        String firstPart = clusterGenerator.generate(4, 10);
        String secondPart = clusterGenerator.generate(5, 10);
        String thirdPart = clusterGenerator.generate(4, 10);

        System.out.println("--- Part generation complete ---");
        System.out.println(firstPart);
        System.out.println(secondPart);
        System.out.println(thirdPart);

        boolean firstPartValid = clusterVerifier.verifyMultithread(clusterGenerator.hideSolution(firstPart), firstPart);
        firstPart = firstPart.split(" = ")[0];

        boolean secondPartValid = clusterVerifier.verifyMultithread(clusterGenerator.hideSolution(secondPart), secondPart);
        secondPart = secondPart.split(" = ")[0];

        boolean thirdPartValid = clusterVerifier.verifyMultithread(clusterGenerator.hideSolution(thirdPart), thirdPart);
        thirdPart = thirdPart.split(" = ")[0];

        System.out.println("--- Part validation complete ---");
        System.out.println(firstPartValid);
        System.out.println(secondPartValid);
        System.out.println(thirdPartValid);

        String finalEquation = firstPart + Operators.OPERATOR_ADD + secondPart + Operators.OPERATOR_ADD + thirdPart;
        boolean calculatable = EquationCalculator.calculatable(finalEquation);
        if(!calculatable) System.err.println("Equation is not valid");
        finalEquation += " = " + EquationCalculator.calculate(finalEquation);
        System.out.println("Clustered equation: " + finalEquation);

        boolean unique = clusterVerifier.verifyMultithread(clusterGenerator.hideSolution(finalEquation), finalEquation);
        if(!unique) System.err.println("Equation is not unique");

        return finalEquation;
    }

}
