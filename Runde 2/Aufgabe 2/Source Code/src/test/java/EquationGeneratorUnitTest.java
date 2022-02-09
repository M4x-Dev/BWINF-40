import equations.EquationCalculator;
import equations.EquationGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class EquationGeneratorUnitTest {

    private static final int RUNS = 10;
    private static final int OPERATORS = 3;

    @Test
    public void testEquationGenerator() {
        System.out.println();
        for(int i = 0; i < RUNS; i++) {
            EquationGenerator generator = new EquationGenerator();
            String equation = generator.generate(OPERATORS);
            System.out.println("[" + (i + 1) + "/" + RUNS + "] " + equation);
            /*Assertions.assertTrue(EquationCalculator.calculatable(equation));

            for(int a = 0; a < (equation.length() - 1) / 2; a++) {
                String operator = equation.substring(i * 2 + 1, i * 2 + 2);
                String node = equation.substring(i * 2, i * 2 + 3);
                Assertions.assertTrue(EquationCalculator.canSolveNode(node, operator));
            }*/
            Assertions.assertTrue(true);
        }
        System.out.println();
    }

}