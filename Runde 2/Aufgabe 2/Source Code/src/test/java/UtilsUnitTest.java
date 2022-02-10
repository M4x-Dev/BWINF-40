import org.junit.jupiter.api.Test;
import utils.Operators;
import utils.Utils;

public class UtilsUnitTest {

    @Test
    public void testOperatorResolving() {
        String equation = "1+1";

        int operatorIndex = 0;
        System.out.println("Tracking forward");
        while((operatorIndex = Utils.getNextOperatorIndex(equation, operatorIndex, true)) < equation.length() - 1) {
            System.out.println("Operator index: " + operatorIndex);
            System.out.println("Operator: " + equation.charAt(operatorIndex));
        }

        operatorIndex = equation.length() - 1;
        System.out.println("Tracking backward");
        while((operatorIndex = Utils.getNextOperatorIndex(equation, operatorIndex, false)) >= 0) {
            System.out.println("Operator index: " + operatorIndex);
            System.out.println("Operator: " + equation.charAt(operatorIndex));
        }

        int lastOperatorIndex = Utils.getLastOperatorIndex(equation, Operators.OPERATOR_HIERARCHY);
        System.out.println(lastOperatorIndex);
    }

}