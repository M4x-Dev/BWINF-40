import java.util.ArrayList;

/**
 * Klasse, welche eine Marktwaage, wie in der Aufgabenstellung beschrieben, darstellt.
 */
public class Scale {

    private static final int NEGATIVE_RECURSIONS_MAX = 1000;

    public final ArrayList<Integer> leftWeights = new ArrayList<>(); //Linke Seite der Waage
    public final ArrayList<Integer> rightWeights = new ArrayList<>(); //Rechte Seite der Waage

    private ScaleState bestState; //Zustand der Waage, welcher am nächsten an das Zielgewicht herankam

    public ScaleState balance(int initialWeight, ArrayList<Integer> availableWeights) {
        System.out.println();
        return balanceInternal(initialWeight, initialWeight, availableWeights, 0).finalize(initialWeight); //Ausbalancieren der Waage
    }

    public ScaleState balanceInternal(int initialWeight, int lastDifference, ArrayList<Integer> availableWeights, int negativeRecursions) {
        int difference = getPlatformDifference(initialWeight);

        System.out.println(leftWeights + " --- " + rightWeights + " | " + difference + "; last: " + lastDifference);

        if(bestState == null || (Math.abs(difference) <= Math.abs(bestState.difference(initialWeight)))) bestState = new ScaleState(new ArrayList<>(leftWeights), new ArrayList<>(rightWeights), false);
        else negativeRecursions++;

        if(negativeRecursions > NEGATIVE_RECURSIONS_MAX) return bestState;

        if(difference > 0) {
            //Rechte Seite ist schwerer
            System.out.println("Right side is heavier");
            int nearestWeightAdd = getNearestWeight(availableWeights, difference);
            int nearestWeightRemove = getNearestWeight(rightWeights, difference);

            if(isAdditionBetter(nearestWeightAdd, nearestWeightRemove, difference) || availableWeights.size() > 0) {
                if(nearestWeightAdd != -1) {
                    System.out.println("Adding " + nearestWeightAdd + " from open set");
                    availableWeights.remove((Integer)nearestWeightAdd);
                    leftWeights.add(nearestWeightAdd);
                }
            } else {
                System.out.println("Removing " + nearestWeightRemove + " from right side");
                rightWeights.remove((Integer)nearestWeightRemove);
                if(isSwappingBetter(nearestWeightRemove, difference, initialWeight, false)) leftWeights.add(nearestWeightRemove);
                else availableWeights.add(nearestWeightRemove);
            }

            return balanceInternal(initialWeight, difference, availableWeights, negativeRecursions);
        } else if(difference < 0) {
            //Links Seite ist schwerer
            System.out.println("Left side is heavier");
            int nearestWeightAdd = getNearestWeight(availableWeights, difference);
            int nearestWeightRemove = getNearestWeight(leftWeights, difference);

            if(isAdditionBetter(nearestWeightAdd, nearestWeightRemove, difference) || availableWeights.size() > 0) {
                if(nearestWeightAdd != -1) {
                    System.out.println("Adding " + nearestWeightAdd + " from open set");
                    availableWeights.remove((Integer)nearestWeightAdd);
                    rightWeights.add(nearestWeightAdd);
                }
            } else {
                System.out.println("Removing " + nearestWeightRemove + " from left side");
                leftWeights.remove((Integer)nearestWeightRemove);
                if(isSwappingBetter(nearestWeightRemove, difference, initialWeight, true)) rightWeights.add(nearestWeightRemove);
                else availableWeights.add(nearestWeightRemove);
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

    public boolean isAdditionBetter(Integer nearestWeight, Integer alternative, int difference) {
        if(nearestWeight == -1) return false;
        if(alternative == -1) return true;
        return nearestWeight - difference < alternative - difference;
    }

    public boolean isSwappingBetter(Integer remove, int difference, int targetWeight, boolean left) {
        ArrayList<Integer> dummyLeftWeights = new ArrayList<>(leftWeights);
        ArrayList<Integer> dummyRightWeights = new ArrayList<>(rightWeights);

        if(left) dummyLeftWeights.remove(remove);
        else dummyRightWeights.remove(remove);
        int differenceWithoutAdding = getPlatformDifference(dummyLeftWeights, dummyRightWeights, targetWeight);

        if(left) dummyRightWeights.add(remove);
        else dummyLeftWeights.add(remove);
        int differenceWithAdding = getPlatformDifference(dummyLeftWeights, dummyRightWeights, targetWeight);

        return Math.abs(differenceWithAdding) < Math.abs(differenceWithoutAdding);
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

}