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
        balanceInternal(initialWeight, availableWeights);
        return bestState;
    }

    public boolean balanceInternal(int lastDifference, ArrayList<Integer> availableWeights) {
        int difference = getPlatformDifference();

        /*System.out.println(leftWeights + " --- " + rightWeights);
        System.out.println("Difference: " + difference);*/

        if(Math.abs(difference) > Math.abs(lastDifference)) return false;
        else bestState = new ScaleState(new ArrayList<>(leftWeights), new ArrayList<>(rightWeights), false);

        if(difference > 0) {
            //Rechte Seite ist schwerer
            int nearestWeight = getNearestWeight(availableWeights, difference);

            if(nearestWeight != -1) {
                availableWeights.remove((Integer)nearestWeight);
            } else {
                nearestWeight = getNearestWeight(rightWeights, difference);
                rightWeights.remove((Integer)nearestWeight);
            }

            leftWeights.add(nearestWeight);
            return balanceInternal(difference, availableWeights);
        } else if(difference < 0) {
            //Links Seite ist schwerer
            int nearestWeight = getNearestWeight(availableWeights, difference);

            if(nearestWeight != -1) {
                availableWeights.remove((Integer)nearestWeight);
            } else {
                nearestWeight = getNearestWeight(leftWeights, difference);
                leftWeights.remove((Integer)nearestWeight);
            }

            rightWeights.add(nearestWeight);
            return balanceInternal(difference, availableWeights);
        } else {
            //Gleichgewicht ist erreicht
            bestState = new ScaleState(new ArrayList<>(leftWeights), new ArrayList<>(rightWeights), true);
            return true;
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

    public int getPlatformDifference() {
        return Utils.sumIntegerList(rightWeights) - Utils.sumIntegerList(leftWeights);
    }

    public record ScaleState(ArrayList<Integer> leftWeights, ArrayList<Integer> rightWeights, boolean done) { }

}