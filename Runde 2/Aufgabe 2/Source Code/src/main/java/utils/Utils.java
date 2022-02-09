package utils;

import java.util.ArrayList;

public class Utils {

    public static boolean isEven(int num) {
        return num % 2 == 0;
    }

    public static boolean containsAny(String check, ArrayList<String> contains) {
        for (String contain : contains) {
            if(check.contains(contain))
                return true;
        }

        return false;
    }

    public static int getLastOperatorIndex(String equation) {
        int lastOperatorIndex = -1;
        for(String operator : Constants.OPERATOR_HIERARCHY) {
            int index = equation.lastIndexOf(operator);
            if(index > lastOperatorIndex) lastOperatorIndex = index;
        }
        return lastOperatorIndex;
    }

    public static int getNextOperatorIndex(String equation, int start, boolean forward) {
        try {
            for(int i = start + (forward ? 1 : -1); forward ? i < equation.length() - 1 : i >= 0; i += forward ? 1 : -1) {
                if(Constants.OPERATOR_HIERARCHY.contains(equation.substring(i, i + 1)))
                    return i;
            }
        } catch (Exception e) {
            return forward ? equation.length() - 1 : -1;
        }

        return forward ? equation.length() - 1 : -1;
    }

    public static int extractLastNumber(String equation) {
        return Integer.parseInt(equation.substring(getLastOperatorIndex(equation) + 1));
    }

}
