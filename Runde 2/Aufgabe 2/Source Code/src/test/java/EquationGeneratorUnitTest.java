import equations.EquationCalculator;
import equations.EquationGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class EquationGeneratorUnitTest {

    private static final int RUNS = 100;
    private static final int OPERATORS = 15;

    @Test
    public void testEquationGenerator() {
        System.out.println();
        for(int i = 0; i < RUNS; i++) {
            EquationGenerator generator = new EquationGenerator();
            String equation = generator.generateRaw(OPERATORS).split(" = ")[0];
            System.out.println("[" + (i + 1) + "/" + RUNS + "] " + equation);
            Assertions.assertTrue(EquationCalculator.calculatable(equation));

            for(int a = 0; a < (equation.length() - 1) / 2; a++) {
                String operator = equation.substring(a * 2 + 1, a * 2 + 2);
                String node = equation.substring(a * 2, a * 2 + 3);
                Assertions.assertTrue(EquationCalculator.canSolveNode(node, operator));
            }
        }
        System.out.println();
    }

}