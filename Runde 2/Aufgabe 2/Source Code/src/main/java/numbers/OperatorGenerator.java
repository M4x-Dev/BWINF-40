package numbers;

import utils.Operators;

import java.util.ArrayList;
import java.util.Random;

public class OperatorGenerator {

    private static final Random instanceRandom = new Random();

    //public static int WEIGHT_ADDITION = 3;
    //public static int WEIGHT_SUBTRACTION = 7;
    //public static int WEIGHT_MULTIPLICATION = 3;
    //public static int WEIGHT_DIVISION = 7;

    /*public static int WEIGHT_ADDITION = 4;
    public static int WEIGHT_SUBTRACTION = 1;
    public static int WEIGHT_MULTIPLICATION = 6;
    public static int WEIGHT_DIVISION = 2;*/

    public static int WEIGHT_ADDITION = 5;
    public static int WEIGHT_SUBTRACTION = 3;
    public static int WEIGHT_MULTIPLICATION = 10;
    public static int WEIGHT_DIVISION = 1;

    public static String generateOperator(ArrayList<String> operators) {
        ArrayList<String> pool = new ArrayList<>();

        if(operators.contains(Operators.OPERATOR_ADD))
            addOperator(pool, Operators.OPERATOR_ADD, WEIGHT_ADDITION);
        if (operators.contains(Operators.OPERATOR_SUBTRACT))
            addOperator(pool, Operators.OPERATOR_SUBTRACT, WEIGHT_SUBTRACTION);
        if (operators.contains(Operators.OPERATOR_MULTIPLY))
            addOperator(pool, Operators.OPERATOR_MULTIPLY, WEIGHT_MULTIPLICATION);
        if (operators.contains(Operators.OPERATOR_DIVIDE))
            addOperator(pool, Operators.OPERATOR_DIVIDE, WEIGHT_DIVISION);

        return pool.get(instanceRandom.nextInt(pool.size()));
    }

    public static String generateOperatorWithCustomWeights(ArrayList<String> operators, int addWeight, int subWeight, int multWeight, int divWeight) {
        ArrayList<String> pool = new ArrayList<>();

        if(operators.contains(Operators.OPERATOR_ADD))
            addOperator(pool, Operators.OPERATOR_ADD, addWeight);
        else if(operators.contains(Operators.OPERATOR_SUBTRACT))
            addOperator(pool, Operators.OPERATOR_SUBTRACT, subWeight);
        else if(operators.contains(Operators.OPERATOR_MULTIPLY))
            addOperator(pool, Operators.OPERATOR_MULTIPLY, multWeight);
        else if(operators.contains(Operators.OPERATOR_DIVIDE))
            addOperator(pool, Operators.OPERATOR_DIVIDE, divWeight);

        return pool.get(instanceRandom.nextInt(pool.size()));
    }

    private static void addOperator(ArrayList<String> pool, String operator, int weight) {
        for(int i = 0; i <= weight; i++)
            pool.add(operator);
    }

}
