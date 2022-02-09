package utils;

import java.util.ArrayList;

public class Constants {

    public static final String OPERATOR_ADD = "+";
    public static final String OPERATOR_SUBTRACT = "-";
    public static final String OPERATOR_MULTIPLY = "*";
    public static final String OPERATOR_DIVIDE = ":";
    public static final String OPERATOR_PLACEHOLDER = "o";

    public static String invertOperator(String operator) {
        return switch(operator) {
            case OPERATOR_ADD -> OPERATOR_SUBTRACT;
            case OPERATOR_MULTIPLY -> OPERATOR_DIVIDE;
            case OPERATOR_DIVIDE -> OPERATOR_MULTIPLY;
            default -> OPERATOR_ADD;
        };
    }

    public static ArrayList<String> excludeOperators(String... exclude) {
        ArrayList<String> defaultOperators = new ArrayList<>(OPERATOR_HIERARCHY);
        for (String operator : exclude) defaultOperators.remove(operator);
        return defaultOperators;
    }

    public static final ArrayList<String> LINE_OPERATORS = new ArrayList<>() {{
        add(OPERATOR_ADD);
        add(OPERATOR_SUBTRACT);
    }};
    public static final ArrayList<String> POINT_OPERATORS = new ArrayList<>() {{
        add(OPERATOR_MULTIPLY);
        add(OPERATOR_DIVIDE);
    }};

    public static final ArrayList<String> OPERATOR_LIST = new ArrayList<>() {{
        add(Constants.OPERATOR_ADD);
        add(Constants.OPERATOR_SUBTRACT);
        add(Constants.OPERATOR_MULTIPLY);
        add(Constants.OPERATOR_DIVIDE);
    }};
    public static final ArrayList<String> OPERATOR_HIERARCHY = new ArrayList<>() {{
        add(Constants.OPERATOR_MULTIPLY);
        add(Constants.OPERATOR_DIVIDE);
        add(Constants.OPERATOR_ADD);
        add(Constants.OPERATOR_SUBTRACT);
    }};

}