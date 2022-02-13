package equations;

import utils.DebugUtils;
import utils.Operators;
import utils.Utils;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Klasse, welche mathematische Gleichungen nach den folgenden Regeln berechnen kann:
 * <br>1. Natürliche Zahlen größer 0 als Ergebnisse/Zwischenergebnisse
 * <br>2. Punkt- vor Strichrechnung
 * <br>3. Linksassoziatives Berechnen bei gleichrangigen Operatoren
 */
public class EquationCalculator {

    /**
     * Funktion, welche das Ergebnis einer gegebenen mathematischen Gleichung berechnet.
     * Diese Funktion verwendet die Funktion {@link #transformEquation}, um das Ergebnis zu berechnen.
     *
     * @param equation Mathematische Gleichung, welche berechnet werden soll.
     *
     * @return Gibt das Ergebnis der Gleichung zurück (natürliche Zahl größer 0).
     */
    public static int calculate(String equation) {
        DebugUtils.println("Calculating: " + equation);
        return Integer.parseInt(transformEquation(equation, Operators.OPERATOR_HIERARCHY));
    }

    public static boolean calculatable(String equation) {
        //Entfernen aller Leerzeichen
        equation = equation.replaceAll(" ", "");

        DebugUtils.println("Testing equation: " + equation);

        //Hauptschleife des Algorithmus
        while(Utils.containsAny(equation, Operators.OPERATOR_LIST) && equation.length() > 2) {
            for(String operator : Operators.OPERATOR_HIERARCHY) {
                if (equation.contains(operator)) {
                    operator = checkInverseOperator(equation, operator);

                    if(canSolveOperator(equation, operator)) {
                        equation = solveOperator(equation, operator);
                        DebugUtils.println("Partially solved equation: " + equation);
                        break;
                    } else {
                        DebugUtils.println("Check failed");
                        return false;
                    }
                }
            }
        }

        return true;
    }

    /**
     * Funktion, welche eine gegebene mathematische Gleichung transformiert.
     * Dabei berechnet die Funktion die Teilergebnisse der einzelnen Terme und fügt das Ergebnis in die Gleichung ein, bis nur noch das Ergebnis übrig bleibt.
     * Dabei werden nur Operatoren berechnet, welche sich in dem replaceOperators-Parameter befinden.
     * Dadurch kann die Funktion eine Gleichung in eine Summe umwandeln, wenn sich die Addition nicht in der Liste befindet.
     *
     * <br><br>Beispiel zum Vorgehen des Algorithmus (replaceOperators beeinhaltet alle Operatoren):
     * <br>Ausgangsgleichung: 1 + 2 * 3 - 4
     * <br>1. Schritt: 1 + 6 - 4
     * <br>2. Schritt: 7 - 4
     * <br>3. Schritt: 3
     *
     * @param equation Mathematische Gleichung, welche transformiert werden soll.
     * @param replaceOperators Operatoren, welche durch den Algorithmus ersetzt/berechnet werden sollen.
     *
     * @return Gibt die transformierte Gleichung zurück.
     */
    public static String transformEquation(String equation, ArrayList<String> replaceOperators) {
        //Entfernen aller Leerzeichen
        equation = equation.replaceAll(" ", "");

        //Hauptschleife des Algorithmus
        while(Utils.containsAny(equation, replaceOperators) && equation.length() > 2) {
            for(String operator : replaceOperators) {
                if(equation.contains(operator)) {
                    //Berechnung eines Teiltermes
                    DebugUtils.println("Transforming: " + equation);
                    equation = solveOperator(equation, checkInverseOperator(equation, operator));
                    break;
                }
            }
        }

        //Rückgabe der transformierten Gleichung
        return equation;
    }

    /**
     * Funktion, welche eine einzelne Operation einer gegebenen mathematischen Gleichung transformiert.
     * Dabei ermittelt die Funktion den Teilterm des gegebenen Operatoren und berechnet dessen Ergebnis.
     * Anschließend wird der Term in der ursprünglichen Gleichung ersetzt und die neue Gleichung wird zurückgegeben.
     *
     * @param equation
     * @param operator
     * @return
     */
    public static String solveOperator(String equation, String operator) {
        int operatorIndex = equation.indexOf(operator);
        int previousOperator = Utils.getNextOperatorIndex(equation, operatorIndex, false);
        int nextOperator = Utils.getNextOperatorIndex(equation, operatorIndex, true);

        DebugUtils.println("Current equation: " + equation);

        String node = equation.length() > 3 ? equation.substring(previousOperator == 0 ? previousOperator : previousOperator + 1, nextOperator == equation.length() - 1 ? nextOperator + 1 : nextOperator) : equation;

        DebugUtils.println("Current node: " + node);

        String newEquation = equation.replaceFirst("\\b(" + Pattern.quote(node) + ")\\b", solveNode(node, operator));
        DebugUtils.println("New equation: " + newEquation);
        return newEquation;
    }

    public static boolean canSolveOperator(String equation, String operator) {
        int operatorIndex = equation.indexOf(operator);
        int previousOperator = Utils.getNextOperatorIndex(equation, operatorIndex, false);
        int nextOperator = Utils.getNextOperatorIndex(equation, operatorIndex, true);

        String node = equation.length() > 3 ? equation.substring(previousOperator == 0 ? previousOperator : previousOperator + 1, nextOperator == equation.length() - 1 ? nextOperator + 1 : nextOperator) : equation;
        return canSolveNode(node, operator);
    }

    public static String solveNode(String node, String operator) {
        int result = 0;

        int operatorIndex = node.indexOf(operator);

        DebugUtils.println("Solving node: " + node);
        DebugUtils.println("x: " + node.substring(0, operatorIndex));
        DebugUtils.println("y: " + node.substring(operatorIndex + 1));

        int x = Integer.parseInt(node.substring(0, operatorIndex));
        int y = Integer.parseInt(node.substring(operatorIndex + 1));

        switch (operator) {
            case Operators.OPERATOR_ADD -> result = x + y;
            case Operators.OPERATOR_SUBTRACT -> result = x - y;
            case Operators.OPERATOR_MULTIPLY -> result = x * y;
            case Operators.OPERATOR_DIVIDE -> result = x / y;
        }

        DebugUtils.println("Result of node: " + result);
        return String.valueOf(result);
    }

    public static boolean canSolveNode(String node, String operator) {
        int operatorIndex = node.indexOf(operator);
        int x = Integer.parseInt(node.substring(0, operatorIndex));
        int y = Integer.parseInt(node.substring(operatorIndex + 1));

        DebugUtils.println("Checking node: " + node);

        return switch(operator) {
            case Operators.OPERATOR_SUBTRACT -> x - y > 0;
            case Operators.OPERATOR_DIVIDE -> x % y == 0 && x / y > 0;
            default -> true;
        };
    }

    private static String checkInverseOperator(String equation, String operator) {
        String inverseOperator = Operators.invertOperator(operator);
        int operatorIndex = equation.indexOf(operator);
        int inverseOperatorIndex = equation.indexOf(inverseOperator);
        return operatorIndex < inverseOperatorIndex || inverseOperatorIndex == -1 ? operator : inverseOperator;
    }

}
