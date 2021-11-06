import java.util.ArrayList;

/**
 * Klasse, welche eine Marktwaage, wie in der Aufgabenstellung beschrieben, darstellt.
 */
public class Scale {

    private static final int NEGATIVE_RECURSIONS_MAX = 10;

    public final ArrayList<Integer> leftWeights = new ArrayList<>(); //Linke Seite der Waage
    public final ArrayList<Integer> rightWeights = new ArrayList<>(); //Rechte Seite der Waage

    private ScaleState bestState; //Zustand der Waage, welcher am nächsten an das Zielgewicht herankam

    public ScaleState balance(int initialWeight, ArrayList<Integer> availableWeights) {
        println("");
        return balanceInternal(initialWeight, initialWeight, availableWeights, 0).finalize(initialWeight); //Ausbalancieren der Waage
    }

    public ScaleState balanceInternal(int initialWeight, int lastDifference, ArrayList<Integer> availableWeights, int negativeRecursions) {
        int difference = getPlatformDifference(initialWeight);

        println(leftWeights + " --- " + rightWeights + " | " + difference + "; last: " + lastDifference);

        if(bestState == null || (Math.abs(difference) < Math.abs(bestState.difference(initialWeight)))) bestState = new ScaleState(new ArrayList<>(leftWeights), new ArrayList<>(rightWeights), false);
        else negativeRecursions++;

        if(negativeRecursions > NEGATIVE_RECURSIONS_MAX) return bestState;

        if(difference > 0) {
            //Rechte Seite ist schwerer
            println("Right side is heavier");
            int nearestWeightAdd = getNearestWeight(availableWeights, difference);
            int nearestWeightRemove = getNearestWeight(rightWeights, difference);

            switch (getBestAction(nearestWeightAdd, nearestWeightRemove, initialWeight, false)) {
                case Add -> {
                    println("Adding " + nearestWeightAdd + " from open set");
                    availableWeights.remove((Integer) nearestWeightAdd);
                    leftWeights.add(nearestWeightAdd);
                }
                case Remove -> {
                    println("Removing " + nearestWeightRemove + " from right side");
                    rightWeights.remove((Integer) nearestWeightRemove);
                    availableWeights.add(nearestWeightRemove);
                }
                case Swap -> {
                    println("Swapping " + nearestWeightRemove + " from right side to left side");
                    rightWeights.remove((Integer) nearestWeightRemove);
                    leftWeights.add(nearestWeightRemove);
                }
            }

            return balanceInternal(initialWeight, difference, availableWeights, negativeRecursions);
        } else if(difference < 0) {
            //Links Seite ist schwerer
            println("Left side is heavier");
            int nearestWeightAdd = getNearestWeight(availableWeights, difference);
            int nearestWeightRemove = getNearestWeight(leftWeights, difference);

            switch (getBestAction(nearestWeightAdd, nearestWeightRemove, initialWeight, true)) {
                case Add -> {
                    println("Adding " + nearestWeightAdd + " from open set");
                    availableWeights.remove((Integer)nearestWeightAdd);
                    rightWeights.add(nearestWeightAdd);
                }
                case Remove -> {
                    println("Removing " + nearestWeightRemove + " from left side");
                    leftWeights.remove((Integer)nearestWeightRemove);
                    availableWeights.add(nearestWeightRemove);
                }
                case Swap -> {
                    println("Swapping " + nearestWeightRemove + " from left side to right side");
                    leftWeights.remove((Integer)nearestWeightRemove);
                    rightWeights.add(nearestWeightRemove);
                }
            }

            return balanceInternal(initialWeight, difference, availableWeights, negativeRecursions);
        } else {
            //Gleichgewicht ist erreicht
            return new ScaleState(new ArrayList<>(leftWeights), new ArrayList<>(rightWeights), true);
        }
    }

    public Integer getNearestWeight(ArrayList<Integer> source, int difference) {
        int bestNum = source.size() > 0 ? source.get(0) : -1;
        int bestDiff = source.size() > 0 ? Math.abs(source.get(0) - Math.abs(difference)) : -1;

        for(Integer num : source) {
            int diff = Math.abs(num - Math.abs(difference));
            if(diff < bestDiff) {
                bestNum = num;
                bestDiff = diff;
            }
        }

        return bestNum;
    }

    public enum Action {
        Add,
        Remove,
        Swap
    }

    public Action getBestAction(Integer possibleAdd, Integer possibleRemove, int targetWeight, boolean leftHeavier) {
        ArrayList<Integer> dummyLeftWeights = new ArrayList<>(leftWeights);
        ArrayList<Integer> dummyRightWeights = new ArrayList<>(rightWeights);

        if(leftHeavier) dummyRightWeights.add(possibleAdd);
        else dummyLeftWeights.add(possibleAdd);
        int differenceAdd = possibleAdd != -1 ? Math.abs(getPlatformDifference(dummyLeftWeights, dummyRightWeights, targetWeight)) : 1000;

        dummyLeftWeights = new ArrayList<>(leftWeights);
        dummyRightWeights = new ArrayList<>(rightWeights);

        if(leftHeavier) dummyLeftWeights.remove(possibleRemove);
        else dummyRightWeights.remove(possibleRemove);
        int differenceRemove = possibleRemove != -1 ? Math.abs(getPlatformDifference(dummyLeftWeights, dummyRightWeights, targetWeight)) : 1000;

        if(leftHeavier) dummyRightWeights.add(possibleRemove);
        else dummyLeftWeights.add(possibleRemove);
        int differenceSwap = possibleRemove != -1 ? Math.abs(getPlatformDifference(dummyLeftWeights, dummyRightWeights, targetWeight)) : 1000;

        int bestDifference = differenceAdd;
        if(differenceRemove < bestDifference) bestDifference = differenceRemove;
        if(differenceSwap < bestDifference) bestDifference = differenceSwap;

        if(possibleRemove == -1) return Action.Add;
        if(possibleAdd == -1) bestDifference = differenceRemove;

        if(bestDifference == differenceAdd) return Action.Add;
        else if(bestDifference == differenceRemove) return Action.Remove;
        else return Action.Swap;
    }

    public int getPlatformDifference(int targetWeight) {
        return sumIntegerList(rightWeights) - (sumIntegerList(leftWeights) + targetWeight);
    }

    public int getPlatformDifference(ArrayList<Integer> customLeftWeights, ArrayList<Integer> customRightWeights, int targetWeight) {
        return sumIntegerList(customRightWeights) - (sumIntegerList(customLeftWeights) + targetWeight);
    }

    public static int sumIntegerList(ArrayList<Integer> list) {
        int sum = 0;

        for(Integer num : list)
            sum += num;

        return sum;
    }

    public record ScaleState(ArrayList<Integer> leftWeights, ArrayList<Integer> rightWeights, boolean done) {

        public ScaleState finalize(int targetWeight) {
            leftWeights.add(0, targetWeight);
            return this;
        }

        public int difference(int targetWeight) {
            return sumIntegerList(rightWeights) - (sumIntegerList(leftWeights) + targetWeight);
        }

    }

    public static void println(String line) {
        if(Main.DEBUG_MODE) System.out.println(line);
    }

}