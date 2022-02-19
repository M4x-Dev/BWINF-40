package numbers;

import utils.Utils;

import java.util.ArrayList;
import java.util.Random;

public class NumberGenerator {

    private static final Random instanceRandom = new Random();

    public enum GeneratorMode {
        Even,
        Odd,
        Ignore
    }

    public static int generateNumber(int lower, int upper, GeneratorMode mode) {
        return generateNumber(lower, upper, -1, mode);
    }

    private static int generateNumberWithWeights(int lower, int upper) {
        lower = lower <= 1 ? 2 : lower;
        ArrayList<Integer> numberPool = new ArrayList<>();
        if(lower == upper) {
            numberPool.add(lower);
            numberPool.add(upper);
        }

        float weightFactor = 2.3f;
        for(int i = lower; i < upper; i++) {
            for(int j = 0; j < i * weightFactor; j++) {
                numberPool.add(i);
            }
        }

        return numberPool.get(instanceRandom.nextInt(numberPool.size()));
    }

    public static int generateNumberGeneric(int lower, int upper) {
        int range = upper - lower;
        return instanceRandom.nextInt(range > 0 ? range : 1) + lower;
    }

    public static int generateNumber(int lower, int upper, int exclude, GeneratorMode mode) {
        int range = upper - lower;
        //int number = instanceRandom.nextInt(range > 0 ? range : 1) + lower;
        int number = generateNumberWithWeights(lower, upper);

        if(mode.equals(GeneratorMode.Ignore)) {
            if(exclude == -1) return number;
            else return number - 1 >= lower ? number - 1 : number + 1 < upper ? number + 1 : number;
        }

        if(mode.equals(GeneratorMode.Even)) {
            if(Utils.isEven(number)) return number;

            if(number - 1 < lower) number = number + 1;
            else number = number - 1;

            if(exclude != -1) return number - 2 >= lower ? number - 2 : number + 2 < upper ? number + 2 : number;
            else return number;
        } else if(mode.equals(GeneratorMode.Odd)) {
            if(!Utils.isEven(number)) return number;

            if(number - 1 < lower) number = number + 1;
            else number = number - 1;

            if(exclude != -1) return number - 2 >= lower ? number - 2 : number + 2 < upper ? number + 2 : number;
            else return number;
        }

        return number;
    }

}
