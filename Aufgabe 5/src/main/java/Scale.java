import java.util.ArrayList;

/**
 * Klasse, welche eine Marktwaage, wie in der Aufgabenstellung beschrieben, darstellt.
 */
public class Scale {

    public final ArrayList<Integer> leftWeights = new ArrayList<>();
    public final ArrayList<Integer> rightWeights = new ArrayList<>();

    private ScaleState bestState;

    public ScaleState balance(int initialWeight, ArrayList<Integer> availableWeights) {
        leftWeights.add(initialWeight);
        return balanceInternal(initialWeight, initialWeight, availableWeights);
    }

    public ScaleState balanceInternal(int initialWeight, int lastDifference, ArrayList<Integer> availableWeights) {
        int difference = getPlatformDifference();

        System.out.println(leftWeights + " --- " + rightWeights);

        if(Math.abs(difference) > Math.abs(lastDifference) && bestState.rightWeights().size() > 0) return bestState;
        else bestState = new ScaleState(new ArrayList<>(leftWeights), new ArrayList<>(rightWeights), false);

        if(difference > 0) {
            //Rechte Seite ist schwerer
            int nearestWeightAdd = getNearestWeight(availableWeights, difference);
            int nearestWeightRemove = getNearestWeight(rightWeights, difference);

            if(isAdditionBetter(nearestWeightAdd, nearestWeightRemove, difference)) {
                if(nearestWeightAdd != -1) {
                    availableWeights.remove((Integer)nearestWeightAdd);
                } else {
                    nearestWeightAdd = getNearestWeight(rightWeights, difference);
                    rightWeights.remove((Integer)nearestWeightAdd);
                }

                leftWeights.add(nearestWeightAdd);
            } else {
                rightWeights.remove((Integer)nearestWeightRemove);
                leftWeights.add(nearestWeightRemove);
            }

            return balanceInternal(initialWeight, difference, availableWeights);
        } else if(difference < 0) {
            //Links Seite ist schwerer
            int nearestWeightAdd = getNearestWeight(availableWeights, difference);
            int nearestWeightRemove = getNearestWeight(leftWeights, difference);

            if(isAdditionBetter(nearestWeightAdd, nearestWeightRemove, difference) || nearestWeightRemove == initialWeight) {
                if(nearestWeightAdd != -1) {
                    availableWeights.remove((Integer)nearestWeightAdd);
                } else if(nearestWeightRemove != initialWeight) {
                    nearestWeightAdd = getNearestWeight(leftWeights, difference);
                    leftWeights.remove((Integer)nearestWeightAdd);
                }

                rightWeights.add(nearestWeightAdd);
            } else {
                leftWeights.remove((Integer)nearestWeightRemove);
                rightWeights.add(nearestWeightRemove);
            }

            return balanceInternal(initialWeight, difference, availableWeights);
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
        return nearestWeight - difference < alternative - difference;
    }

    public int getPlatformDifference() {
        return sumIntegerList(rightWeights) - sumIntegerList(leftWeights);
    }

    public static int sumIntegerList(ArrayList<Integer> list) {
        int sum = 0;

        for(Integer num : list)
            sum += num;

        return sum;
    }

    public record ScaleState(ArrayList<Integer> leftWeights, ArrayList<Integer> rightWeights, boolean done) { }

}