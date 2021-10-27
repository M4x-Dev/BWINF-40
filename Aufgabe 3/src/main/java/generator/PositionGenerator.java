package generator;

import java.util.ArrayList;
import java.util.Random;

public class PositionGenerator {

    private static final int MAX_ATTEMPTS_GENERATOR = 3;

    private final Random instanceRandom;
    private final ArrayList<Integer> exclusions = new ArrayList<>();

    public PositionGenerator(Random instanceRandom) {
        this.instanceRandom = instanceRandom;
    }

    public void addExclusion(Integer exclusion) {
        this.exclusions.add(exclusion);
    }

    public void clearExclusions() {
        this.exclusions.clear();
    }

    public int generate(int bounds) {
        //System.out.println("Generating number with bounds: " + bounds);
        return generateNumberInternal(bounds, 0);
    }

    private int generateNumberInternal(int bounds, int attempt) {
        int number = instanceRandom.nextInt(bounds);
        if(attempt > MAX_ATTEMPTS_GENERATOR) return number;
        return exclusions.contains(number) ? generateNumberInternal(bounds, attempt + 1) : number;
    }

}